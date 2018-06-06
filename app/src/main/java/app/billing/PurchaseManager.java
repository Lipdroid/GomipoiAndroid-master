package app.billing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.billing.util.IabHelper;
import app.billing.util.IabResult;
import app.billing.util.Inventory;
import app.billing.util.Purchase;
import app.billing.util.SkuDetails;

/**
 * アプリ内課金の管理クラス
 */
public class PurchaseManager implements IabHelper.OnIabSetupFinishedListener, IabHelper.QueryInventoryFinishedListener, IabHelper.OnIabPurchaseFinishedListener {

	// ------------------------------
	// Enum
	// ------------------------------
	public enum StoreItem {
		noItem(0),
		jewel_10(1),
		jewel_50(2),
		jewel_100(3),
		jewel_300(4),
		jewel_500(5),
		jewel_1000(6),
		;

		private int mValue;

		StoreItem(int value) {
			mValue = value;
		}

		public int getValue() {
			return mValue;
		}

		public static StoreItem valueOf(int value) {
			StoreItem[] values = StoreItem.values();

			for (StoreItem item : values) {
				if (item.mValue == value) {
					return item;
				}
			}
			return noItem;
		}
	}

	public enum PurchaseResult {
		// デバイス未対応
		PurchaseResult_unsupportedDevice,
		// google play ストアに接続できなかった
		PurchaseResult_failurePurchaseSession,
		// 課金利用可能
		PurchaseResult_availablePurchase,
		// 不明なエラー
		PurchaseResult_unknownError,
		// 購入完了
		PurchaseResult_purchased,
		// 購入済
		PurchaseResult_hasPurchased,
		// ユーザキャンセル
		PurchaseResult_userCancel,
		// サーバエラー（アイテムが見つからない等）
		PurchaseResult_unavailable,
		// apkエラー等
		PurchaseResult_devError,
	}

	// ------------------------------
	// Define
	// ------------------------------
	public static int CODE_ACTIVITY_RESULT_PURCHASE = 12321;

	// 通知アイテムの取得用キー
	public static final String KEY_RESULT_CODE = "resultCode";

	private static final StoreItem[] allpurchaseItem = {StoreItem.jewel_10, StoreItem.jewel_50, StoreItem.jewel_100, StoreItem.jewel_300, StoreItem.jewel_500, StoreItem.jewel_1000};

	// ------------------------------
	// Member
	// ------------------------------
	private OnPurchaseListener mPurchaseListener = null;
	private OnQueryInventoryFinishListener mInventoryFinishListener = null;
	private IabHelper mHelper;

	// 課金処理中かどうか
	private boolean mHasPurchaseSession = false;

	private IabHelper.QueryInventoryFinishedListener mQueryInventoryFinishedListener = null;

	// コールバックの設定必須
	// private Handler mSetupFinishHandler;
	// private Handler mPurchaseFinishHandler;

	// ------------------------------
	// Constructor
	// ------------------------------
	/**
	 * Constructor
	 */
	public PurchaseManager(Context context, String key) {
		mHelper = new IabHelper(context, key);
	}

	// ------------------------------
	// Native
	// ------------------------------
	/**
	 * 課金の public key取得(byte[])
	 */
	public static native byte[] getPurchaseItem();

	/**
	 * ストアで使用するId取得
	 */
	public static native byte[] getStoreItem(int type);

	/**
	 * 課金処理後取得できるアイテムIdから整数値を取得
	 */
	public static native int getStoreItemValue(String itemId);

	/**
	 * idからpayload生成
	 */
	public static native byte[] createPayload(String itemId);

	/**
	 * developer payload の比較
	 */
	public static native boolean verifyPayload(String payload, String productId);

	public static native boolean decrypt(byte[] byteData);

	public static native boolean encryptBuffer(byte[] byteData);

	// ------------------------------
	// Implements
	// ------------------------------
    /*
     * (非 Javadoc) 課金設定完了後に呼ばれる
     */
	@Override
	public void onIabSetupFinished(IabResult result) {
		boolean canPurchase = result.isSuccess();
		if (!canPurchase) {
			// notification error
			if (mPurchaseListener != null) {
				int responseCode = result.getResponse();
				PurchaseResult unavailableResult = PurchaseResult.PurchaseResult_unsupportedDevice;
				if (responseCode == IabHelper.IABHELPER_BAD_RESPONSE)
					if (responseCode == IabHelper.IABHELPER_REMOTE_EXCEPTION) {
						unavailableResult = PurchaseResult.PurchaseResult_failurePurchaseSession;
					}
				mPurchaseListener.onSetupFinished(false, unavailableResult,
						null);
			}
			return;
		}

		// アイテム設定
		List<String> allSkus = getPurchaseItemList();

		mHelper.queryInventoryAsync(true, allSkus, this);
	}

	/*
     * (非 Javadoc) アイテム情報の確認完了後
     */
	@Override
	public void onQueryInventoryFinished(IabResult result, Inventory inv) {

		if (result.isSuccess()) {
			notifyCheckInventoryFinished(inv);
		} else {
			// 課金ができないエラーを返す
			if (mPurchaseListener != null) {
				mPurchaseListener.onSetupFinished(false,
						PurchaseResult.PurchaseResult_unavailable, null);
			}
		}
	}

	/*
     * (非 Javadoc) 課金処理終了後
     */
	@Override
	public void onIabPurchaseFinished(IabResult result, Purchase info) {
		// 課金パラメータの取得
		PurchaseResult purchaseResult = PurchaseResult.PurchaseResult_unknownError;
		int response = result.getResponse();
		switch (response) {
			case IabHelper.BILLING_RESPONSE_RESULT_OK:
				// 購入完了後の処理
				purchaseResult = PurchaseResult.PurchaseResult_purchased;
				break;
			case IabHelper.BILLING_RESPONSE_RESULT_DEVELOPER_ERROR:
			case IabHelper.IABHELPER_VERIFICATION_FAILED:
				// おそらくapkがおかしい
				purchaseResult = PurchaseResult.PurchaseResult_devError;
				break;
			case IabHelper.BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED:
				// すでにアイテム所有済み
				purchaseResult = PurchaseResult.PurchaseResult_hasPurchased;
				break;
			case IabHelper.BILLING_RESPONSE_RESULT_ITEM_UNAVAILABLE:
				// サーバーからアイテム情報が正しく取得できない
				purchaseResult = PurchaseResult.PurchaseResult_unavailable;
				break;
			case IabHelper.BILLING_RESPONSE_RESULT_ERROR:
				// 不明なエラー
				purchaseResult = PurchaseResult.PurchaseResult_unknownError;
				break;
			case IabHelper.BILLING_RESPONSE_RESULT_USER_CANCELED:
			case IabHelper.IABHELPER_USER_CANCELLED:
				// キャンセルが発生した場合はなにもしない
				purchaseResult = PurchaseResult.PurchaseResult_userCancel;
				break;
			default:
				purchaseResult = PurchaseResult.PurchaseResult_unknownError;
				break;
		}

		if (purchaseResult == PurchaseResult.PurchaseResult_purchased) {
			// check payload
			boolean verifiedPayload = verifyPayload(info);
			if (!verifiedPayload) {
				// 購入情報が正しくない場合はエラーを返す
				purchaseResult = PurchaseResult.PurchaseResult_unknownError;
			}
		}

		mHasPurchaseSession = false;

		// storeItem
		StoreItem selectedItem = StoreItem.noItem;
		if (info != null && purchaseResult.equals(PurchaseResult.PurchaseResult_purchased)) {
			selectedItem = getStoreInfo(info);
		}

		if (mPurchaseListener != null) {
			mPurchaseListener.onPurchaseEventFinished(purchaseResult, selectedItem, info);
		}
	}

	// ------------------------------
	// Accesser
	// ------------------------------
//	/**
//	 * IabHelperのインスタンスを返す
//	 */
//	public IabHelper getHelper() {
//		return mHelper;
//	}

	/**
	 * onActivityResult時に呼ぶ
	 */
	public boolean handleActivityResult(int requestCode, int resultCode, Intent data) {
		if (mHelper == null) {
			return false;
		}

		return mHelper.handleActivityResult(requestCode, resultCode, data);
	}

	/**
	 * onCreate時に呼ぶ (リスナーが必要な場合)
	 */
	public void setup(OnPurchaseListener listener) {
		mPurchaseListener = listener;
		setup();
	}

	/**
	 * onCreate時に呼ぶ (リスナーが不要な場合)
	 */
	public void setup() {
		if (mHelper != null) {
			mHelper.startSetup(this);
		}
	}

	/**
	 * onDestroy時に呼ぶ
	 */
	public void shutdown() {
		mQueryInventoryFinishedListener = null;
		if (mHelper != null) {
			mHelper.dispose();
		}
	}

	/**
	 * 購入処理を行う
	 */
	public PurchaseResult purchase(StoreItem item, Activity activity, int requestCode, String extraData) {
		int purchaseItem = item.getValue();

		// 課金アイテムの取得
		byte[] productByte = getStoreItem(purchaseItem);

		if (productByte == null) {
			// 不明な値がかえってきたためエラー表示
			return PurchaseResult.PurchaseResult_devError;
		}

		String sku = null;
		try {
			sku = new String(productByte, "UTF-8");
			// developerPayload は内部で設定済み
			purchaseFlow(activity, sku, requestCode, extraData);
		} catch (UnsupportedEncodingException e) {
			// 不正な値がかえってきた場合にエラー表示
			return PurchaseResult.PurchaseResult_devError;
		} catch (Exception e) {
			return PurchaseResult.PurchaseResult_devError;
		}

		return PurchaseResult.PurchaseResult_purchased;
	}

	/**
	 * 購入処理を行う
	 */
	public boolean purchaseFlow(Activity activity, String sku, int requestCode, String extraData) {
		if (mHasPurchaseSession) {
			return false;
		}
		mHasPurchaseSession = true;
		boolean result = false;
		String itemType = IabHelper.ITEM_TYPE_INAPP;
		// createPayment

		String developerPayload = null;
		if (!sku.equals("android.test.purchased")) {
			 developerPayload = new String(createPayload(sku));
		}

		mHelper.launchPurchaseFlow(activity, sku, itemType, requestCode, this, developerPayload);

		return result;
	}

	/**
	 * ユーザーの所有アイテムを確認する(復元処理)
	 */
	public void queryInventory(OnQueryInventoryFinishListener inventoryFinishListener) {
		mInventoryFinishListener = inventoryFinishListener;
		if (mHelper == null) {
			if (mInventoryFinishListener != null) {
				mInventoryFinishListener.onQueryInventoryFinished(null);
			}

			mInventoryFinishListener = null;
		}

		List<String> allSkus = getPurchaseItemList();

		mQueryInventoryFinishedListener = getQueryInventoryFinishedListener(allSkus);
		mHelper.queryInventoryAsync(true, allSkus, mQueryInventoryFinishedListener);
	}

	/**
	 * アイテムを消費する(消費型アイテムの際に使用する)
	 */
	public void consumePurchaseItem(Purchase purchase) {
		if (mHelper == null || purchase == null) {
			return;
		}

		mHelper.consumeAsync(purchase, new IabHelper.OnConsumeFinishedListener() {

			@Override
			public void onConsumeFinished(Purchase purchase, IabResult result) {

			}
		});
	}

	/**
	 * アイテムを消費する(消費型アイテムの際に使用する)
	 */
	public void consumePurchaseItems(List<Purchase> purchases) {
		if (mHelper == null || purchases == null) {
			return;
		}

		mHelper.consumeAsync(purchases, new IabHelper.OnConsumeMultiFinishedListener() {

			@Override
			public void onConsumeMultiFinished(List<Purchase> purchases,
											   List<IabResult> results) {
			}
		});
	}

	// ------------------------------
	// Function
	// ------------------------------
	/**
	 * 問い合わせをして課金情報(Payload)が正しいかチェックする
	 */
	private boolean verifyPayload(Purchase purchase) {
		boolean result = true;

		String payload = purchase.getDeveloperPayload();
		String sku = purchase.getSku();

		result = verifyPayload(payload, sku);

		return result;
	}

	/**
	 * アイテムのID取得
	 */
	private String getPurchaseItem(StoreItem storeItem) throws UnsupportedEncodingException {
		byte[] itemBytes = getStoreItem(storeItem.getValue());
		return new String(itemBytes, "UTF-8");
	}

	/**
	 * アイテムのIDリスト取得
	 */
	private List<String> getPurchaseItemList() {
		List<String> list = new ArrayList<String>();

		for (StoreItem purchaseItem : allpurchaseItem) {
			try {
				String itemName = getPurchaseItem(purchaseItem);
				list.add(itemName);
			} catch (UnsupportedEncodingException e) {
				return null;
			}
		}
		return list;
	}

	/**
	 * ユーザーの所有アイテム確認のリスナーを返す
	 */
	private IabHelper.QueryInventoryFinishedListener getQueryInventoryFinishedListener(final List<String> allSkus) {
		return new IabHelper.QueryInventoryFinishedListener() {

			@Override
			public void onQueryInventoryFinished(IabResult result, Inventory inv) {
				HashMap<StoreItem, Purchase> storeItems = getAvailableRestoreItemList(allSkus, inv);

				if (mInventoryFinishListener != null) {
					mInventoryFinishListener.onQueryInventoryFinished(storeItems);
				}
			}
		};
	}

	/**
	 * アイテムの状況をチェックを完了したことを通知する
	 */
	private void notifyCheckInventoryFinished(Inventory inventory) {
		if (inventory == null) {
			// なんらかの形でinventoryが取得できなかった場合はエラーを渡す
			if (mPurchaseListener != null) {
				// エラー通知を送る
				mPurchaseListener.onSetupFinished(false,
						PurchaseResult.PurchaseResult_failurePurchaseSession,
						null);
			}
		} else {
			// 金額情報の作成
			Map<StoreItem, String> priceInfo = new HashMap<StoreItem, String>();

			// 取得する
			for (StoreItem storeItem : allpurchaseItem) {
				try {
					String sku = getPurchaseItem(storeItem);
					SkuDetails skuDetails = inventory.getSkuDetails(sku);
					if (skuDetails == null) {
						continue;
					}
					String price = skuDetails.getPrice();
					if (price.startsWith("￥")) {
						price = price.replaceAll("￥", "¥");
					}
					priceInfo.put(storeItem, price);
				} catch (UnsupportedEncodingException e) {
					// データが不正であればなにも返さない
					// エラー通知を送る
					mPurchaseListener
							.onSetupFinished(
									false,
									PurchaseResult.PurchaseResult_failurePurchaseSession,
									null);
					return;
				}
			}

			// ここまでくれば正確なデータが取得できているはずなので
			if (mPurchaseListener != null) {
				mPurchaseListener.onSetupFinished(true,
						PurchaseResult.PurchaseResult_availablePurchase,
						priceInfo);
			}
		}
	}

	/**
	 * 購入情報からストアタイプを取得する
	 */
	private StoreItem getStoreInfo(Purchase purchase) {
		StoreItem storeItem = StoreItem.noItem;
		String sku = purchase.getSku();

		if (sku == null) {
			return StoreItem.noItem;
		}

		int itemIdentifer = getStoreItemValue(sku);
		storeItem = StoreItem.valueOf(itemIdentifer);

		return storeItem;
	}

	/**
	 * 復元可能なアイテムのリストを取得する セットアイテムは展開して渡す
	 */
	private HashMap<StoreItem, Purchase> getAvailableRestoreItemList(List<String> allSkus,
																	 Inventory inventory) {
		HashMap<StoreItem, Purchase> itemMap = new HashMap<StoreItem, Purchase>();

		for (String sku : allSkus) {
			if(inventory == null)
				break;
			Purchase purchase = inventory.getPurchase(sku);
			if (purchase == null) {
				continue;
			}
			// check payload
			boolean availableRestore = verifyPayload(purchase);
			if (availableRestore) {
				String restoreSku = purchase.getSku();
				int restoreValue = getStoreItemValue(restoreSku);
				StoreItem availableItem = StoreItem.valueOf(restoreValue);
				if (!availableItem.equals(StoreItem.noItem)) {
					itemMap.put(availableItem, purchase);
				}
			}
		}
		return itemMap;
	}

	// ------------------------------
	// Interface
	// ------------------------------
	/**
	 * アイテム状況の問い合わせ
	 */
	public interface OnQueryInventoryFinishListener {
		void onQueryInventoryFinished(HashMap<StoreItem, Purchase> storeItems);
	}

	public interface OnPurchaseListener {

		/**
		 * 課金の準備が完了した際に呼ばれる
		 */
		void onSetupFinished(boolean availablePurchase, PurchaseResult result, Map<StoreItem, String> skuPriceMap);

		/**
		 * 課金イベントが完了した際に呼ばれる
		 */
		void onPurchaseEventFinished(PurchaseResult result, StoreItem storeItem, Purchase info);

		void onRestoreTransactionFinished(List<StoreItem> storeItems);
	}
}
