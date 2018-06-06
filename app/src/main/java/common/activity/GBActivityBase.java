package common.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.View;
import android.webkit.HttpAuthHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.firebase.iid.FirebaseInstanceId;
import com.topmission.gomipoi.R;
import com.topmission.gomipoi.SplashActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import app.activity.ExchangeActivity;
import app.activity.FriendActivity;
import app.activity.GameActivity;
import app.activity.HelpActivity;
import app.activity.InfoActivity;
import app.activity.LoginActivity;
import app.activity.NewsActivity;
import app.activity.QRReadActivity;
import app.activity.RankingActivity;
import app.activity.SettingsActivity;
import app.activity.ShopActivity;
import app.activity.ShowQRActivity;
import app.activity.TopActivity;
import app.activity.TutorialActivity;
import app.animation.translate.TranslateAnimation;
import app.application.GBApplication;
import app.application.OnApplicationListener;
import app.billing.PurchaseManager;
import app.billing.util.Purchase;
import app.data.BookGarbageData;
import app.data.ItemDialogResponse;
import app.data.ListItemData;
import app.data.SharedCaptureData;
import app.data.ShopItem;
import app.data.friend.FriendMessageDialogResponse;
import app.data.friend.IconCanSelectDialogResponse;
import app.data.friend.SearchResponse;
import app.data.http.FriendCodeUseParam;
import app.data.http.FriendInvitationsParam;
import app.data.http.FriendMessagesResponse;
import app.data.http.FriendPresentRequestsParam;
import app.data.http.FriendPresentsParam;
import app.data.http.FriendPresentsUseParam;
import app.data.http.FriendsForPresentRequestResponse;
import app.data.http.FriendsSearchByCodeParam;
import app.data.http.FriendsSearchByCodeResponse;
import app.data.http.FriendshipBonusParam;
import app.data.http.GomipoiBookCheckParam;
import app.data.http.GomipoiBookOwnParam;
import app.data.http.GomipoiBookReceiveBonusesParam;
import app.data.http.GomipoiFriendsRankResponse;
import app.data.http.GomipoiFriendsResponse;
import app.data.http.GomipoiGameMovePlaceParam;
import app.data.http.GomipoiGameSaveParam;
import app.data.http.GomipoiGarbageFoundParam;
import app.data.http.GomipoiGarbageRecipeParam;
import app.data.http.GomipoiGarbageSynthesesParam;
import app.data.http.GomipoiItemSetBuyParam;
import app.data.http.GomipoiItemUseParam;
import app.data.http.GomipoiJirokichiBonusesParams;
import app.data.http.MessagesResponse;
import app.data.http.NewsTopicCheckResponse;
import app.data.http.RegisterDeviceTokenParam;
import app.data.http.SystemMessagesResponse;
import app.data.http.UserAppJewelParam;
import app.data.http.UserAppPointParam;
import app.data.http.UserDefinitiveRegisterParam;
import app.data.http.UserGuestRegisterResponce;
import app.data.http.UserSessionsParam;
import app.data.http.UsersParam;
import app.define.BroomType;
import app.define.ChangeActivityCode;
import app.define.ConnectionCode;
import app.define.DebugMode;
import app.define.DialogCode;
import app.define.FragmentEventCode;
import app.define.FriendActionCode;
import app.define.GarbageCanType;
import app.define.ItemCode;
import app.define.ItemId;
import app.define.SeData;
import app.define.StageType;
import app.dialog.CaptureWaitingDialog;
import app.dialog.FriendMessageDialog;
import app.dialog.GameMenuDialog;
import app.dialog.IconCanSelectDialog;
import app.dialog.IndicatorDialog;
import app.dialog.ItemBuyDialog;
import app.dialog.ItemBuyTicketDialog;
import app.dialog.ItemBuyTicketSuccessDialog;
import app.dialog.ItemDialog;
import app.dialog.ItemUseDialog;
import app.dialog.MessageDialog;
import app.dialog.NewsDialog;
import app.dialog.Notice1Dialog;
import app.dialog.Notice2Dialog;
import app.dialog.Notice3Dialog;
import app.dialog.PictureBookDetailDialog;
import app.dialog.PictureBookDialog;
import app.dialog.PicturePoiDialog;
import app.dialog.PicturePoiResultGetDialog;
import app.dialog.RoomSecretDialog;
import app.dialog.ScrollDialog;
import app.dialog.SearchFriendDialog;
import app.dialog.SynthesisDialog;
import app.dialog.SynthesisSuccessDialog;
import app.dialog.VersionUpDialog;
import app.dialog.define.MessageDialogType;
import app.fragment.ExchangeFragment;
import app.fragment.FriendListFragment;
import app.fragment.FriendListRankFragment;
import app.fragment.GameFragment;
import app.fragment.RegistFragment;
import app.fragment.TopFragment;
import app.http.ConnectionManager;
import app.jni.JniBridge;
import app.notification.InstanceIDListenerService;
import app.number.NumberUtils;
import app.preference.PJniBridge;
import app.sound.BgmManager;
import app.uuid.OnUUIDManagerListener;
import app.uuid.UUIDManager;
import lib.activity.FragmentActivityBase;
import lib.dialog.DialogBase;
import lib.fragment.FragmentTranslateAnimationBase;
import lib.log.DebugLog;
import lib.qrcode.QRCodeUtil;


/**
 *
 */
public class GBActivityBase extends FragmentActivityBase
        implements ConnectionManager.OnConnectionManagerListener, OnApplicationListener
                , OnUUIDManagerListener{

    // ------------------------------
    // Enum
    // ------------------------------
    public enum ContactType {
        NORMAL,
        LOGIN_PASSWORD,
    }

    // -----------------------------
    // Define
    // ------------------------------
    public static final int PERMISSION_EXSTORAGE_REQUEST_FOR_UUID = 101;
    public static final int GEM_EXCHANGE_COUNT = 10;    // 写真ポイで宝石と交換する枚数

    private static final String dForcedUpdateAction = "forcedUpdateNotificationAction";
    private static final String dMaintenanceAction = "maintenanceNotificationAction";


    public static final int SNS_SHARE_REQUEST_CODE = 201;

    public static final int READ_QR_CODE_REQUEST_CODE = 202;
    // ------------------------------
    // Member
    // ------------------------------
    private String mDeviceId;

    private View layoutNews;
    private WebView webViewNews;

    private FragmentTranslateAnimationBase mTranslateAnimation;
    private ConnectionManager mConnectionManager;

    private IndicatorDialog mConnectionDialog;
    private MessageDialog mErrorDialog;
    private MessageDialog mNetworkErrorkDialog;
    private MessageDialog mNetworkErrorWithRetryDialog;
    private MessageDialog mGetNetworkErrorWithRetryDialog;
    private MessageDialog mServiceNetworkErrorWithRetryDialog;
    private boolean mIsNeedGetRetry;

    private CaptureWaitingDialog mCaptureWaitingDialog;

    private ScrollDialog mScrollDialog;

    private ForcedUpdateNotificationReceiver mForcedUpdateNotificationReceiver;
    private MaintainanceNotificationReceiver mMaintainanceNotificationReceiver;

    // 課金
    private PurchaseManager mPurchaseManager;

    // ------------------------------
    // Override
    // ------------------------------
    @Override
    protected boolean isHiddenActionBar() {
        return true;
    }

    @Override
    protected boolean isKeepScreenOn() {
        return true;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        // アプリの再起動が必要な場合はアプリの再起動処理を行う
        if (getApp() == null || getApp().isNeedRelunchar()) {
            this.finish();
            if (this instanceof TopActivity || this instanceof TutorialActivity || this instanceof LoginActivity) {
                startActivity(new Intent(getApplicationContext(), SplashActivity.class));
            }
            return;
        }

        getDeviceId();

        //課金用
        byte[] getData = PurchaseManager.getPurchaseItem();
        String key = new String(getData);
        mPurchaseManager = new PurchaseManager(getApplicationContext(), key);
        mPurchaseManager.setup(new PurchaseManager.OnPurchaseListener() {

            /*
             * (非 Javadoc) 課金アイテムチェック後呼ばれる
             */
            @Override
            public void onSetupFinished(boolean availablePurchase, PurchaseManager.PurchaseResult result, Map<PurchaseManager.StoreItem, String> skuPriceMap) {
                if (availablePurchase && mPurchaseManager != null) {

                    // アクセストークンを持っているときだけ復元処理を行う
                    if (getApp().getAccessToken() != null)
                    {
                        mPurchaseManager.queryInventory(new PurchaseManager.OnQueryInventoryFinishListener() {

                            @Override
                            public void onQueryInventoryFinished(HashMap<PurchaseManager.StoreItem, Purchase> storeItems) {
                                if (storeItems != null && storeItems.size() > 0) {
                                    handleRestoreEvent(storeItems);
                                }
                            }

                        });
                    }

                } else {
                    // error dialogで返してストア画面終了
                    if (result == PurchaseManager.PurchaseResult.PurchaseResult_unsupportedDevice) {

                    } else if (result == PurchaseManager.PurchaseResult.PurchaseResult_failurePurchaseSession) {

                    }
                }
            }

            @Override
            public void onPurchaseEventFinished(PurchaseManager.PurchaseResult result, PurchaseManager.StoreItem storeItem, Purchase purchaseInfo) {
                handlePurchaseEvent(result, storeItem, purchaseInfo);
            }
            @Override
            public void onRestoreTransactionFinished(List<PurchaseManager.StoreItem> storeItems) {

            }
        });
    }

    @Override
    protected void onCreateHandler(Bundle bundler) {
        super.onCreateHandler(bundler);

        // リサイズ処理
        View view = findViewById(getResizeBaseViewId());
        if (view != null) {
            GBApplication application = getApp();
            if (application != null) {
                application.getResizeManager().resizeFragmentContainer(view);
            }
        }

        // お知らせViewと処理の作成
        layoutNews = findViewById(R.id.layoutNews);
        if (layoutNews != null) {
            webViewNews = (WebView) layoutNews.findViewById(R.id.webViewNews);
            if (webViewNews != null) {
                webViewNews.clearCache(true);
                webViewNews.getSettings().setJavaScriptEnabled(true);
                webViewNews.getSettings().setLoadWithOverviewMode(true);
                webViewNews.getSettings().setUseWideViewPort(true);
                webViewNews.getSettings().setDefaultTextEncodingName("UTF-8");
                webViewNews.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
                        // Basic認証は開発用のみ使用
                        if (DebugMode.isTestServer){
                            handler.proceed(PJniBridge.nativeGetBasicUsername(), PJniBridge.nativeGetBasicPassword());
                        }
                    }

                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        if (layoutNews != null) {
                            layoutNews.setVisibility(View.GONE);
                        }

                        if (url.endsWith("close_action")) {
                            DebugLog.i("GBActivityBase - onCreateHandler called");
                            if (!getApp().getPreferenceManager().getNotice1Hidden()
                                    && getApp().getPlayerManager().isNeedShowNotice1Dialog()) {
                                onShowDialog(DialogCode.Notice1.getValue(), null);
                            } else if (!getApp().getPreferenceManager().getNotice2Hidden()
                                    && getApp().getPlayerManager().isNeedShowNotice2Dialog()) {
                                onShowDialog(DialogCode.Notice2.getValue(), null);
                            } else if (!getApp().getPreferenceManager().getNotice3Hidden()
                                    && getApp().getPlayerManager().isNeedShowNotice3Dialog()) {
                                onShowDialog(DialogCode.Notice3.getValue(), null);
                            }
                            return true;
                        }

                        onChangedActivity(ChangeActivityCode.News.getValue(), url);
                        return true;
                    }
                });
            }
        }

        setForcedUpdateNotificationReceiver();
        setMaintainanceNotificationReceiver();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SNS_SHARE_REQUEST_CODE) {
            // TODO 情報取得

            return;
        }
        if (requestCode == READ_QR_CODE_REQUEST_CODE) {
            // result QRCode
            if (resultCode == Activity.RESULT_OK) {
                // get result text
                String friendCode = QRCodeUtil.friendCode(data.getStringExtra(QRReadActivity.RESULT_TEXT_KEY));
                onShowDialog(DialogCode.SearchFriend.getValue(), friendCode);
            }
            return;
        }
        if (!onPurchaseResultGot(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        } else {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // アプリの再起動が必要な場合に、再起動処理を行う
        if (getApp() == null || getApp().isNeedRelunchar()) {
            this.finish();
            if (this instanceof TopActivity || this instanceof TutorialActivity || this instanceof LoginActivity) {
                startActivity(new Intent(getApplicationContext(), SplashActivity.class));
            }
            return;
        }

        // ログアウト処理中なら、ログアウト処理を行う
        if (!(this instanceof TopActivity) && getApp().isLogouting()) {
            logoutedFinish();
            return;
        }

        // ApplicationListenerをセットする
        getApp().setApplicationListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 必要ならBGMを再生する
        if (getApp() != null) {
            if (isNeedBgmPlay()) {
                getApp().getBgmManager().changeBgm(getBgmType());
                getApp().getBgmManager().playAndRetain();
            }
        }
    }

    @Override
    protected void onStop() {
        // 必要ならBGMを止める
        if (getApp() != null && isNeedBgmPlay()) {
            getApp().getBgmManager().stopAndRelease();
        }
        super.onStop();
    }

    @Override
    protected void onPause() {
        // ApplicationListenerを取り消す
        if (getApp() != null) {
            getApp().setApplicationListener(null);
        }

        super.onPause();
    }

    @Override
    public void onBackPressed() {
        // お知らせを表示していたら、そっちの処理を優先
        if (layoutNews != null && layoutNews.getVisibility() == View.VISIBLE) {
            if (webViewNews != null && webViewNews.canGoBack()) {
                webViewNews.goBack();
                return;
            }
            layoutNews.setVisibility(View.GONE);
            DebugLog.i("GBActivityBase - onBackPressed");
            if (!getApp().getPreferenceManager().getNotice1Hidden()
                    && getApp().getPlayerManager().isNeedShowNotice1Dialog()) {
                onShowDialog(DialogCode.Notice1.getValue(), null);
            } else if (!getApp().getPreferenceManager().getNotice2Hidden()
                    && getApp().getPlayerManager().isNeedShowNotice2Dialog()) {
                onShowDialog(DialogCode.Notice2.getValue(), null);
            } else if (!getApp().getPreferenceManager().getNotice3Hidden()
                    && getApp().getPlayerManager().isNeedShowNotice3Dialog()) {
                onShowDialog(DialogCode.Notice3.getValue(), null);
            }
            return;
        }

        // WebView内の処理はWebViewで行う　2016.4.25変更
//        // WebViewの処理を優先
//        Fragment fragment = getCurrentActiveFragment();
//        if (fragment != null && fragment instanceof WebFragmentBase && ((WebFragmentBase) fragment).goBackWeb()) {
//            return;
//        }

        // 遷移アニメーションをセットして、システムのBack処理
        super.onBackPressed();
        FragmentTranslateAnimationBase animation = getFragmentAnimation();
        if (animation != null) {
            overridePendingTransition(animation.getLeftEnter(), animation.getLeftExit());
        }
    }

    @Override
    protected void onDestroy() {
        unregistForcedUpdateNotificationReceiver();
        unregistMaintainanceNotificationReceiver();

        try {
            if (mPurchaseManager != null) {
                mPurchaseManager.shutdown();
            }
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }

        super.onDestroy();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            super.startActivityForResult(intent, requestCode, options);
        } else {
            super.startActivityForResult(intent, requestCode);
        }

        // startActivity系に遷移アニメーションをセットする
        FragmentTranslateAnimationBase animation = getFragmentAnimation();
        if (animation != null) {
            overridePendingTransition(animation.getLeftEnter(), animation.getLeftExit());
        }
    }

    @Override
    protected FragmentTranslateAnimationBase getFragmentAnimation() {
        if (mTranslateAnimation == null) {
            mTranslateAnimation = new TranslateAnimation();
        }
        return mTranslateAnimation;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
            @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_EXSTORAGE_REQUEST_FOR_UUID: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getDeviceId();
                    return;
                }

                // 権限エラー
                onShowDialog(DialogCode.PermissionError.getValue(), null);
                return;
            }

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    // ------------------------------
    // OnFragmentListener
    // ------------------------------
    @Override
    public void onChangedActivity(int requestCode, Object data) {
        super.onChangedActivity(requestCode, data);

        switch (ChangeActivityCode.valueOf(requestCode)) {
            case Top: {
                startActivity(new Intent(getApplicationContext(), TopActivity.class));
                break;
            }
            case Game: {
                startActivity(new Intent(getApplicationContext(), GameActivity.class));
                break;
            }
            case Ranking: {
                startActivity(new Intent(getApplicationContext(), RankingActivity.class));
                break;
            }
            case Exchange: {
                startActivity(new Intent(getApplicationContext(), ExchangeActivity.class));
                break;
            }
            case Shop: {
                startActivity(new Intent(getApplicationContext(), ShopActivity.class));
                break;
            }
            case Settings: {
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                break;
            }
            case Help: {
                startActivity(new Intent(getApplicationContext(), HelpActivity.class));
                break;
            }
            case SNS: {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String title = "";
                String extraText = "ポイントを貯めて楽しく商品をゲットしよう！\n";
                if (!getApp().getIsGuest()) {
                    // 会員ユーザーのみフレンドコードを含む
                    title = "友達を招待して\nフレンドを増やそう！";
                    extraText += ("[" + getApp().getFriendCode() + "]でフレンド検索してね！\n");
                }
                extraText += "http://exchange-appli.com/downroad.html";
                intent.putExtra(Intent.EXTRA_TEXT, extraText);
                startActivity(Intent.createChooser(intent, title));
                break;
            }
            case News: {
                Intent intent = new Intent(getApplicationContext(), NewsActivity.class);
                intent.putExtra(NewsActivity.EXTRAS_KEY_URL, (String)data);
                startActivity(intent);
                break;
            }

            case CaptureShare: {
                SharedCaptureData capture = (SharedCaptureData)data;

                // Intent発行
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, capture.text);
                intent.putExtra(Intent.EXTRA_STREAM, capture.image);
                intent.setType("image/*");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(intent, "自分の部屋をシェアしてみよう！"));
                break;
            }

            case Friend: {
                startActivity(new Intent(getApplicationContext(), FriendActivity.class));
                break;
            }

            case InstallOther: {
                int appId = (int)data;
                String packageName = null;

                switch (appId) {
                    case 1001:
                        packageName = "com.topmission.janpoi";
                        break;

                    case 1002:
                        packageName = "com.topmission.gomipoi";
                        break;
                }

                if (packageName == null) {
                    unlockEvent();
                    return;
                }

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName));
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }

            case Info: {
                startActivity(new Intent(getApplicationContext(),InfoActivity.class));
                break;
            }

            case Contact: {
                ContactType type = (ContactType) data;

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SENDTO);
                intent.setType("text/plain");
                intent.setData(Uri.parse("mailto:info.excapp@gmail.com"));
                String message = "";
                if (type.equals(ContactType.NORMAL)) {
                    message = String.format("\n\n[お問い合わせID]\n%s",getDeviceId());
                } else if(type.equals(ContactType.LOGIN_PASSWORD)) {
                    intent.putExtra(Intent.EXTRA_SUBJECT, "パスワード再発行申請 ゴミPOIにPOI/Android");
                    message = String.format("パスワードを紛失された場合は、パスワードの再発行をいたします。\nユーザーを特定するために、以下の項目をご記入ください。\n内容確認後、折り返しご連絡いたします。\n\n※ニックネームのみ記入される場合は、ユーザーの特定・ご本人確認ができない場合があります。\n※ユーザーが特定できない場合は、パスワードを再発行できない可能性があります。\n\n[ニックネーム(ランキング表示名)]\n\n\n[ログインID(半角英数字6〜20文字)]\n\n\n[その他]\n\n\n\n\n\n[お問い合わせID]\n%s",getDeviceId());

                }
                intent.putExtra(Intent.EXTRA_TEXT, message);
                startActivity(Intent.createChooser(intent, null));

                break;
            }
            case ReadQR: {
                Intent intent = new Intent(this, QRReadActivity.class);
                startActivityForResult(intent, READ_QR_CODE_REQUEST_CODE);
                break;
            }
            case ShowQR: {
                Intent intent = new Intent(this, ShowQRActivity.class);
                intent.putExtra(ShowQRActivity.FRIEND_CODE_KEY, (String) data);
                startActivity(intent);
                break;
            }
            default: {
                super.onChangedActivity(requestCode, data);
                break;
            }

        }
    }

    @Override
    public void onEvent(int eventCode, Object data) {
        switch (FragmentEventCode.valueOf(eventCode)) {
            case OnResume: {
                getConnectionManager(false).onResume();
                break;
            }

            case OnPause: {
                getConnectionManager(false).onPause();
                break;
            }

            case PostUser: {
                if (data == null || !(data instanceof UsersParam)) {
                    showErrorDialog();
                    return;
                }

                getConnectionManager().userRegist(
                        (UsersParam) data,
                        new ConnectionManager.OnUserRegistListener() {

                    @Override
                    public void UserRegist_Ok() {
                        onChangedActivity(ChangeActivityCode.Top.getValue(), null);
                    }

                    @Override
                    public void UserRegist_OnShowExistSameAccountError() {
                        onShowDialog(DialogCode.ExistSameAccountError.getValue(), null);
                    }

                    @Override
                    public void UserRegist_OnNotMatchUserError() {
                        onShowDialog(DialogCode.NotMatchUserInfo.getValue(), null);
                    }

                    @Override
                    public void UserRegist_OnShowExistSameNicknameError() {
                        onShowDialog(DialogCode.ExistSameNicknameError.getValue(), null);
                    }

                    @Override
                    public void UserRegist_OnShowInvalidPasswordError() {
                        onShowDialog(DialogCode.InvalidPasswordError.getValue(), null);
                    }

                        });
                break;
            }

            case PostUserSessions: {
                if (data == null || !(data instanceof UserSessionsParam)) {
                    showErrorDialog();
                    return;
                }

                final UserSessionsParam param = (UserSessionsParam) data;

                getConnectionManager().userLogin(
                        (UserSessionsParam) data,
                        new ConnectionManager.OnUserLoginListener() {
                    @Override
                    public void UserLogin_Ok() {
                        onChangedActivity(ChangeActivityCode.Top.getValue(), null);

//                        if (param.deviceToken != null) {
//                            GBPreferenceManager prefManager = new GBPreferenceManager(getApplicationContext());
//                            prefManager.setDeviceTokenNew(false);
//                        }

                        String token = FirebaseInstanceId.getInstance().getToken();
                        if (TopActivity.sShouldSendDeviceToken && token != null) {
                            onEvent(FragmentEventCode.RegisterDeviceToken.getValue(), new RegisterDeviceTokenParam(token));
                        }
                    }

                    @Override
                    public void UserLogin_AutoLoginOk() {
                        onEvent(FragmentEventCode.GetNewsTopicCheck.getValue(), null);
                        initLoading();

//                        if (param.deviceToken != null) {
//                            GBPreferenceManager prefManager = new GBPreferenceManager(getApplicationContext());
//                            prefManager.setDeviceTokenNew(false);
//                        }
                    }

                    @Override
                    public void UserLogin_OnNotMatchUserError() {
                        onShowDialog(DialogCode.NotMatchUserInfo.getValue(), null);
                    }
                });

                break;
            }

            case DeleteUserSessions: {
                getConnectionManager().userLogout();
                break;
            }

            case GetAllUserData: {
                fetchAllData();
                break;
            }

            case GetItemOwn: {
                getConnectionManager().getItemOwn(new ConnectionManager.OnItemOwnListener() {
                    @Override
                    public void ItemOwn_Ok() {
                        DialogBase dialog = getCurrentActiveDialog();
                        if (dialog != null) {
                            if (dialog instanceof ItemDialog) {
                                ((ItemDialog) dialog).refresh();
                            }
                            dialog.unlockEvent();
                        }
                    }
                });
                break;
            }

            case GetGarbageRecipe: {
                getConnectionManager().getGarbageRecipe(new ConnectionManager.OnGarbageRecipeListener() {
                    @Override
                    public void GarbageRecipe_Ok(GomipoiGarbageRecipeParam response) {
                        if (mScrollDialog == null || response == null) {
                            return;
                        }

                        mScrollDialog.setData(response);
                    }
                });
                break;
            }

            case PatchUserAppsJewel: {
                if (data == null || !(data instanceof UserAppJewelParam)) {
                    showErrorDialog();
                    return;
                }

                final UserAppJewelParam addJewelParam = (UserAppJewelParam) data;
                getConnectionManager().addJewel(addJewelParam, new ConnectionManager.OnAddJewelListener() {
                    @Override
                    public void AddJewel_Ok() {
                        JniBridge.nativeAddGem(addJewelParam.add_jewel_count);

                        switch (addJewelParam.method) {
                            case Shop: {
                                if (mPurchaseManager != null) {
                                    mPurchaseManager.consumePurchaseItem(addJewelParam.boughtInfo);
                                }
                                break;
                            }

                            case Restore: {
                                if (mPurchaseManager != null) {
                                    mPurchaseManager.consumePurchaseItems(addJewelParam.restoreInfos);
                                }
                                break;
                            }

                            case PicturePoi: {
                                if (getApp() != null) {
                                    getApp().getPreferenceManager().setPicturePoiDate(JniBridge.nativeGetCurrentDate());
                                    getApp().getPreferenceManager().setPicturePoiCount(addJewelParam.newPoiCount);
                                    HashMap<String, Object> map = new HashMap<>();
                                    map.put("addPoint", addJewelParam.add_jewel_count);
                                    map.put("playSound", true);
                                    onShowDialog(DialogCode.PicturePoiResultGet.getValue(), map);

                                    //onShowDialog(DialogCode.PicturePoiResultGet.getValue(), addJewelParam.newPoiCount / GEM_EXCHANGE_COUNT);
                                } else {
                                    onShowDialog(DialogCode.ERROR.getValue(), null);
                                }
                                return;
                            }
                        }

                        unlockEvent();
                    }

                    @Override
                    public void AddJewel_OnShortageError() {
                        onShowDialog(DialogCode.GemShortage.getValue(), null);
                    }
                });
                break;
            }

            case PatchUserAppsPoint: {
                if (data == null || !(data instanceof UserAppPointParam)) {
                    showErrorDialog();
                    return;
                }

                final UserAppPointParam addPointParam = (UserAppPointParam) data;
                getConnectionManager().addPoint(addPointParam, new ConnectionManager.OnAddPointListener() {
                    @Override
                    public void AddPoint_Ok() {
                        JniBridge.nativeAddPoint(addPointParam.add_point);
                        unlockEvent();
                    }
                });

                break;
            }

            case GetBookOwn: {
                if (data == null || !(data instanceof GomipoiBookOwnParam)) {
                    showErrorDialog();
                    return;
                }

                final GomipoiBookOwnParam bookOwnParam = (GomipoiBookOwnParam) data;
                getConnectionManager().getBookOwn(bookOwnParam, new ConnectionManager.OnBookOwnListener() {
                    @Override
                    public void BookOwn_Ok() {
                        // コンプリートチェック
                        int[] resultArray = JniBridge.nativeGetPageBonus();
                        if (resultArray.length > 0) {
                            // 受け取り通信
                            onEvent(
                                    FragmentEventCode.PostReceiveBonusPages.getValue(),
                                    new GomipoiBookReceiveBonusesParam(resultArray));
                        }

                        DialogBase dialog = getCurrentActiveDialog();
                        if (dialog != null) {
                            if (dialog instanceof PictureBookDialog) {
                                ((PictureBookDialog) dialog).refresh();
                            }
                            dialog.unlockEvent();
                        }
                    }
                });

                break;
            }

            case PostGarbageFound: {
                if (data == null || !(data instanceof GomipoiGarbageFoundParam)) {
                    showErrorDialog();
                    return;
                }

                final GomipoiGarbageFoundParam foundParam = (GomipoiGarbageFoundParam) data;
                getConnectionManager().garbageFound(foundParam, new ConnectionManager.OnGarbageFoundListener() {
                    @Override
                    public void GarbageFound_Ok() {
                    }
                });
                break;
            }

            case PostGarbageSyntheses: {
                if (data == null || !(data instanceof GomipoiGarbageSynthesesParam)) {
                    showErrorDialog();
                    return;
                }

                final GomipoiGarbageSynthesesParam synthesesParam = (GomipoiGarbageSynthesesParam) data;
                getConnectionManager().garbageSyntheses(synthesesParam, new ConnectionManager.OnGarbageSynthesesListener() {
                    @Override
                    public void GarbageSyntheses_Ok() {
                    }

                    @Override
                    public void GarbageSyntheses_NotHaveReceipe() {
                        onShowDialog(DialogCode.SynthesisNotHaveReceipe.getValue(), null);
                    }

                    @Override
                    public void GarbageSyntheses_Failed() {
                        onShowDialog(DialogCode.FailedSynthesis.getValue(), null);
                    }

                    @Override
                    public void GarbageSyntheses_AlreadySucceed() {
                        onShowDialog(DialogCode.SynthesisAlreadyGot.getValue(), null);
                    }
                });
                break;
            }

            case PostItemUse: {
                if (data == null || !(data instanceof GomipoiItemUseParam)) {
                    showErrorDialog();
                    return;
                }

                final GomipoiItemUseParam useParam = (GomipoiItemUseParam) data;

                getConnectionManager().useItem(useParam, new ConnectionManager.OnItemUseListener() {
                    @Override
                    public void ItemUse_Ok() {
                        if (useParam.itemData.id.equals(ItemId.AUTO_BRROM)) {
                            unlockEvent();
                            return;
                        }

                        DialogBase itemDialog = getCurrentActiveDialog();
                        if (itemDialog != null && itemDialog instanceof ItemDialog) {
                            closeDialog(itemDialog);
                        }

                        if (useParam.itemData.id.equals(ItemId.RoomSecret1)
                                ||useParam.itemData.id.equals(ItemId.RoomSecret2)
                                ||useParam.itemData.id.equals(ItemId.RoomSecret3)
                                ||useParam.itemData.id.equals(ItemId.RoomSecret4)
                                ||useParam.itemData.id.equals(ItemId.RoomSecret5)
                                ||useParam.itemData.id.equals(ItemId.RoomSecret6)) {
                            JniBridge.nativeAddGem(1);

                            Fragment fragment = getCurrentActiveFragment();
                            if (fragment != null && fragment instanceof GameFragment) {
                                ((GameFragment) fragment).onChangedGem(JniBridge.nativeGetGem());
                            }

                            HashMap<String, Object> map = new HashMap<>();
                            map.put("addPoint", 1);
                            map.put("playSound", true);
                            onShowDialog(DialogCode.PicturePoiResultGet.getValue(), map);
                        }
                        unlockEvent();
                    }

                    @Override
                    public void ItemUse_NotHave() {
                        onShowDialog(DialogCode.ItemShortage.getValue(), useParam);
                    }

                    @Override
                    public void ItemUse_AlreadyEmptyCan() {
                        onShowDialog(DialogCode.ItemUseAlreadyEmptyCan.getValue(), null);
                    }

                    @Override
                    public void ItemUse_AlreadyUsed() {
                        onShowDialog(DialogCode.ItemUseAlreadyUsed.getValue(), null);
                    }

                    @Override
                    public void ItemUse_Disabled() {
                        onShowDialog(DialogCode.ItemUseDisabled.getValue(), null);
                    }

                    @Override
                    public void ItemUse_NotChangeCan() {
                        onShowDialog(DialogCode.ItemUseNotChangeCan.getValue(), null);
                    }

                    @Override
                    public void ItemUse_DailyCountReached() {
                        onShowDialog(DialogCode.ItemUseDailyCountReached.getValue(), null);
                    }
                });
                break;
            }

            case PostItemSetBuy: {
                DebugLog.i("GBActivityBase - onEvent PostItemSetBuy");
                if (data == null || !(data instanceof GomipoiItemSetBuyParam)) {
                    showErrorDialog();
                    return;
                }

                final GomipoiItemSetBuyParam itemSetParam = (GomipoiItemSetBuyParam) data;
                getConnectionManager().buyItem(itemSetParam, new ConnectionManager.OnItemSetBuyListener() {
                    @Override
                    public void ItemSetBuy_Ok(int price) {
                        JniBridge.nativeAddGem(-price);

                        if (itemSetParam.itemData.isBuyAndUseItem()) {
                            ListItemData item = itemSetParam.itemData;
                            BroomType broomType = item.getBroomType();
                            GarbageCanType garbageCanType = item.getGarbageCanType();

                            if (!broomType.equals(BroomType.UNKNOWN)) {
                                JniBridge.nativeChangeBroomType(broomType.getValue());
                            }
                            else if (!garbageCanType.equals(GarbageCanType.UNKNOWN)) {
                                StageType stage = item.getGarbageCanStageType();
                                if (stage.getValue() == JniBridge.nativeGetCurrentStage()) {
                                    JniBridge.nativeChangeGarbageCanType(garbageCanType.getValue());
                                }
                            }
                            else if (item.itemCode.equals(ItemCode.AUTO_BRROM)) {
                                onEvent(FragmentEventCode.PostItemUse.getValue(), new GomipoiItemUseParam(itemSetParam.itemData));
                            }
                        }

                        DialogBase itemDialog = getCurrentActiveDialog();
                        if (itemDialog != null && itemDialog instanceof ItemDialog) {
                            ((ItemDialog) itemDialog).onChangedGem();
                            itemDialog.unlockEvent();
                        }

                        onShowDialog(DialogCode.BoughtItem.getValue(), null);
                    }

                    @Override
                    public void ItemSetBuy_GemShortage() {
                        onShowDialog(DialogCode.GemShortage.getValue(), null);
                    }

                    @Override
                    public void ItemSetBuy_AchievedMaxPossession() {
                        onShowDialog(DialogCode.ItemSetBuyAchievedMaxPossession.getValue(), null);
                    }

                    @Override
                    public void ItemSetBuy_NoKey() {
                        onShowDialog(DialogCode.ItemSetBuyNoKey.getValue(), null);
                    }
                });
                break;
            }

            case PostBookCheck: {
                if (data == null || !(data instanceof GomipoiBookCheckParam)) {
                    showErrorDialog();
                    return;
                }

                final GomipoiBookCheckParam bookCheckParam = (GomipoiBookCheckParam)data;
                getConnectionManager().bookCheck(
                        bookCheckParam,
                        new ConnectionManager.OnBookCheckListener() {
                    @Override
                    public void BookCheck_Ok() {

                        DialogBase parentDialog = getCurrentActiveDialog();
                        if (parentDialog != null && parentDialog instanceof PictureBookDialog) {
                            ((PictureBookDialog)parentDialog).refresh();
                        }

                    }
                });
                break;
            }

            case PostReceiveBonusPages: {
                if (data == null || !(data instanceof GomipoiBookReceiveBonusesParam)) {
                    showErrorDialog();
                    return;
                }

                final GomipoiBookReceiveBonusesParam bookReceiveBonusesParam = (GomipoiBookReceiveBonusesParam)data;
                getConnectionManager().bookReceiveBonuses(
                        bookReceiveBonusesParam,
                        new ConnectionManager.OnBookReceiveBonusesListener() {
                    @Override
                    public void BookReceiveBonuses_Ok() {
                        onShowDialog(
                                DialogCode.PageCompleteBonus.getValue(),
                                bookReceiveBonusesParam.receive_bonus_pages);
                    }
                });
                break;
            }

            case GetNewsTopicCheck: {
                getConnectionManager().newsTopicCheck(new ConnectionManager.OnNewsTopicCheckListener() {
                    @Override
                    public void NewsTopicCheck_Ok(NewsTopicCheckResponse response) {
                        if (getApp() != null) {
                            getApp().getPlayerManager().setNewsTopicCheckResponse(response);
                        }
                    }
                });
                break;
            }

            case GetSystemCheck: {
                getConnectionManager().getSystemCheck(new ConnectionManager.OnGetSystemCheckListener(){
                    @Override
                    public void SystemCheck_Ok() {
                        // 通信可能なActivityはデータを取得する
                        if (isEnabledConnection()) {
                            if (getApp() != null && (getApp().getAccessToken() == null || getApp().getAccessToken().length() == 0)) {
                                if (getApp().getPreferenceManager().isNeedTutorial() || getApp().isLogouting()) {
                                    // 強制ログアウトの処理
                                    logout(true);
                                    logoutedFinish();
                                    return;
                                }

                                JniBridge.nativeOnStartGetData();

                                TopActivity.sShouldSendDeviceToken = true;
                                boolean isNew = InstanceIDListenerService.isNew(getApplicationContext());
                                onEvent(
                                        FragmentEventCode.PostUserSessions.getValue(),
                                        new UserSessionsParam(
                                                UserSessionsParam.ParentActivity.OTHER_ACTIVITY,
                                                getApp().getPreferenceManager().getUserId(),
                                                getApp().getPreferenceManager().getUserPassword(),
                                                isNew ? InstanceIDListenerService.getDeviceToken(getApplicationContext()) : null));
                                return;
                            }
                            else {
                                if (TopActivity.sShouldSendDeviceToken) {
                                    String token = FirebaseInstanceId.getInstance().getToken();

                                    if (token != null) {
                                        onEvent(FragmentEventCode.RegisterDeviceToken.getValue(), new RegisterDeviceTokenParam(token));
                                    }
                                }
                            }

                            if (!isEnabledConnection()) {
                                onShowDialog(DialogCode.GetNetworkErrorWithRetry.getValue(), null);
                                return;
                            }

                            JniBridge.nativeOnStartGetData();
                            onEvent(FragmentEventCode.GetAllUserData.getValue(), null);
                        }
                        else {
                            unlockEvent();
                        }
                    }

                    @Override
                    public void SystemCheck_ShowMaintenance(HashMap<String, Object> maintenanceMap) {
                        Intent intent = new Intent(dMaintenanceAction);
                        intent.putExtra(MaintainanceNotificationReceiver.KEY_INTENT_URL, (String) maintenanceMap.get("url"));
                        intent.putExtra(MaintainanceNotificationReceiver.KEY_INTENT_MAP, maintenanceMap);
                        sendBroadcast(intent);
                    }

                    @Override
                    public void SystemCheck_ShowVersionUp(String storeUrl) {
                        Intent intent = new Intent(dForcedUpdateAction);
                        intent.putExtra(ForcedUpdateNotificationReceiver.KEY_INTENT_URL, storeUrl);
                        sendBroadcast(intent);
                    }
                });

                break;
            }

            case ShowCaptureWaitingDialog: {
                showCaptureWaitingDialog();
                break;
            }

            case CloseCaptureWaitingDialog: {
                closeCaptureWaitingDialog();
                break;
            }

            // API: users/premium_ticket
            case PostPremiumTicket: {
                getConnectionManager().postPremiumTicket(new ConnectionManager.OnPremiumTicketListener() {
                    @Override
                    public void PremiumTicket_Ok() {
                        Fragment fragment = getCurrentActiveFragment();
                        if (fragment instanceof ExchangeFragment) {
                            ((ExchangeFragment) fragment).onBuyTicketSuccess();
                        }
                    }

                    @Override
                    public void PremiumTicket_Failed() {
                        Fragment fragment = getCurrentActiveFragment();
                        if (fragment instanceof ExchangeFragment) {
                            ((ExchangeFragment) fragment).onBuyTicketFailed();
                        }
                    }
                });

                break;
            }

            case GetFriendList: {
                getConnectionManager().getFriendList(new ConnectionManager.OnFriendListListener() {
                    @Override
                    public void FriendList_AllOk() {
                        unlockEvent();
                    }

                    @Override
                    public void FriendList_OK_Friends(int friendCount, int friendMaxCount, List<GomipoiFriendsResponse> listData) {
                        Fragment fragment = getCurrentActiveFragment();
                        if (fragment instanceof FriendListFragment) {
                            ((FriendListFragment) fragment).onReceivedFriendListData(friendCount, friendMaxCount, listData);
                        }
                    }

                    @Override
                    public void FriendList_OK_FriendMessageBadge(int count) {
                        Fragment fragment = getCurrentActiveFragment();
                        if (fragment instanceof FriendListFragment) {
                            ((FriendListFragment) fragment).onReceivedMessageBadge(count, false);
                        }
                    }

                    @Override
                    public void FriendList_OK_SystemMessageBadge(int count) {
                        Fragment fragment = getCurrentActiveFragment();
                        if (fragment instanceof FriendListFragment) {
                            ((FriendListFragment) fragment).onReceivedMessageBadge(count, true);
                        }
                    }
                });
                break;
            }

            // API: /friends/id=?
            case DeleteFriend: {
                DebugLog.i("GBActivityBase - DeleteFriend case...");
                GomipoiFriendsResponse params = (GomipoiFriendsResponse) data;
                getConnectionManager().deleteFriend(() -> {
                    DebugLog.i("GBActivityBase - DeleteFriend Success...");
                    Fragment fragment = getCurrentActiveFragment();
                    if (fragment instanceof FriendListFragment) {
                        ((FriendListFragment) fragment).onDeleteFriendCallback(params.nickname);
                    }
                }, params.userId);

                break;
            }

            // API: /week_rankings/current
            // API: /week_rankings/last
            case GetFriendListRank: {
                getConnectionManager().getFriendListRank(new ConnectionManager.OnFriendListRankListener() {
                    @Override
                    public void FriendListRank_AllOk() {
                        // Do nothing.
                    }

                    @Override
                    public void FriendListRank_OK(String executedAt, int selfId, int selfRank, int selfPoint, List<GomipoiFriendsRankResponse> listData) {
                        Fragment fragment = getCurrentActiveFragment();
                        if (fragment instanceof FriendListRankFragment) {
                            ((FriendListRankFragment) fragment).onReceivedFriendListRankData(executedAt, selfId, selfRank, selfPoint, listData);
                        }
                    }
                });
                break;
            }

            case GetFriendSearch: {
                if (data == null || !(data instanceof FriendsSearchByCodeParam)) {
                    showErrorDialog();
                    return;
                }

                getConnectionManager().friendSearch((FriendsSearchByCodeParam)data, new ConnectionManager.OnFriendsSearchByCodeListener() {
                    @Override
                    public void FriendsSearchByCode_Ok(FriendsSearchByCodeResponse response) {
                        DialogBase dialog = getCurrentActiveDialog();
                        if (dialog != null && dialog instanceof SearchFriendDialog) {
                            ((SearchFriendDialog) dialog).onReceivedSearchResult(response);
                            dialog.unlockEvent();
                        }
                        unlockEvent();
                    }

                    @Override
                    public void FriendsSearchByCode_InvalidCode() {
                        DialogBase dialog = getCurrentActiveDialog();
                        if (dialog != null && dialog instanceof SearchFriendDialog) {
                            ((SearchFriendDialog) dialog).onReceivedSearchResult(null);
                            dialog.unlockEvent();
                        }
                        unlockEvent();
//                        onShowDialog(
//                                DialogCode.SearchByCode_Invalid.getValue(),
//                                null);
                    }
                });
                break;
            }

            case RequestAddFriend: {
                if (data == null || !(data instanceof FriendCodeUseParam)) {
                    showErrorDialog();
                    return;
                }

                getConnectionManager().friendCodeUse((FriendCodeUseParam) data, new ConnectionManager.OnFriendCodeUseListener() {
                    @Override
                    public void FriendCodeUse_Ok() {
                        DialogBase dialog = getCurrentActiveDialog();
                        if (dialog != null && dialog instanceof SearchFriendDialog) {
                            closeDialog(dialog);
                        }
                        onEvent(
                                FragmentEventCode.GetFriendList.getValue(),
                                null);
                    }

                    @Override
                    public void FriendCodeUse_NotMatchUser() {
                        onShowDialog(
                                DialogCode.CodeUse_NotMatch.getValue(),
                                null);
                    }

                    @Override
                    public void FriendCodeUse_IsSelf() {
                        onShowDialog(
                                DialogCode.CodeUse_IsSelf.getValue(),
                                null);
                    }

                    @Override
                    public void FriendCodeUse_AlreadyFriend() {
                        onShowDialog(
                                DialogCode.CodeUse_AlreadyFriend.getValue(),
                                null);
                    }

                    @Override
                    public void FriendCodeUse_ReachedUpperLimitSelf() {
                        onShowDialog(
                                DialogCode.CodeUse_ReachedUpperLimitSelf.getValue(),
                                null);
                    }

                    @Override
                    public void FriendCodeUse_ReachedUpperLimitFriend() {
                        onShowDialog(
                                DialogCode.CodeUse_ReachedUpperLimitFriend.getValue(),
                                null);
                    }
                });
                break;
            }

            case GetFriendMessage: {
                getConnectionManager().getFriendMessageList(new ConnectionManager.OnFriendMessagesListener() {
                    @Override
                    public void FriendMessages_Ok(String serverDate, List<MessagesResponse> listData) {
                        DialogBase dialog = getCurrentActiveDialog();
                        if (dialog instanceof FriendMessageDialog) {
                            ((FriendMessageDialog) dialog).onReceivedFriendMessageData(serverDate, listData);
                        }
                    }

                    @Override
                    public void SystemMessages_Ok(String serverDate, List<MessagesResponse> listData) {
                        DialogBase dialog = getCurrentActiveDialog();
                        if (dialog instanceof FriendMessageDialog) {
                            ((FriendMessageDialog) dialog).onReceivedFriendMessageData(serverDate, listData);
                        }
                    }

                    @Override
                    public void MessagesAll_Ok() {
                        DialogBase dialog = getCurrentActiveDialog();
                        dialog.unlockEvent();
                        unlockEvent();
                    }
                });
                break;
            }

            case Send_Present_FriendList: {
                if (data == null || !(data instanceof FriendPresentsParam)) {
                    showErrorDialog();
                    return;
                }

                getConnectionManager().sendPresents_FriendList((FriendPresentsParam) data, new ConnectionManager.OnFriendPresentsListener() {
                    @Override
                    public void FriendPresents_Ok() {
                        DialogBase dialog = getCurrentActiveDialog();
                        if (dialog != null && dialog instanceof MessageDialog) {
                            closeDialog(dialog);
                        }
                        onEvent(
                                FragmentEventCode.GetFriendList.getValue(),
                                null);
                    }

                    @Override
                    public void FriendPresents_ExceedUpperLimitSelf() {
                        onShowDialog(
                                DialogCode.Present_ExceedUpperLimitSelf.getValue(),
                                null);
                    }

                    @Override
                    public void FriendPresents_ExceedUpperLimitFriend() {
                        onShowDialog(
                                DialogCode.Present_ExceedUpperLimitFriend.getValue(),
                                null);
                    }
                });
                break;
            }

            case Send_Present_MessageList: {
                if (data == null || !(data instanceof FriendPresentsParam)) {
                    showErrorDialog();
                    return;
                }

                getConnectionManager().sendPresents_MessageList((FriendPresentsParam) data, new ConnectionManager.OnFriendPresentsListener() {
                    @Override
                    public void FriendPresents_Ok() {
                        onEvent(
                                FragmentEventCode.GetFriendMessage.getValue(),
                                null);
                    }

                    @Override
                    public void FriendPresents_ExceedUpperLimitSelf() {
                            onShowDialog(
                                    DialogCode.Presents_ExceedUpperLimitSelf.getValue(),
                                    null);
                    }

                    @Override
                    public void FriendPresents_ExceedUpperLimitFriend() {
                        onShowDialog(
                                DialogCode.Presents_ExceedUpperLimitFriend.getValue(),
                                null);
                    }
                });
                break;
            }

            case Receive_FriendshipBonus: {
                if (data == null || !(data instanceof FriendshipBonusParam)) {
                    showErrorDialog();
                    return;
                }

                getConnectionManager().sendFriendshipBonus((FriendshipBonusParam) data, new ConnectionManager.OnFriendshipBonusListener() {
                    @Override
                    public void FriendshipBonus_Ok() {
                        DialogBase dialog = getCurrentActiveDialog();
                        dialog.unlockEvent();
                        unlockEvent();

                        onEvent(
                                FragmentEventCode.GetFriendMessage.getValue(),
                                null);
                    }

                    @Override
                    public void FriendshipBonus_Expiration() {
                        DialogBase dialog = getCurrentActiveDialog();
                        dialog.unlockEvent();
                        unlockEvent();

                        onShowDialog(
                                DialogCode.PresentsUse_Expiration.getValue(),
                                null);
                    }
                });

                break;
            }

            case Receive_Present: {
                if (data == null || !(data instanceof FriendPresentsUseParam)) {
                    showErrorDialog();
                    return;
                }

                getConnectionManager().sendPresents((FriendPresentsUseParam) data, new ConnectionManager.OnFriendPresentsUseListener() {
                    @Override
                    public void FriendPresentsUse_Ok() {
                        onEvent(
                                FragmentEventCode.GetFriendMessage.getValue(),
                                null);
                    }

                    @Override
                    public void FriendPresentsUse_Expiration() {
                        onShowDialog(
                                DialogCode.PresentsUse_Expiration.getValue(),
                                null);
                    }

                    @Override
                    public void FriendPresentsUse_NoEffect() {
                        onShowDialog(
                                DialogCode.PresentsUse_NoEffect.getValue(),
                                null);
                    }
                });

                break;
            }

            case GetGimmeList: {
                getConnectionManager().getPresentRequestList(new ConnectionManager.OnFriendsForPresentRequestListener() {
                    @Override
                    public void FriendsForPresentRequest_Ok(List<FriendsForPresentRequestResponse> listData) {
                        DialogBase dialog = getCurrentActiveDialog();
                        if (dialog instanceof IconCanSelectDialog) {
                            ((IconCanSelectDialog) dialog).onReceivedListData(listData);
                            dialog.unlockEvent();
                        }

                        closeConnectionDialog();
                        unlockEvent();
                    }
                });
                break;
            }

            case RequestGimme_FriendList: {
                if (data == null || !(data instanceof FriendPresentRequestsParam)) {
                    showErrorDialog();
                    return;
                }

                getConnectionManager().requestPresent_FriendList((FriendPresentRequestsParam) data, new ConnectionManager.OnFriendPresentRequestsListener() {
                    @Override
                    public void FriendPresentRequests_Ok() {
                        DialogBase dialog = getCurrentActiveDialog();
                        if (dialog != null && dialog instanceof MessageDialog) {
                            closeDialog(dialog);
                        }
                        onEvent(
                                FragmentEventCode.GetFriendList.getValue(),
                                null);
                    }

                    @Override
                    public void FriendPresentRequests_ExceedUpperLimit() {
                        onShowDialog(
                                DialogCode.PresentRequests_ExceedUpperLimit.getValue(),
                                null);
                    }
                });

                break;
            }

            case RequestGimme_GimmeList: {
                if (data == null || !(data instanceof FriendPresentRequestsParam)) {
                    showErrorDialog();
                    return;
                }

                getConnectionManager().requestPresent_GimmeList((FriendPresentRequestsParam) data, new ConnectionManager.OnFriendPresentRequestsListener() {
                    @Override
                    public void FriendPresentRequests_Ok() {
                        onEvent(
                                FragmentEventCode.GetGimmeList.getValue(),
                                null);
                    }

                    @Override
                    public void FriendPresentRequests_ExceedUpperLimit() {
                        onShowDialog(
                                DialogCode.PresentRequest_ExceedUpperLimit.getValue(),
                                null);
                    }
                });

                break;
            }

            case PostFriendInvitations: {
                if (data == null || !(data instanceof FriendInvitationsParam)) {
                    showErrorDialog();
                    return;
                }

                getConnectionManager().invitation((FriendInvitationsParam) data, new ConnectionManager.OnFriendInvitationsListener() {
                    @Override
                    public void FriendInvitations_Ok() {
                        DialogBase dialog = getCurrentActiveDialog();
                        if (dialog != null && dialog instanceof MessageDialog) {
                            closeDialog(dialog);
                        }
                        onEvent(
                                FragmentEventCode.GetFriendList.getValue(),
                                null);
                    }
                });
                break;
            }

            case Game_ChangeRoom: {
                if (data == null || !(data instanceof GomipoiGameMovePlaceParam)) {
                    showErrorDialog();
                    return;
                }

                getConnectionManager().changePlace((GomipoiGameMovePlaceParam) data, new ConnectionManager.OnGameMovePlaceListener() {
                    @Override
                    public void GameMovePlace_Ok() {
                        getConnectionManager().reloadGame(new ConnectionManager.OnAllGetListener() {
                            @Override
                            public void AllGet_Ok() {
                                DialogBase itemDialog = getCurrentActiveDialog();
                                if (itemDialog != null) {
                                    itemDialog.unlockEvent();
                                    closeDialog(itemDialog);
                                }

                                unlockEvent();
                            }
                        });
                    }

                    @Override
                    public void GameMovePlace_AlreadyInPlace() {
                        DialogBase dialog = getCurrentActiveDialog();
                        if (dialog != null) {
                            closeDialog(dialog);
                        }
                        unlockEvent();

                        MessageDialog errorDialog = MessageDialog.newInstance(
                                DialogCode.ERROR,
                                getSupportFragmentManager(),
                                getString(R.string.dialog_name_error),
                                MessageDialogType.ONE_BUTTON,
                                true);
                        errorDialog.setMessage("すでに選択された部屋にいます。");
                        errorDialog.setLeftButtonText(R.drawable.button_back);
                        showDialog(errorDialog);
                    }

                    @Override
                    public void GameMovePlace_NoKeyRoom() {
                        DialogBase dialog = getCurrentActiveDialog();
                        if (dialog != null) {
                            closeDialog(dialog);
                        }
                        unlockEvent();

                        MessageDialog errorDialog = MessageDialog.newInstance(
                                DialogCode.ERROR,
                                getSupportFragmentManager(),
                                getString(R.string.dialog_name_error),
                                MessageDialogType.ONE_BUTTON,
                                true);
                        errorDialog.setMessage("部屋のカギを所持していません。アイテムリストより入手してください。");
                        errorDialog.setLeftButtonText(R.drawable.button_back);
                        showDialog(errorDialog);
                    }
                });
                break;
            }

            case Receive_JirokichiBonus: {
                if (data == null || !(data instanceof GomipoiJirokichiBonusesParams)) {
                    showErrorDialog();
                    return;
                }

                final GomipoiJirokichiBonusesParams bonusesParam = (GomipoiJirokichiBonusesParams) data;
                getConnectionManager().gameJirokichiBonus(bonusesParam, new ConnectionManager.OnJirokichiBonusesListener() {
                    @Override
                    public void JirokichiBonuses_Ok() {
                        Fragment fragment = getCurrentActiveFragment();
                        if (fragment != null && fragment instanceof GameFragment) {
                            ((GameFragment) fragment).onSucceedReceiveJirokichiBonus(bonusesParam.getGem());
                        }
                    }

                    @Override
                    public void JirokichiBonuses_NoBonus() {
                        if (!DebugMode.isTestJirokichi) {
                            MessageDialog errorDialog = MessageDialog.newInstance(
                                    DialogCode.ERROR,
                                    getSupportFragmentManager(),
                                    getString(R.string.dialog_name_error),
                                    MessageDialogType.ONE_BUTTON,
                                    true);
                            errorDialog.setMessage("受け取り可能なジロキチボーナスがありません。");
                            errorDialog.setLeftButtonText(R.drawable.button_back);
                            showDialog(errorDialog);
                        }
                    }
                });
                break;
            }

            case RegisterDeviceToken: {
                if (data == null || !(data instanceof RegisterDeviceTokenParam)) {
                    showErrorDialog();
                    return;
                }

                final RegisterDeviceTokenParam param = (RegisterDeviceTokenParam) data;
                getConnectionManager().registerDeviceToken(param, new ConnectionManager.OnRegisterDeviceTokenListener() {
                    @Override
                    public void RegisterDeviceToken_Ok() {
                        TopActivity.sShouldSendDeviceToken = false;
                    }
                });
                break;
            }

            case GetNotificationCheck: {
//                getConnectionManager().checkNotification(new ConnectionManager.OnCheckedNotificationListner() {
//                    @Override
//                    public void CheckNotification_Ok(boolean unread) {
//                        // 未読通知がある場合は、NEWを表示
//                        Fragment fragment = getCurrentActiveFragment();
//                        if (fragment != null && fragment instanceof TopFragment) {
//                            ((TopFragment)fragment).setInfoNewVisible(unread);
//                        }
//                    }
//                });

                getConnectionManager().checkTopNotifications(new ConnectionManager.OnCheckedTopNotificationsListner() {
                    @Override
                    public void CheckTopNotifications_Ok(boolean notification_unread, boolean friend_unread) {
                        // 未読通知がある場合は、NEWを表示
                        Fragment fragment = getCurrentActiveFragment();
                        if (fragment != null && fragment instanceof TopFragment) {
                            ((TopFragment)fragment).setInfoNewVisible(notification_unread, friend_unread);
                        }
                    }
                });
                break;
            }

            case PostUserGuestRegister: {
                getConnectionManager().userGuestRegist(new ConnectionManager.OnUserGuestRegisteredListner() {
                    @Override
                    public void UserGuestRegister_Ok(UserGuestRegisterResponce responce) {
                        InstanceIDListenerService notificationService = InstanceIDListenerService.getInstance();
                        boolean isNew = notificationService == null ? false : notificationService.isNew(getApplicationContext());
                        onEvent(
                                FragmentEventCode.PostUserSessions.getValue(),
                                new UserSessionsParam(
                                        UserSessionsParam.ParentActivity.LOGIN_ACTIVITY,
                                        responce.account,
                                        responce.password,
                                        isNew ? notificationService.getDeviceToken(getApplicationContext()) : null));
                    }
                });

                break;
            }

            case PostUserDefinitiveRegister: {
                if (data == null || !(data instanceof UserDefinitiveRegisterParam)) {
                    showErrorDialog();
                    return;
                }

                // ParentActivity設定
                final UserDefinitiveRegisterParam param = (UserDefinitiveRegisterParam)data;
                UserSessionsParam.ParentActivity parentActivity = UserSessionsParam.ParentActivity.OTHER_ACTIVITY;
                if (GBActivityBase.this instanceof TopActivity) {
                    parentActivity = UserSessionsParam.ParentActivity.TOP_ACTIVITY_GUEST_REGISTER;
                } else if (GBActivityBase.this instanceof SettingsActivity) {
                    parentActivity = UserSessionsParam.ParentActivity.SETTING_ACTIVITY_GUEST_REGISTER;
                } else if (GBActivityBase.this instanceof ExchangeActivity) {
                    parentActivity = UserSessionsParam.ParentActivity.EXCHANGE_ACTIVITY_GUEST_REGISTER;
                }
                param.parentActivity = parentActivity;

                getConnectionManager().userDefinitiveRegist(param, new ConnectionManager.OnUserDefinitiveRegisterdListener() {
                    @Override
                    public void UserDefinitiveRegist_Ok_Setting() {
                        GBActivityBase.this.finish();
                    }

                    @Override
                    public void UserDefinitiveRegist_Ok_Top() {
                        onBackPressed();
                    }

                    @Override
                    public void UserDefinitiveRegist_Ok_Exchange() {
                        GBActivityBase.this.finish();
                    }

                    @Override
                    public void UserDefinitiveRegist_Ok_Other() {
                        onEvent(FragmentEventCode.GetNewsTopicCheck.getValue(), null);
                        initLoading();
                    }

                    @Override
                    public void UserDefinitiveRegist_OnNotMatchUserError() {
                        onShowDialog(DialogCode.NotMatchUserInfo.getValue(), null);
                    }

                    @Override
                    public void UserDefinitiveRegist_OnShowExistSameAccountError() {
                        onShowDialog(DialogCode.ExistSameAccountError.getValue(), null);
                    }

                    @Override
                    public void UserDefinitiveRegist_OnShowExistSameNicknameError() {
                        onShowDialog(DialogCode.ExistSameNicknameError.getValue(), null);
                    }

                    @Override
                    public void UserDefinitiveRegist_OnShowInvalidPasswordError() {
                        onShowDialog(DialogCode.InvalidPasswordError.getValue(), null);
                    }

                });

                break;
            }

            case OpenConnectionDialog: {
                showConnectionDialog();
                break;
            }

            case CloseConnectionDialog: {
                DebugLog.i("GBActivityBase - CloseConnectionDialog");
                unlockEvent();
                closeConnectionDialog();
                break;
            }

            default: {
                super.onEvent(eventCode, data);
            }

        }
    }

    @Override
    public void onShowDialog(int dialogCode, Object data) {
        DialogCode code = DialogCode.valueOf(dialogCode);
        switch (code) {
            case ERROR: {
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        getString(R.string.dialog_name_error),
                        MessageDialogType.ONE_BUTTON,
                        true);
                dialog.setMessage("エラーが発生しました。もう一度操作を行ってください。");
                dialog.setLeftButtonText(R.drawable.button_back);
                showDialog(dialog);
                break;
            }

            case InputShortage: {
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        getString(R.string.dialog_name_input_shortage),
                        MessageDialogType.ONE_BUTTON,
                        true);
                dialog.setMessage("空白の項目があります");
                dialog.setLeftButtonText(R.drawable.button_back);
                showDialog(dialog);
                break;
            }

            case InputAccountOverTextLength: {
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        "InputAccountOverTextLengthDialog",
                        MessageDialogType.ONE_BUTTON,
                        true);
                dialog.setMessage("ニックネームは1〜20文字内で入力してください。");
                dialog.setLeftButtonText(R.drawable.button_back);
                showDialog(dialog);
                break;
            }

            case InputUserIdOverTextLength: {
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        "InputUserIdOverTextLengthDialog",
                        MessageDialogType.ONE_BUTTON,
                        true);
                dialog.setMessage("ログインIDは6〜20文字内で入力してください。");
                dialog.setLeftButtonText(R.drawable.button_back);
                showDialog(dialog);
                break;
            }

            case InputPasswordOverTextLength: {
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        "InputPasswordOverTextLengthDialog",
                        MessageDialogType.ONE_BUTTON,
                        true);
                dialog.setMessage("パスワードは6〜20文字内で入力してください。");
                dialog.setLeftButtonText(R.drawable.button_back);
                showDialog(dialog);
                break;
            }

            case InputSurrogate: {
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        "InputSurrogateDialog",
                        MessageDialogType.ONE_BUTTON,
                        true);
                dialog.setMessage("ニックネームに絵文字は使用できません。");
                dialog.setLeftButtonText(R.drawable.button_back);
                showDialog(dialog);
                break;
            }

            case News: {
                showDialog(NewsDialog.newInstance(getString(R.string.dialog_name_news)));
                break;
            }

            case Notice1: {
                DebugLog.i("GBActivityBase - onShowDialog Notice1");
                getApp().getPlayerManager().shownNotice1Dialog();
                showFloatingDialog(Notice1Dialog.newInstance(getString(R.string.dialog_name_notice1)));
                break;
            }

            case Notice2: {
                DebugLog.i("GBActivityBase - onShowDialog Notice2");
                getApp().getPlayerManager().shownNotice2Dialog();
                showFloatingDialog(Notice2Dialog.newInstance(getString(R.string.dialog_name_notice2)));
                break;
            }

            case Notice3: {
                DebugLog.i("GBActivityBase - onShowDialog Notice3");
                getApp().getPlayerManager().shownNotice3Dialog();
                showFloatingDialog(Notice3Dialog.newInstance(getString(R.string.dialog_name_notice3)));
                break;
            }

            case BuyGem: {
                if (data == null || !(data instanceof ShopItem)) {
                    onShowDialog(DialogCode.ERROR.getValue(), null);
                    return;
                }

                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        getString(R.string.dialog_name_buy_gem),
                        MessageDialogType.TWO_BUTTON,
                        (ShopItem)data,
                        true);
                dialog.setMessage("宝石" + ((ShopItem)data).getGemCountText() + "を購入しますか？");
                dialog.setLeftButtonText(R.drawable.button_yes);
                dialog.setRightButtonText(R.drawable.button_no);
                showDialog(dialog);
                break;
            }

            case PictureBook: {
                PictureBookDialog dialog = PictureBookDialog.newInstance(getString(R.string.dialog_name_picture_book));
                showDialog(dialog);
                break;
            }

            case PictureBookDetail: {
                if (data == null || !(data instanceof BookGarbageData)) {
                    onShowDialog(DialogCode.ERROR.getValue(), null);
                    return;
                }

                PictureBookDetailDialog dialog = PictureBookDetailDialog.newInstance("PictureBookDetailDialog", (BookGarbageData)data);
                showFloatingDialog(dialog);
                break;
            }

            case GameMenu: {
                if (data == null || !(data instanceof ArrayList)) {
                    unlockEvent();
                    return;
                }
                GameMenuDialog dialog = GameMenuDialog.newInstance((ArrayList<StageType>) data);
                showDialog(dialog);
                break;
            }

            case PageCompleteBonus: {
                if (data == null || !(data instanceof int[])) {
                    onShowDialog(DialogCode.ERROR.getValue(), null);
                    return;
                }

                int[] completedPages = (int[]) data;
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        "PageCompleteBonus",
                        MessageDialogType.ONE_BUTTON,
                        true);
                dialog.setMessage("ページコンプリート！\n" + completedPages.length + "ページ分のボーナスとして\n" + "宝石を" + (10 * completedPages.length) + "個プレゼントします。");
                dialog.setLeftButtonText(R.drawable.button_back);
                showFloatingDialog(dialog);
                break;
            }

            case Item: {
                ItemDialog dialog = ItemDialog.newInstance();
                showDialog(dialog);
                break;
            }

            case AlreadyBought: {
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        "AlreadyBoughtDialog",
                        MessageDialogType.ONE_BUTTON,
                        true);
                dialog.setMessage("すでに購入しています。");
                dialog.setLeftButtonText(R.drawable.button_back);
                showFloatingDialog(dialog);
                break;
            }

            case UsedSameDay: {
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        "UsedSameDayDialog",
                        MessageDialogType.ONE_BUTTON,
                        true);
                dialog.setMessage("1日1回制限のため使用できません。");
                dialog.setLeftButtonText(R.drawable.button_back);
                showFloatingDialog(dialog);
                break;
            }

            case GardenKeyMessage: {
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        "GardenKeyMessageDialog",
                        MessageDialogType.ONE_BUTTON,
                        true);
                dialog.setMessage("フレンドが10人になると解放されるよ！");
                dialog.setLeftButtonText(R.drawable.button_back);
                showFloatingDialog(dialog);
                break;
            }

            case ConfirmSeal: {
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        "ConfirmSealDialog",
                        MessageDialogType.TWO_BUTTON,
                        null,
                        true);
                dialog.setMessage("電動ほうき使用中はママが出ませんがよろしいですか？");
                dialog.setLeftButtonText(R.drawable.button_yes);
                dialog.setRightButtonText(R.drawable.button_no);
                showFloatingDialog(dialog);
                break;
            }

            case Synthesis: {
                SynthesisDialog dialog = SynthesisDialog.newInstance(getString(R.string.dialog_name_synthesis));
                showDialog(dialog);
                break;
            }

            case FailedSynthesis: {
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        "FailedSynthesisDialog",
                        MessageDialogType.ONE_BUTTON,
                        true);
                dialog.setMessage("合成に失敗しました。");
                dialog.setLeftButtonText(R.drawable.button_back);
                showFloatingDialog(dialog);
                break;
            }

            case SynthesisSuccess: {
                if (data instanceof BookGarbageData) {
                    SynthesisSuccessDialog dialog = SynthesisSuccessDialog.newInstance(getString(R.string.dialog_name_synthesis_success), (BookGarbageData)data);
                    showFloatingDialog(dialog);
                }
                break;
            }

            case PicturePoi: {
                PicturePoiDialog dialog = PicturePoiDialog.newInstance(getString(R.string.dialog_name_picture_poi));
                showDialog(dialog);
                break;
            }

            case PicturePoiResultGet: {
                int addPoint = 0;
                boolean playSound = false;
                if (data instanceof HashMap) {
                    HashMap<String, Object> map = (HashMap<String, Object>) data;
                    addPoint    = (int) map.get("addPoint");
                    playSound   = (boolean) map.get("playSound");
                }
                else {
                    addPoint = (int) data;
                }
                PicturePoiResultGetDialog dialog = PicturePoiResultGetDialog.newInstance(
                        "PicturePoiResultGetDialog",
                        addPoint,
                        playSound);
                showFloatingDialog(dialog);
                break;
            }

            case PicturePoiResult: {
                if (data == null || !(data instanceof PicturePoiDialog.PicturePoiDialogResponse)) {
                    onShowDialog(DialogCode.ERROR.getValue(), null);
                    return;
                }

                PicturePoiDialog.PicturePoiDialogResponse dialogResponse = (PicturePoiDialog.PicturePoiDialogResponse)data;
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        "PicturePoiResultDialog",
                        MessageDialogType.ONE_BUTTON,
                        dialogResponse,
                        true);
                dialog.setMessage(
                        "写真の削除処理を\n行いました。\n\n"
                                + "成功 : " + NumberUtils.getNumberFormatText(dialogResponse.successCount) + "件\n"
                                + "失敗 : " + NumberUtils.getNumberFormatText(dialogResponse.failedCount) + "件\n\n"
                                + "※ お使いの端末によっては、写真の削除に失敗することがあります。");
                dialog.setLeftButtonText(R.drawable.button_back);
                showFloatingDialog(dialog);
                break;
            }

            case PicturePoiResultNone: {
                if (getApp() == null) {
                    onShowDialog(DialogCode.ERROR.getValue(), null);
                    return;
                }

                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        "PicturePoiResultNoneDialog",
                        MessageDialogType.ONE_BUTTON,
                        true);
                dialog.setMessage("あと写真 " + (GEM_EXCHANGE_COUNT - getApp().getPreferenceManager().getPicturePoiCount()) + " 枚で宝石3個獲得！");
                dialog.setLeftButtonText(R.drawable.button_back);
                showFloatingDialog(dialog);
                break;
            }

            case Scroll: {
                mScrollDialog = ScrollDialog.newInstance(getString(R.string.dialog_name_scroll));
                showFloatingDialog(mScrollDialog);
                break;
            }

            case SynthesisShortage: {
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        getString(R.string.dialog_name_synthesis_shortage),
                        MessageDialogType.ONE_BUTTON,
                        true);
                dialog.setMessage("ゴミを3つ選択してください。");
                dialog.setLeftButtonText(R.drawable.button_back);
                showFloatingDialog(dialog);
                break;
            }

            case ItemShortage: {
                if (data == null || !(data instanceof GomipoiItemUseParam)) {
                    onShowDialog(DialogCode.ERROR.getValue(), null);
                    return;
                }

                GomipoiItemUseParam item = (GomipoiItemUseParam) data;
                MessageDialog dialog = MessageDialog.newInstance(
                        DialogCode.ItemShortage,
                        getSupportFragmentManager(),
                        "ItemShortageDialog",
                        MessageDialogType.ONE_BUTTON,
                        true);
                dialog.setMessage("「" + item.itemData.id.getName() + "」を持っていません。\n入手ページからアイテムを購入してください。");
//                dialog.setMessage("アイテムを持っていません。");
                dialog.setLeftButtonText(R.drawable.button_back);
                showFloatingDialog(dialog);
                break;
            }

            case ItemUse: {
                if (data == null || !(data instanceof ListItemData)) {
                    onShowDialog(DialogCode.ERROR.getValue(), null);
                    return;
                }

                ItemUseDialog dialog = ItemUseDialog.newInstance((ListItemData) data);
                showFloatingDialog(dialog);
                break;
            }

            case ItemBuy: {
                if (data == null || !(data instanceof ListItemData)) {
                    onShowDialog(DialogCode.ERROR.getValue(), null);
                    return;
                }

                ItemBuyDialog dialog = ItemBuyDialog.newInstance((ListItemData) data);
                showFloatingDialog(dialog);
                break;
            }

            case ItemBuyTicket: {
                ItemBuyTicketDialog dialog = ItemBuyTicketDialog.newInstance();
                showFloatingDialog(dialog);
                break;
            }

            case ItemBuyTicketSuccess: {
                ItemBuyTicketSuccessDialog dialog = ItemBuyTicketSuccessDialog.newInstance();
                showFloatingDialog(dialog);
                break;
            }

            case RoomSecretDetail: {
                if (data == null || !(data instanceof ListItemData)) {
                    onShowDialog(DialogCode.ERROR.getValue(), null);
                    return;
                }

                RoomSecretDialog dialog = RoomSecretDialog.newInstance((ListItemData) data);
                showFloatingDialog(dialog);
                break;
            }

            case ExistSameNicknameError:
            {
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        getString(R.string.dialog_name_exist_same_nickname_error),
                        MessageDialogType.ONE_BUTTON,
                        true);
                dialog.setMessage(getString(R.string.regist_message_error_exist_same_name));
                dialog.setLeftButtonText(R.drawable.button_back);
                showDialog(dialog);
                break;
            }

            case InvalidPasswordError:
            {
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        getString(R.string.dialog_name_invalid_password_error),
                        MessageDialogType.ONE_BUTTON,
                        true);
                dialog.setMessage(getString(R.string.regist_message_error_invalid_password));
                dialog.setLeftButtonText(R.drawable.button_back);
                showDialog(dialog);
                break;
            }

            case LoginGuestConfirm:
            {
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        getString(R.string.dialog_name_login_guest_confirm),
                        MessageDialogType.TWO_BUTTON,
                        true);
                dialog.setMessage(getString(R.string.login_guest_message_confirm));
                dialog.setLeftButtonText(R.drawable.button_yes);
                dialog.setRightButtonText(R.drawable.button_no);
                showDialog(dialog);

                break;
            }

            case GuestLimitedFuncFriend:
            {
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        getString(R.string.dialog_name_guest_limited_func_friend),
                        MessageDialogType.TWO_BUTTON,
                        true);
                dialog.setMessage(getString(R.string.guest_limited_func_friend_message));
                dialog.setLeftButtonText(R.drawable.button_yes);
                dialog.setRightButtonText(R.drawable.button_no);
                showDialog(dialog);

                break;
            }

            case GemShortage: {
                MessageDialog dialog = MessageDialog.newInstance(
                        DialogCode.GemShortage,
                        getSupportFragmentManager(),
                        "GemShortageDialog",
                        MessageDialogType.TWO_BUTTON,
                        true);
                dialog.setMessage("宝石が足りません。\n宝石を購入しますか？");
                dialog.setLeftButtonText(R.drawable.button_yes);
                dialog.setRightButtonText(R.drawable.button_no);
                showFloatingDialog(dialog);
                break;
            }

            case Connection: {
                mConnectionDialog = IndicatorDialog.newInstance();
                showFloatingDialog(mConnectionDialog);
                break;
            }

            case NetworkError: {
                if (mNetworkErrorkDialog != null) {
                    return;
                }

                mNetworkErrorkDialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        "NetworkErrorDialog",
                        MessageDialogType.ONE_BUTTON,
                        true);
                mNetworkErrorkDialog.setMessage("通信エラーが発生しました。通信状態を確認してください。");
                mNetworkErrorkDialog.setLeftButtonText(R.drawable.button_back);
                showFloatingDialog(mNetworkErrorkDialog);
                break;
            }

            case GetNetworkErrorWithRetry: {
                if (mGetNetworkErrorWithRetryDialog != null) {
                    return;
                }

//                if (getApp() != null && getApp().getAccessToken() == null) {
//                    unlockEvent();
//                    return;
//                }

                mGetNetworkErrorWithRetryDialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        "GetNetworkErrorWithRetryDialog",
                        MessageDialogType.ONE_BUTTON,
                        data != null ? (ConnectionManager.RetryData)data : null,
                        false
                );
                mGetNetworkErrorWithRetryDialog.setMessage("通信エラーが発生しました。通信状態を確認してください。");
                mGetNetworkErrorWithRetryDialog.setLeftButtonText(R.drawable.button_back);
                closeConnectionDialog();
                showFloatingDialog(mGetNetworkErrorWithRetryDialog);
                break;
            }

            case ServiceNetworkErrorWithRetry: {
                if (mServiceNetworkErrorWithRetryDialog != null) {
                    return;
                }

                if (getApp() != null && getApp().getAccessToken() == null) {
                    if (mIsNeedGetRetry) {
                        mIsNeedGetRetry = false;
                        initLoading();
                    }
                    return;
                }

                mServiceNetworkErrorWithRetryDialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        "ServiceNetworkErrorWithRetryDialog",
                        MessageDialogType.ONE_BUTTON,
                        data != null ? (ConnectionManager.RetryData)data : null,
                        false
                );
                mServiceNetworkErrorWithRetryDialog.setMessage("通信エラーが発生しました。通信状態を確認してください。");
                mServiceNetworkErrorWithRetryDialog.setLeftButtonText(R.drawable.button_back);
                closeConnectionDialog();
                showFloatingDialog(mServiceNetworkErrorWithRetryDialog);
                break;
            }

            case NetworkErrorWithRetry: {
                if (mNetworkErrorWithRetryDialog != null) {
                    return;
                }

                if (data == null || !(data instanceof ConnectionManager.RetryData)) {
                    onShowDialog(DialogCode.ERROR.getValue(), null);
                    return;
                }

                mNetworkErrorWithRetryDialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        "NetworkErrorWithRetryDialog",
                        MessageDialogType.ONE_BUTTON,
                        (ConnectionManager.RetryData)data,
                        false
                );
                mNetworkErrorWithRetryDialog.setMessage("通信エラーが発生しました。通信状態を確認してください。");
                mNetworkErrorWithRetryDialog.setLeftButtonText(R.drawable.button_back);
                closeConnectionDialog();

                try {
                    showFloatingDialog(mNetworkErrorWithRetryDialog);
                } catch (IllegalStateException ignored) {
                }
                break;
            }

            case ExistSameAccountError: {
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        "ExistSameAccountErrorDialog",
                        MessageDialogType.ONE_BUTTON,
                        true);
                dialog.setMessage("ログインIDはすでに使用されています。");
                dialog.setLeftButtonText(R.drawable.button_back);
                showDialog(dialog);
                break;
            }

            case NotMatchUserInfo: {
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        "NotMatchUserInfoDialog",
                        MessageDialogType.ONE_BUTTON,
                        true);
                dialog.setMessage("ログインIDまたはパスワードが一致しませんでした。");
                dialog.setLeftButtonText(R.drawable.button_back);
                showDialog(dialog);
                break;
            }

            case LogoutConfirm: {
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        "LogoutConfirmDialog",
                        MessageDialogType.TWO_BUTTON,
                        true);
                dialog.setMessage("ログアウトしますか？");
                dialog.setLeftButtonText(R.drawable.button_yes);
                dialog.setRightButtonText(R.drawable.button_no);
                showDialog(dialog);
                break;
            }

            case UnauthorizedError: {
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        "UnauthorizedErrorDialog",
                        MessageDialogType.ONE_BUTTON,
                        false);
                dialog.setMessage("他の端末でログインされました。\n登録画面に戻ります。");
                dialog.setLeftButtonText(R.drawable.button_back);
                closeConnectionDialog();
                showDialog(dialog);
                break;
            }

            case BrokenBroom: {
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        "BrokenBroomDialog",
                        MessageDialogType.ONE_BUTTON,
                        true);
                dialog.setMessage("ほうきが壊れました。");
                dialog.setLeftButtonText(R.drawable.button_back);
                showDialog(dialog);
                break;
            }

            case BrokenGarbageCan: {
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        "BrokenGarbageCanDialog",
                        MessageDialogType.ONE_BUTTON,
                        true);
                dialog.setMessage("ゴミ箱XLが壊れました。");
                dialog.setLeftButtonText(R.drawable.button_back);
                showDialog(dialog);
                break;
            }

            case Unimplemented: {
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        "UnimplementedDialog",
                        MessageDialogType.ONE_BUTTON,
                        true);
                dialog.setMessage("近日公開");
                dialog.setLeftButtonText(R.drawable.button_back);
                showFloatingDialog(dialog);
                break;
            }

            case SynthesisNotHaveReceipe: {
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        "SynthesisNotHaveReceipeDialog",
                        MessageDialogType.ONE_BUTTON,
                        true);
                dialog.setMessage("レシピを持っていません。");
                dialog.setLeftButtonText(R.drawable.button_back);
                showFloatingDialog(dialog);
                break;
            }

            case SynthesisAlreadyGot: {
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        "SynthesisAlreadyGotDialog",
                        MessageDialogType.ONE_BUTTON,
                        true);
                dialog.setMessage("すでに合成に成功している組み合わせです。");
                dialog.setLeftButtonText(R.drawable.button_back);
                showFloatingDialog(dialog);
                break;
            }

            case GameSaveAlreadyMaxCapacity: {
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        "GameSaveAlreadyMaxCapacityDialog",
                        MessageDialogType.ONE_BUTTON,
                        true);
                dialog.setMessage("ゴミ箱がいっぱいです。");
                dialog.setLeftButtonText(R.drawable.button_back);
                showFloatingDialog(dialog);
                break;
            }

            case ItemUseAlreadyEmptyCan: {
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        "ItemUseAlreadyEmptyCanDialog",
                        MessageDialogType.ONE_BUTTON,
                        true);
                dialog.setMessage("ゴミ箱はすでに空です。");
                dialog.setLeftButtonText(R.drawable.button_back);
                showFloatingDialog(dialog);
                break;
            }

            case ItemUseAlreadyUsed: {
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        "ItemUseAlreadyUsedDialog",
                        MessageDialogType.ONE_BUTTON,
                        true);
                dialog.setMessage("すでに使用しています。");
                dialog.setLeftButtonText(R.drawable.button_back);
                showFloatingDialog(dialog);
                break;
            }

            case DonwgradeError: {
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        "DonwgradeErrorDialog",
                        MessageDialogType.ONE_BUTTON,
                        true);
                dialog.setMessage("すでに巨大なゴミ箱を使用しています。");
                dialog.setLeftButtonText(R.drawable.button_back);
                showFloatingDialog(dialog);
                break;
            }

            case BoughtItem: {
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        "BoughtItemDialog",
                        MessageDialogType.ONE_BUTTON,
                        true);
                dialog.setMessage("アイテムを購入しました。");
                dialog.setLeftButtonText(R.drawable.button_back);
                showFloatingDialog(dialog);
                break;
            }

            case NotHavePrevItem: {
                String name = "";
                if (data != null && data instanceof ItemId) {
                    switch ((ItemId)data) {
                        case DustboxBig:
                            name = "お茶の間のゴミ箱M";
                            break;
                        case DustboxBigPoiko:
                            name = "ポイ子の部屋のゴミ箱M";
                            break;
                        case GARBAGE_CAN_XL:
                            name = "お茶の間のゴミ箱L";
                            break;
                        case GARBAGE_CAN_XL_ROOM:
                            name = "ポイ子の部屋のゴミ箱L";
                            break;
                    }
                }

                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        "NotHavePrevItemDialog",
                        MessageDialogType.ONE_BUTTON,
                        true);
                dialog.setMessage("「" + name + "」を持っていないと購入できません。");
                dialog.setLeftButtonText(R.drawable.button_back);
                showFloatingDialog(dialog);
                break;
            }

            case NotHaveScroll: {
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        "NotHaveScrollDialog",
                        MessageDialogType.ONE_BUTTON,
                        true);
                dialog.setMessage("ゴミ合成には合成の秘伝書が必要です。\nアイテムから入手してください。");
                dialog.setLeftButtonText(R.drawable.button_back);
                showFloatingDialog(dialog);
                break;
            }

            case DeletePictureAlert: {
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        "DeletePictureAlertDialog",
                        MessageDialogType.TWO_BUTTON,
                        true);
                dialog.setMessage("本日のボーナスは受け取り済みです。\n宝石は取得できませんが、\n写真を削除しますか？");
                dialog.setLeftButtonText(R.drawable.button_yes);
                dialog.setRightButtonText(R.drawable.button_no);
                showFloatingDialog(dialog);
                break;
            }

            case ItemUseDisabled: {
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        "ItemUseDisabledDialog",
                        MessageDialogType.ONE_BUTTON,
                        true);
                dialog.setMessage("使用できないアイテムです。");
                dialog.setLeftButtonText(R.drawable.button_back);
                showFloatingDialog(dialog);
                break;
            }

            case ItemUseNotChangeCan: {
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        "ItemUseNotChangeCanDialog",
                        MessageDialogType.ONE_BUTTON,
                        true);
                dialog.setMessage("ゴミが溢れてしまうため、\nゴミ箱の変更はできません。");
                dialog.setLeftButtonText(R.drawable.button_back);
                showFloatingDialog(dialog);
                break;
            }

            case ItemUseDailyCountReached: {
                // 2017.01.06 連打した場合にここに来ることがあるので、アラートなどは表示しない
                break;
            }

            case ItemSetBuyAchievedMaxPossession: {
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        "ItemSetBuyAchievedMaxPossessionDialog",
                        MessageDialogType.ONE_BUTTON,
                        true);
                dialog.setMessage("すでにアイテムの所持数が上限に達しています。");
                dialog.setLeftButtonText(R.drawable.button_back);
                showFloatingDialog(dialog);
                break;
            }

            case ItemSetBuyNoKey: {
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        "ItemSetBuyNoKeyDialog",
                        MessageDialogType.ONE_BUTTON,
                        true);
                dialog.setMessage(getString(R.string.item_buy_no_key_error));
                dialog.setLeftButtonText(R.drawable.button_back);
                showFloatingDialog(dialog);
                break;
            }

            case PermissionError: {
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        "PermissionErrorDialog",
                        MessageDialogType.ONE_BUTTON,
                        true);
                dialog.setMessage("実行に必要な権限が\n許可されていません。\n\n権限の設定を\nご確認ください。");
                dialog.setLeftButtonText(R.drawable.button_back);
                showDialog(dialog);
                break;
            }

            case StorageError: {
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        "StorageErrorDialog",
                        MessageDialogType.ONE_BUTTON,
                        true);
                dialog.setMessage("ストレージで\nエラーが発生しました。");
                dialog.setLeftButtonText(R.drawable.button_back);
                showDialog(dialog);
                break;
            }

            case CaptureWaiting: {
                mCaptureWaitingDialog = CaptureWaitingDialog.newInstance();
                showFloatingDialog(mCaptureWaitingDialog);
                break;
            }

            case SearchFriend: {
                if (data instanceof String) {
                    showDialog(SearchFriendDialog.newInstance((String) data));
                } else {
                    showDialog(SearchFriendDialog.newInstance());
                }
                break;
            }

            case InviteFriend: {
                if (data == null && !(data instanceof GomipoiFriendsResponse)) {
                    onShowDialog(DialogCode.ERROR.getValue(), null);
                    return;
                }

                GomipoiFriendsResponse response = (GomipoiFriendsResponse)data;

                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        getString(R.string.dialog_name_invite_friend),
                        MessageDialogType.TWO_BUTTON,
                        response,
                        true);
                dialog.setMessage(response.getInviteDialogText());
                dialog.setLeftButtonText(R.drawable.button_yes);
                dialog.setRightButtonText(R.drawable.button_no);
                showDialog(dialog);
                break;
            }

            case DeleteFriend: {
                DebugLog.i("GBActivityBase - onShowDialog DeleteFriend");
                if (data == null && !(data instanceof GomipoiFriendsResponse)) {
                    onShowDialog(DialogCode.ERROR.getValue(), null);
                    return;
                }

                GomipoiFriendsResponse response = (GomipoiFriendsResponse)data;
                DebugLog.i("GBActivityBase - onShowDialog DeleteFriend response data: " + response);
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        getString(R.string.dialog_name_delete_friend),
                        MessageDialogType.TWO_BUTTON,
                        response,
                        true);

                dialog.setMessage(response.nickname + "さんをフレンドを解除しますか？");
                dialog.setLeftButtonText(R.drawable.button_yes);
                dialog.setRightButtonText(R.drawable.button_no);
                showDialog(dialog);
                break;
            }

            case DeleteFriendSuccess: {
                DebugLog.i("GBActivityBase - onShowDialog DeleteFriendSuccess");
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        getString(R.string.dialog_name_delete_friend),
                        MessageDialogType.ONE_BUTTON,
                        null,
                        true);
                dialog.setMessage(data.toString() + "さん\nを解除しました");
                dialog.setLeftButtonText(R.drawable.button_back);
                showDialog(dialog);
                break;
            }

            case GiveFriend: {
                if (data == null && !(data instanceof GomipoiFriendsResponse)) {
                    onShowDialog(DialogCode.ERROR.getValue(), null);
                    return;
                }

                GomipoiFriendsResponse response = (GomipoiFriendsResponse)data;

                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        getString(R.string.dialog_name_give_friend),
                        MessageDialogType.TWO_BUTTON,
                        response,
                        true);
                dialog.setMessage(response.getGiveDialogText());
                dialog.setLeftButtonText(R.drawable.button_yes);
                dialog.setRightButtonText(R.drawable.button_no);
                showDialog(dialog);
                break;
            }

            case GimmeFriend: {
                if (data == null && !(data instanceof GomipoiFriendsResponse)) {
                    onShowDialog(DialogCode.ERROR.getValue(), null);
                    return;
                }

                GomipoiFriendsResponse response = (GomipoiFriendsResponse)data;

                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        getString(R.string.dialog_name_gimme_friend),
                        MessageDialogType.TWO_BUTTON,
                        response,
                        true);
                dialog.setMessage(response.getGimmeDialogText());
                dialog.setLeftButtonText(R.drawable.button_yes);
                dialog.setRightButtonText(R.drawable.button_no);
                showDialog(dialog);
                break;
            }

            case FriendMessage: {
                showDialog(FriendMessageDialog.newInstance(getString(R.string.dialog_name_friend_message)));
                break;
            }

            case SearchByCode_Invalid: {
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        getString(R.string.dialog_name_searchbycode_invalid),
                        MessageDialogType.ONE_BUTTON,
                        true);
                dialog.setMessage(getString(R.string.dialog_message_searchbycode_invalid));
                dialog.setLeftButtonText(R.drawable.button_back);
                showFloatingDialog(dialog);
                break;
            }

            case CodeUse_NotMatch:{
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        getString(R.string.dialog_name_codeuse_notmatch),
                        MessageDialogType.ONE_BUTTON,
                        true);
                dialog.setMessage(getString(R.string.dialog_message_codeuse_notmatch));
                dialog.setLeftButtonText(R.drawable.button_back);
                showFloatingDialog(dialog);
                break;
            }

            case CodeUse_IsSelf:{
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        getString(R.string.dialog_name_codeuse_isself),
                        MessageDialogType.ONE_BUTTON,
                        true);
                dialog.setMessage(getString(R.string.dialog_message_codeuse_isself));
                dialog.setLeftButtonText(R.drawable.button_back);
                showFloatingDialog(dialog);
                break;
            }

            case CodeUse_AlreadyFriend:{
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        getString(R.string.dialog_name_codeuse_alreadyfriend),
                        MessageDialogType.ONE_BUTTON,
                        true);
                dialog.setMessage(getString(R.string.dialog_message_codeuse_alreadyfriend));
                dialog.setLeftButtonText(R.drawable.button_back);
                showFloatingDialog(dialog);
                break;
            }

            case CodeUse_ReachedUpperLimitSelf:{
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        getString(R.string.dialog_name_codeuse_reachedupperlimitself),
                        MessageDialogType.ONE_BUTTON,
                        true);
                dialog.setMessage(getString(R.string.dialog_message_codeuse_reachedupperlimitself));
                dialog.setLeftButtonText(R.drawable.button_back);
                showFloatingDialog(dialog);
                break;
            }

            case CodeUse_ReachedUpperLimitFriend:{
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        getString(R.string.dialog_name_codeuse_reachedupperlimitfriend),
                        MessageDialogType.ONE_BUTTON,
                        true);
                dialog.setMessage(getString(R.string.dialog_message_codeuse_reachedupperlimitfriend));
                dialog.setLeftButtonText(R.drawable.button_back);
                showFloatingDialog(dialog);
                break;
            }

            case Present_ExceedUpperLimitSelf:{
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        getString(R.string.dialog_name_present_exceedupperlimitself),
                        MessageDialogType.ONE_BUTTON,
                        true);
                dialog.setMessage(getString(R.string.dialog_message_present_exceedupperlimitself));
                dialog.setLeftButtonText(R.drawable.button_back);
                showFloatingDialog(dialog);
                break;
            }

            case Present_ExceedUpperLimitFriend:{
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        getString(R.string.dialog_name_present_exceedupperlimitfriend),
                        MessageDialogType.ONE_BUTTON,
                        true);
                dialog.setMessage(getString(R.string.dialog_message_present_exceedupperlimitfriend));
                dialog.setLeftButtonText(R.drawable.button_back);
                showFloatingDialog(dialog);
                break;
            }

            case Presents_ExceedUpperLimitSelf:{
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        getString(R.string.dialog_name_presents_exceedupperlimitself),
                        MessageDialogType.ONE_BUTTON,
                        true);
                dialog.setMessage(getString(R.string.dialog_message_presents_exceedupperlimitself));
                dialog.setLeftButtonText(R.drawable.button_back);
                showFloatingDialog(dialog);
                break;
            }

            case Presents_ExceedUpperLimitFriend:{
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        getString(R.string.dialog_name_presents_exceedupperlimitfriend),
                        MessageDialogType.ONE_BUTTON,
                        true);
                dialog.setMessage(getString(R.string.dialog_message_presents_exceedupperlimitfriend));
                dialog.setLeftButtonText(R.drawable.button_back);
                showFloatingDialog(dialog);
                break;
            }

            case PresentsUse_Expiration:{
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        getString(R.string.dialog_name_presentsuse_expiration),
                        MessageDialogType.ONE_BUTTON,
                        true);
                dialog.setMessage(getString(R.string.dialog_message_presentsuse_expiration));
                dialog.setLeftButtonText(R.drawable.button_back);
                showFloatingDialog(dialog);
                break;
            }

            case PresentsUse_NoEffect:{
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        getString(R.string.dialog_name_presentsuse_noeffect),
                        MessageDialogType.ONE_BUTTON,
                        true);
                dialog.setMessage(getString(R.string.dialog_message_presentsuse_noeffect));
                dialog.setLeftButtonText(R.drawable.button_back);
                showFloatingDialog(dialog);
                break;
            }

            case PresentRequest_ExceedUpperLimit:{
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        getString(R.string.dialog_name_presentrequest_exceedupperlimit),
                        MessageDialogType.ONE_BUTTON,
                        true);
                dialog.setMessage(getString(R.string.dialog_message_presentrequest_exceedupperlimit));
                dialog.setLeftButtonText(R.drawable.button_back);
                showFloatingDialog(dialog);
                break;
            }

            case PresentRequests_ExceedUpperLimit:{
                MessageDialog dialog = MessageDialog.newInstance(
                        code,
                        getSupportFragmentManager(),
                        getString(R.string.dialog_name_presentrequests_exceedupperlimit),
                        MessageDialogType.ONE_BUTTON,
                        true);
                dialog.setMessage(getString(R.string.dialog_message_presentrequests_exceedupperlimit));
                dialog.setLeftButtonText(R.drawable.button_back);
                showFloatingDialog(dialog);
                break;
            }

            case IconCan_Select: {
                showDialog(IconCanSelectDialog.newInstance("IconCanSelectDialog"));
                break;
            }

            default: {
                super.onShowDialog(dialogCode, data);
            }
        }
    }

    // ------------------------------
    // OnDialogLisntener
    // ------------------------------
    @Override
    public void onEvent(DialogBase dialog, int resultCode, Object data) {
        DialogCode code = DialogCode.valueOf(dialog.getDialogCode());
        switch (code) {
            case News: {
                closeDialog(dialog);
                playClickSe(resultCode == DialogBase.RESULT_OK);
                if (resultCode == DialogBase.RESULT_OK) {
                    onChangedActivity(ChangeActivityCode.News.getValue(), null);
                    return;
                }
                unlockEvent();
                break;
            }

            case Notice1: {
                DebugLog.i("GBActivityBase - onEvent Notice1 called");
                closeFloatingDialog(dialog);
                playClickSe(resultCode == DialogBase.RESULT_OK);

                if (!getApp().getPreferenceManager().getNotice2Hidden()
                        && getApp().getPlayerManager().isNeedShowNotice2Dialog()) {
                    DebugLog.i("GBActivityBase - onEvent Notice1 called Notice2 calling");
                    onShowDialog(DialogCode.Notice2.getValue(), null);
                } else if (!getApp().getPreferenceManager().getNotice3Hidden()
                        && getApp().getPlayerManager().isNeedShowNotice3Dialog()) {
                    // Show Notice3 Dialog instead when Notice2 Dialog is already checked.
                    DebugLog.i("GBActivityBase - onEvent Notice1 called Notice3 calling");
                    onShowDialog(DialogCode.Notice3.getValue(), null);
                } else {
                    unlockEvent();
                }
                break;
            }

            case Notice2: {
                DebugLog.i("GBActivityBase - onEvent Notice2 called");
                closeFloatingDialog(dialog);
                playClickSe(resultCode == DialogBase.RESULT_OK);

                if (!getApp().getPreferenceManager().getNotice3Hidden()
                        && getApp().getPlayerManager().isNeedShowNotice3Dialog()) {
                    DebugLog.i("GBActivityBase - onEvent Notice2 called Notice3 calling");
                    onShowDialog(DialogCode.Notice3.getValue(), null);
                } else {
                    unlockEvent();
                }
                break;
            }

            case Notice3: {
                DebugLog.i("GBActivityBase - onEvent Notice3 called");
                closeFloatingDialog(dialog);
                playClickSe(resultCode == DialogBase.RESULT_OK);
                unlockEvent();
                break;
            }

            case PictureBook: {
                if (resultCode == DialogBase.RESULT_OK) {
                    if (data != null && data instanceof PictureBookDialog.PictureBookDialogResponse) {
                        PictureBookDialog.PictureBookDialogResponse response = (PictureBookDialog.PictureBookDialogResponse)data;
                        if (response.action == PictureBookDialog.PictureBookDialogResponse.ACTION_GET_DATA) {
                            onEvent(
                                    FragmentEventCode.GetBookOwn.getValue(),
                                    new GomipoiBookOwnParam());
                        }
                        else {
                            playClickSe(true);
                            onShowDialog(DialogCode.PictureBookDetail.getValue(), response.data);
                        }
                        return;
                    }
                }
                playClickSe(false);
                closeDialog(dialog);
                unlockEvent();
                break;
            }

            case PictureBookDetail: {
                if (resultCode == DialogBase.RESULT_OK) {
                    if (data == null || !(data instanceof BookGarbageData)) {
                        DialogBase parentDialog = getCurrentActiveDialog();
                        if (parentDialog != null) {
                            parentDialog.unlockEvent();
                        }
                        return;
                    }
                    onEvent(
                            FragmentEventCode.PostBookCheck.getValue(),
                            new GomipoiBookCheckParam((BookGarbageData) data));
                    return;
                }
                playClickSe(false);
                closeFloatingDialog(dialog);
                DialogBase parentDialog = getCurrentActiveDialog();
                if (parentDialog != null) {
                    parentDialog.unlockEvent();
                }
                break;
            }

            case GameMenu: {
                if (resultCode == DialogBase.RESULT_OK) {
                    if (data != null && data instanceof Integer) {

                        int newRoom = (int) data;
                        StageType stage = StageType.UNKNOWN;

                        switch (newRoom) {
                            case GameMenuDialog.RESPONSE_TOP:
                                closeDialog(dialog);
                                unlockEvent();
                                finish();
                                break;

                            case GameMenuDialog.RESPONSE_MAIN_ROOM:
                                stage = StageType.DefaultRoom;
                                break;

                            case GameMenuDialog.RESPONSE_POIKO_ROOM:
                                stage = StageType.PoikoRoom;
                                break;

                            case GameMenuDialog.RESPONSE_GARDEN:
                                stage = StageType.Garden;
                                break;
                        }

                        if (!stage.equals(StageType.UNKNOWN)) {
                            StageType currentStage = StageType.valueOf(JniBridge.nativeGetCurrentStage());

                            if (!stage.equals(currentStage)) {
                                JniBridge.nativeSaveData(stage.getValue());
                            }
                            else {
                                closeDialog(dialog);
                                unlockEvent();
                            }
                        }
                    }

                    unlockEvent();
                }
                else {
                    unlockEvent();
                }
                break;
            }

            case PageCompleteBonus: {
                playClickSe(true);
                closeFloatingDialog(dialog);
                unlockEvent();
                break;
            }

            case PicturePoi: {
                playClickSe(resultCode == DialogBase.RESULT_OK);
                if (resultCode == DialogBase.RESULT_OK) {
                    if (data != null && data instanceof PicturePoiDialog.PicturePoiDialogResponse) {
                        PicturePoiDialog.PicturePoiDialogResponse dialogResponse = (PicturePoiDialog.PicturePoiDialogResponse)data;
                        if (dialogResponse.action == PicturePoiDialog.PicturePoiDialogResponse.ACTION_NOT_BONUS_CONFIRM) {
                            onShowDialog(DialogCode.DeletePictureAlert.getValue(), null);
                            return;
                        }

                        onShowDialog(DialogCode.PicturePoiResult.getValue(), data);
                        return;
                    }
                }
                closeDialog(dialog);
                unlockEvent();
                break;
            }

            case PicturePoiResult: {
                playClickSe(true);
                closeFloatingDialog(dialog);

                if (getApp() != null && data != null && data instanceof PicturePoiDialog.PicturePoiDialogResponse) {
                    if (((PicturePoiDialog.PicturePoiDialogResponse)data).action == PicturePoiDialog.PicturePoiDialogResponse.ACTION_NOT_BONUS_DELETE) {
                        unlockEvent();
                        return;
                    }


                    int currentPoiCount = getApp().getPreferenceManager().getPicturePoiCount();
                    int addValue = ((PicturePoiDialog.PicturePoiDialogResponse) data).successCount;
                    int result = currentPoiCount + addValue;
                    if (result >= GEM_EXCHANGE_COUNT) {
                        getApp().getPreferenceManager().setPicturePoiDate(JniBridge.nativeGetCurrentDate());
                        onEvent(
                                FragmentEventCode.PatchUserAppsJewel.getValue(),
                                new UserAppJewelParam(
                                        UserAppJewelParam.Method.PicturePoi,
                                        3,
                                        result));
                        return;
                    }

                    getApp().getPreferenceManager().setPicturePoiDate(JniBridge.nativeGetCurrentDate());
                    getApp().getPreferenceManager().setPicturePoiCount(result);
                    onShowDialog(DialogCode.PicturePoiResultNone.getValue(), null);
                    return;
                }

                break;
            }

            case PicturePoiResultNone:
            case PicturePoiResultGet: {
                playClickSe(false);
                closeFloatingDialog(dialog);
                DialogBase parentDialog = getCurrentActiveDialog();
                if (parentDialog != null && parentDialog instanceof PicturePoiDialog) {
                    closeDialog(parentDialog);
                }
                unlockEvent();
                break;
            }

            case Scroll: {
                if (mScrollDialog == null) {
                    return;
                }

                if (resultCode == DialogBase.RESULT_OK
                        && data instanceof Integer
                        && (int)data == ScrollDialog.ACTION_GET_DATA) {
                    onEvent(FragmentEventCode.GetGarbageRecipe.getValue(), null);
                    return;
                }

                playClickSe(false);
                closeFloatingDialog(dialog);
                mScrollDialog = null;
                DialogBase parentDialog = getCurrentActiveDialog();
                if (parentDialog != null) {
                    parentDialog.unlockEvent();
                }
                break;
            }

            case Synthesis: {
                if (resultCode == DialogBase.RESULT_OK) {
                    if (data != null && data instanceof SynthesisDialog.SynthesisDialogResponse) {
                        SynthesisDialog.SynthesisDialogResponse respons = (SynthesisDialog.SynthesisDialogResponse)data;
                        if (respons.action == SynthesisDialog.SynthesisDialogResponse.ACTION_SCROLL) {
                            playClickSe(true);
                            onShowDialog(DialogCode.Scroll.getValue(), null);
                            return;
                        }
                        if (respons.action == SynthesisDialog.SynthesisDialogResponse.ACTION_SYNTHESIS) {
                            playClickSe(true);

                            if (JniBridge.nativeGetItemOwnCount(ItemCode.BookOfSecrets1.getValue()) <= 0
                                    && JniBridge.nativeGetItemOwnCount(ItemCode.BookOfSecrets2.getValue()) <= 0
                                    && JniBridge.nativeGetItemOwnCount(ItemCode.BookOfSecrets3.getValue()) <= 0
                                    && JniBridge.nativeGetItemOwnCount(ItemCode.BookOfSecrets4.getValue()) <= 0
                                    && JniBridge.nativeGetItemOwnCount(ItemCode.BookOfSecrets5.getValue()) <= 0
                                    && JniBridge.nativeGetItemOwnCount(ItemCode.BookOfSecrets6.getValue()) <= 0) {
                                onShowDialog(DialogCode.NotHaveScroll.getValue(), null);
                                return;
                            }

                            onEvent(
                                    FragmentEventCode.PostGarbageSyntheses.getValue(),
                                    new GomipoiGarbageSynthesesParam(
                                            respons.data1,
                                            respons.data2,
                                            respons.data3));
                            return;
                        }
                    }
                }
                if (resultCode == DialogBase.RESULT_NG) {
                    playClickSe(true);
                    onShowDialog(DialogCode.SynthesisShortage.getValue(), null);
                    return;
                }
                playClickSe(false);
                closeDialog(dialog);
                unlockEvent();
                break;
            }

            case FailedSynthesis: {
                playClickSe(false);
                closeFloatingDialog(dialog);
                DialogBase parentDialog = getCurrentActiveDialog();
                if (parentDialog != null) {
                    parentDialog.unlockEvent();
                    if (parentDialog instanceof SynthesisDialog) {
                        // 合成成功ダイアログを閉じたことを通知
                        ((SynthesisDialog) parentDialog).receiveSynthesisSuccess();
                    }
                }
                unlockEvent();
                break;
            }

            case SynthesisSuccess: {
                playClickSe(false);
                closeFloatingDialog(dialog);
                DialogBase parentDialog = getCurrentActiveDialog();
                if (parentDialog != null) {
                    parentDialog.unlockEvent();
                    if (parentDialog instanceof SynthesisDialog) {
                        // 合成成功ダイアログを閉じたことを通知
                        ((SynthesisDialog) parentDialog).receiveSynthesisSuccess();
                    }
                }
                unlockEvent();
                break;
            }

            case SynthesisShortage: {
                playClickSe(false);
                closeFloatingDialog(dialog);
                DialogBase parentDialog = getCurrentActiveDialog();
                if (parentDialog != null) {
                    parentDialog.unlockEvent();
                }
                unlockEvent();
                break;
            }

            case ERROR: {
                playClickSe(false);
                closeFloatingDialog(dialog);
                mErrorDialog = null;
                unlockEvent();
                break;
            }

            case NetworkError: {
                playClickSe(false);
                closeFloatingDialog(dialog);
                mNetworkErrorkDialog = null;
                unlockEvent();
                break;
            }

            case BuyGem: {
                playClickSe(resultCode == DialogBase.RESULT_OK);
                closeDialog(dialog);
                if (resultCode == DialogBase.RESULT_OK) {
                    if (data == null || !(data instanceof ShopItem)) {
                        showErrorDialog();
                        return;
                    }

                    // PlayStoreの購入処理
                    if (mPurchaseManager != null) {
                        if (mPurchaseManager.purchase(
                                ((ShopItem) data).storeItem,
                                this,
                                PurchaseManager.CODE_ACTIVITY_RESULT_PURCHASE,
                                "") != PurchaseManager.PurchaseResult.PurchaseResult_purchased) {
                            unlockEvent();
                        }
                        return;
                    }
                }
                unlockEvent();
                break;
            }

            case Item: {
                if (resultCode != DialogBase.RESULT_OK || data == null || !(data instanceof ItemDialogResponse)) {
                    playClickSe(resultCode == DialogBase.RESULT_OK);
                    closeDialog(dialog);
                    // 一時停止を解除
                    if (getApp() != null) {
                        getApp().getJniBridge().pauseEnd();
                    }
                    unlockEvent();
                    return;
                }
                ItemDialogResponse response = (ItemDialogResponse) data;
                switch (response.status) {
                    case ItemDialogResponse.GET_NEWEST_INFO: {
                        onEvent(FragmentEventCode.GetItemOwn.getValue(), null);
                        break;
                    }

                    case ItemDialogResponse.MOVE_TO_SHOP: {
                        playClickSe(resultCode == DialogBase.RESULT_OK);
                        onChangedActivity(ChangeActivityCode.Shop.getValue(), null);
                        break;
                    }

                    case ItemDialogResponse.SHOW_USE_DIALOG: {
                        playClickSe(resultCode == DialogBase.RESULT_OK);

                        // 電動ほうきと電池は未公開とする
//                        if (response.listItemData.id.equals(ItemId.AUTO_BRROM)
//                                || response.listItemData.id.equals(ItemId.BATTERY)) {
//                            onShowDialog(DialogCode.Unimplemented.getValue(), null);
//                            return;
//                        }

                        if (response.listItemData.id.equals(ItemId.Z_DRINK) && JniBridge.nativeIsLimitItemUsing(ItemCode.Z_DRINK.getValue())) {
                            onShowDialog(DialogCode.UsedSameDay.getValue(), null);
                            return;
                        }
                        if (response.listItemData.id.equals(ItemId.DROP) && JniBridge.nativeIsLimitItemUsing(ItemCode.DROP.getValue())) {
                            onShowDialog(DialogCode.UsedSameDay.getValue(), null);
                            return;
                        }
                        if (response.listItemData.id.equals(ItemId.Seal)
                                && JniBridge.nativeIsItemUsing(ItemCode.BATTERY.getValue())
                                && JniBridge.nativeGetCurrentStage() != StageType.Garden.getValue()) {
                            onShowDialog(DialogCode.ConfirmSeal.getValue(), null);
                            return;
                        }

                        onShowDialog(DialogCode.ItemUse.getValue(), response.listItemData);
                        break;
                    }

                    case ItemDialogResponse.SHOW_BUY_DIALOG: {
                        playClickSe(resultCode == DialogBase.RESULT_OK);

                        // 電動ほうきと電池は未公開とする
//                        if (response.listItemData.id.equals(ItemId.AUTO_BRROM)
//                                || response.listItemData.id.equals(ItemId.BATTERY)) {
//                            onShowDialog(DialogCode.Unimplemented.getValue(), null);
//                            return;
//                        }


                        // 秘伝書
                        if ((response.listItemData.id.equals(ItemId.BookOfSecrets1)
                                || response.listItemData.id.equals(ItemId.BookOfSecrets2)
                                || response.listItemData.id.equals(ItemId.BookOfSecrets3))
                                && JniBridge.nativeGetItemOwnCount(response.listItemData.itemCode.getValue()) > 0) {
                            // 2回は購入できない
                            onShowDialog(DialogCode.AlreadyBought.getValue(), null);
                            return;
                        }

                        // ゴミ箱
                        if (response.listItemData.id.equals(ItemId.DustboxMiddle)) {
                            if (JniBridge.nativeIsItemUsing(ItemCode.DustboxMiddle.getValue())) {
                                onShowDialog(DialogCode.ItemUseAlreadyUsed.getValue(), null);
                                return;
                            }
                            else if (JniBridge.nativeIsItemUsing(ItemCode.DustboxBig.getValue())) {
                                onShowDialog(DialogCode.DonwgradeError.getValue(), null);
                                return;
                            }
                        }
                        else if (response.listItemData.id.equals(ItemId.DustboxBig)) {
                            if (JniBridge.nativeIsItemUsing(ItemCode.DustboxBig.getValue())) {
                                onShowDialog(DialogCode.ItemUseAlreadyUsed.getValue(), null);
                                return;
                            }
                            else if (JniBridge.nativeIsItemUsing(ItemCode.DustboxSmall.getValue())) {
                                onShowDialog(DialogCode.NotHavePrevItem.getValue(), ItemId.DustboxBig);
                                return;
                            }
                        }
                        else if (response.listItemData.id.equals(ItemId.GARBAGE_CAN_XL)) {
                            if (JniBridge.nativeGetCurrentStage() == StageType.DefaultRoom.getValue()
                                    && JniBridge.nativeGetCurrentGarbageCanType() == GarbageCanType.XL.getValue()) {
                                onShowDialog(DialogCode.AlreadyBought.getValue(), null);
                                return;
                            }

                            if (JniBridge.nativeGetCurrentStage() != StageType.DefaultRoom.getValue()
                                    && JniBridge.nativeIsItemUsing(ItemCode.GARBAGE_CAN_XL.getValue())) {
                                onShowDialog(DialogCode.AlreadyBought.getValue(), null);
                                return;
                            }
                            if (JniBridge.nativeIsItemUsing(ItemCode.DustboxSmall.getValue()) || JniBridge.nativeIsItemUsing(ItemCode.DustboxMiddle.getValue())) {
                                onShowDialog(DialogCode.NotHavePrevItem.getValue(), ItemId.GARBAGE_CAN_XL);
                                return;
                            }
                        }
                        else if (response.listItemData.id.equals(ItemId.DustboxMiddlePoiko)) {
                            if (JniBridge.nativeIsItemUsing(ItemCode.DustboxMiddlePoiko.getValue())) {
                                onShowDialog(DialogCode.ItemUseAlreadyUsed.getValue(), null);
                                return;
                            }
                            else if (JniBridge.nativeIsItemUsing(ItemCode.DustboxBigPoiko.getValue())) {
                                onShowDialog(DialogCode.DonwgradeError.getValue(), null);
                                return;
                            }
                        }
                        else if (response.listItemData.id.equals(ItemId.DustboxBigPoiko)) {
                            if (JniBridge.nativeIsItemUsing(ItemCode.DustboxBigPoiko.getValue())) {
                                onShowDialog(DialogCode.ItemUseAlreadyUsed.getValue(), null);
                                return;
                            }
                            else if (JniBridge.nativeIsItemUsing(ItemCode.DustboxSmallPoiko.getValue())) {
                                onShowDialog(DialogCode.NotHavePrevItem.getValue(), ItemId.DustboxBigPoiko);
                                return;
                            }
                        }
                        else if (response.listItemData.id.equals(ItemId.GARBAGE_CAN_XL_ROOM)) {
                            if (JniBridge.nativeGetCurrentStage() == StageType.PoikoRoom.getValue()
                                    && JniBridge.nativeGetCurrentGarbageCanType() == GarbageCanType.XL.getValue()) {
                                onShowDialog(DialogCode.AlreadyBought.getValue(), null);
                                return;
                            }

                            if (JniBridge.nativeGetCurrentStage() != StageType.PoikoRoom.getValue()
                                    && JniBridge.nativeIsItemUsing(ItemCode.GARBAGE_CAN_XL_ROOM.getValue())) {
                                onShowDialog(DialogCode.AlreadyBought.getValue(), null);
                                return;
                            }
                            if (JniBridge.nativeIsItemUsing(ItemCode.DustboxSmallPoiko.getValue()) || JniBridge.nativeIsItemUsing(ItemCode.DustboxMiddlePoiko.getValue())) {
                                onShowDialog(DialogCode.NotHavePrevItem.getValue(), ItemId.GARBAGE_CAN_XL_ROOM);
                                return;
                            }
                        }


                        // 箒
                        BroomType broomType = BroomType.valueOf(JniBridge.nativeGetCurrentBroomType());
                        if (broomType.equals(response.listItemData.getBroomType())) {
                            onShowDialog(DialogCode.AlreadyBought.getValue(), null);
                            return;
                        }

                        if (response.listItemData.id.equals(ItemId.GARDEN_KEY)) {
                            onShowDialog(DialogCode.GardenKeyMessage.getValue(), null);
                            return;
                        }

                        onShowDialog(DialogCode.ItemBuy.getValue(), response.listItemData);
                        break;
                    }

                    case ItemDialogResponse.SHOW_DETAILS_DIALOG: {
                        playClickSe(resultCode == DialogBase.RESULT_OK);
                        onShowDialog(DialogCode.RoomSecretDetail.getValue(), response.listItemData);
                        break;
                    }

                    default: {
                        playClickSe(resultCode == DialogBase.RESULT_OK);
                        closeDialog(dialog);
                        // 一時停止を解除
                        if (getApp() != null && getApp().getJniBridge() != null) {
                            getApp().getJniBridge().pauseEnd();
                        }
                        unlockEvent();
                        return;
                    }

                }
                break;
            }

            case AlreadyBought: {
                playClickSe(false);
                closeFloatingDialog(dialog);
                DialogBase parentDialog = getCurrentActiveDialog();
                if (parentDialog != null) {
                    parentDialog.unlockEvent();
                }
                unlockEvent();
                break;
            }

            case UsedSameDay: {
                playClickSe(resultCode == DialogBase.RESULT_OK);
                closeFloatingDialog(dialog);
                DialogBase parentDialog = getCurrentActiveDialog();
                if (parentDialog != null) {
                    parentDialog.unlockEvent();
                }
                unlockEvent();
                break;
            }

            case GardenKeyMessage: {
                playClickSe(resultCode == DialogBase.RESULT_OK);
                closeFloatingDialog(dialog);
                DialogBase parentDialog = getCurrentActiveDialog();
                if (parentDialog != null) {
                    parentDialog.unlockEvent();
                }
                unlockEvent();
                break;
            }

            case ConfirmSeal: {
                playClickSe(false);
                closeFloatingDialog(dialog);
                DialogBase parentDialog = getCurrentActiveDialog();
                if (parentDialog != null) {
                    parentDialog.unlockEvent();
                }

                if (resultCode == DialogBase.RESULT_OK) {
                    onShowDialog(DialogCode.ItemUse.getValue(), new ListItemData(ItemId.Seal));
                }

                unlockEvent();
                break;
            }

            case ItemShortage: {
                playClickSe(false);
                closeFloatingDialog(dialog);
                DialogBase currentDialog = getCurrentActiveDialog();
                if (currentDialog != null) {
                    currentDialog.unlockEvent();
                }
                break;
            }

            case ItemUse: {
                playClickSe(resultCode == DialogBase.RESULT_OK);
                closeFloatingDialog(dialog);
                if (resultCode == DialogBase.RESULT_OK) {
                    if (data != null && data instanceof ListItemData) {
                        ListItemData item = (ListItemData) data;
                        onEvent(FragmentEventCode.PostItemUse.getValue(), new GomipoiItemUseParam(item));
                    }
                } else {
                    DialogBase itemDialog = getCurrentActiveDialog();
                    if (itemDialog != null) {
                        itemDialog.unlockEvent();
                    }
                }
                unlockEvent();
                break;
            }

            case GemShortage: {
                playClickSe(resultCode == DialogBase.RESULT_OK);
                closeFloatingDialog(dialog);
                if (resultCode == DialogBase.RESULT_OK) {
                    onChangedActivity(ChangeActivityCode.Shop.getValue(), null);
                    return;
                } else {
                    unlockEvent();
                }

                // ItemのDialogが表示されている場合
                DialogBase currentDialog = getCurrentActiveDialog();
                if (currentDialog != null) {
                    currentDialog.unlockEvent();
                }
                break;
            }

            case ItemBuy: {
                DebugLog.i("GBActivityBase - onEvent case ItemBuy");
                playClickSe(resultCode == DialogBase.RESULT_OK);
                closeFloatingDialog(dialog);
                if (resultCode == DialogBase.RESULT_OK) {
                    if (getApp() != null && data != null && data instanceof ListItemData) {
                        ListItemData item = (ListItemData) data;

                        DebugLog.i("itemSet=" + item.itemSetCode.getValue());

                        onEvent(FragmentEventCode.PostItemSetBuy.getValue(), new GomipoiItemSetBuyParam(item));
                        return;
                    }
                }

                DialogBase itemDialog = getCurrentActiveDialog();
                if (itemDialog != null && itemDialog instanceof ItemDialog) {
                    ((ItemDialog) itemDialog).onChangedGem();
                    itemDialog.unlockEvent();
                }
                break;
            }

            case ItemBuyTicket: {
                DebugLog.i("GBActivityBase - onEvent case ItemBuyTicket");
                playClickSe(resultCode == DialogBase.RESULT_OK);
                closeFloatingDialog(dialog);
                if (resultCode == DialogBase.RESULT_OK) {
                    Fragment fragment = getCurrentActiveFragment();
                    if (fragment instanceof ExchangeFragment) {
                        ((ExchangeFragment) fragment).onBuyProcess();
                    }
                    return;
                } else {
                    unlockEvent();
                }

                DialogBase itemDialog = getCurrentActiveDialog();
                if (itemDialog != null && itemDialog instanceof ItemBuyTicketDialog) {
                    itemDialog.unlockEvent();
                }
                break;
            }

            case ItemBuyTicketSuccess: {
                DebugLog.i("GBActivityBase - onEvent case ItemBuyTicketSuccess");
                playClickSe(resultCode == DialogBase.RESULT_OK);
                closeFloatingDialog(dialog);

                if (resultCode == DialogBase.RESULT_OK) {
                    showConnectionDialog();
                    Fragment fragment = getCurrentActiveFragment();
                    if (fragment instanceof ExchangeFragment) {
                        ((ExchangeFragment) fragment).reloadPage();
                    }
                    return;
                }

                DialogBase itemDialog = getCurrentActiveDialog();
                if (itemDialog != null && itemDialog instanceof ItemBuyTicketSuccessDialog) {
                    itemDialog.unlockEvent();
                }
                break;
            }

            case RoomSecretDetail: {
                playClickSe(resultCode == DialogBase.RESULT_OK);
                closeFloatingDialog(dialog);

                DialogBase itemDialog = getCurrentActiveDialog();
                if (itemDialog != null) {
                    itemDialog.unlockEvent();
                }
                break;
            }

            case Connection: {
                closeDialog(dialog);
                break;
            }

            case LogoutConfirm: {
                playClickSe(resultCode == DialogBase.RESULT_OK);
                closeDialog(dialog);
                if (resultCode == DialogBase.RESULT_OK) {
                    showConnectionDialog();
                    onEvent(FragmentEventCode.DeleteUserSessions.getValue(), null);
                }
                unlockEvent();
                break;
            }

            case UnauthorizedError: {
                playClickSe(resultCode == DialogBase.RESULT_OK);
                closeDialog(dialog);
                logoutedFinish();
                break;
            }

            case GetNetworkErrorWithRetry: {
                playClickSe(true);
                closeFloatingDialog(dialog);
                mGetNetworkErrorWithRetryDialog = null;

                if (data == null) {
                    initLoading();
                    return;
                }

                // リトライ処理
                ConnectionManager.RetryData retryData = (ConnectionManager.RetryData) data;
                switch (retryData.code) {
                    case GomipoiItem_Own: {
                        onEvent(FragmentEventCode.GetItemOwn.getValue(), null);
                        break;
                    }

                    case Garbages_Recipe: {
                        onEvent(FragmentEventCode.GetGarbageRecipe.getValue(), null);
                        break;
                    }

                    case Book_Own: {
                        onEvent(FragmentEventCode.GetBookOwn.getValue(), retryData.param);
                        break;
                    }

                    case NewsTopic_Check: {
                        onEvent(FragmentEventCode.GetNewsTopicCheck.getValue(), null);
                        break;
                    }

                    default: {
                        initLoading();
                        break;
                    }
                }
                break;
            }

            case ServiceNetworkErrorWithRetry: {
                playClickSe(true);
                closeFloatingDialog(dialog);
                mServiceNetworkErrorWithRetryDialog = null;

                // リトライ処理
                ConnectionManager.RetryData retryData = (ConnectionManager.RetryData) data;
                if (resultCode == DialogBase.RESULT_OK) {
                    switch (retryData.code) {
                        case Garbage_Found: {
                            if (!(retryData.param instanceof GomipoiGarbageFoundParam)) {
                                showErrorDialog();
                                return;
                            }

                            onEvent(FragmentEventCode.PostGarbageFound.getValue(), retryData.param);
                            break;
                        }

                        case Game_Save: {
                            if (getApp() == null || !(retryData.param instanceof GomipoiGameSaveParam)) {
                                showErrorDialog();
                                return;
                            }

                            final GomipoiGameSaveParam saveParam = (GomipoiGameSaveParam) retryData.param;
                            // TODO: show message
                            getApp().getSaveDataServiceManager().getService().saveData(saveParam, getDeviceId());
                            break;
                        }

                        case  Jirokichi_Bonuses: {
                            if (getApp() == null || !(retryData.param instanceof GomipoiJirokichiBonusesParams)) {
                                showErrorDialog();
                                return;
                            }
                            break;
                        }
                    }
                }

                break;
            }

            case NetworkErrorWithRetry: {
                playClickSe(true);
                closeFloatingDialog(dialog);
                mNetworkErrorWithRetryDialog = null;

                if (getApp() != null && getApp().getAccessToken() == null) {
                    unlockEvent();
                    return;
                }

                // リトライ処理
                ConnectionManager.RetryData retryData = (ConnectionManager.RetryData) data;
                if (resultCode == DialogBase.RESULT_OK) {
                    switch (retryData.code) {
                        case Add_Jewel: {
                            onEvent(FragmentEventCode.PatchUserAppsJewel.getValue(), retryData.param);
                            break;
                        }

                        case Add_Point: {
                            onEvent(FragmentEventCode.PatchUserAppsPoint.getValue(), retryData.param);
                            break;
                        }

                        case Garbage_Found: {
                            onEvent(FragmentEventCode.PostGarbageFound.getValue(), retryData.param);
                            break;
                        }

                        case Garbage_Syntheses: {
                            onEvent(FragmentEventCode.PostGarbageSyntheses.getValue(), retryData.param);
                            break;
                        }

                        case Item_Use: {
                            onEvent(FragmentEventCode.PostItemUse.getValue(), retryData.param);
                            break;
                        }

                        case ItemSet_Buy: {
                            onEvent(FragmentEventCode.PostItemSetBuy.getValue(), retryData.param);
                            break;
                        }

                        case Book_Check: {
                            onEvent(FragmentEventCode.PostBookCheck.getValue(), retryData.param);
                            break;
                        }

                        case Book_ReceiveBonusPages: {
                            onEvent(FragmentEventCode.PostReceiveBonusPages.getValue(), retryData.param);
                            break;
                        }

                        case GomipoiFriends:
                        case FriendMessages_BadgeCount:
                        case SystemMessages_BadgeCount:
                            onEvent(FragmentEventCode.GetFriendList.getValue(), retryData.param);
                            break;

                        case Friends_SearchByCode:
                            onEvent(FragmentEventCode.GetFriendSearch.getValue(), retryData.param);
                            break;

                        case FriendCode_Use:
                            onEvent(FragmentEventCode.RequestAddFriend.getValue(), retryData.param);
                            break;

                        case Friends_ForPresentRequest:
                            onEvent(FragmentEventCode.GetGimmeList.getValue(), retryData.param);
                            break;

                        case FriendInvitations:
                            onEvent(FragmentEventCode.PostFriendInvitations.getValue(), retryData.param);
                            break;

                        case FriendPresents_FriendList:
                            onEvent(FragmentEventCode.Send_Present_FriendList.getValue(), retryData.param);
                            break;

                        case FriendPresents_MessageList:
                            onEvent(FragmentEventCode.Send_Present_MessageList.getValue(), retryData.param);
                            break;

                        case FriendPresents_Use:
                            onEvent(FragmentEventCode.Receive_Present.getValue(), retryData.param);
                            break;

                        case FriendshipBonuses:
                            onEvent(FragmentEventCode.Receive_FriendshipBonus.getValue(), retryData.param);
                            break;

                        case FriendPresentRequests_FriendList:
                            onEvent(FragmentEventCode.RequestGimme_FriendList.getValue(), retryData.param);
                            break;

                        case FriendPresentRequests_GimmeList:
                            onEvent(FragmentEventCode.RequestGimme_GimmeList.getValue(), retryData.param);
                            break;

                        case FriendMessages:
                            onEvent(FragmentEventCode.GetFriendMessage.getValue(), retryData.param);
                            break;

                    }
                }

                break;
            }

            case NotHaveScroll: {
                playClickSe(false);
                closeFloatingDialog(dialog);
                DialogBase parentDialog = getCurrentActiveDialog();
                if (parentDialog != null) {
                    closeDialog(parentDialog);
                }
                unlockEvent();
                break;
            }

            case DeletePictureAlert: {
                playClickSe(resultCode == DialogBase.RESULT_OK);
                closeFloatingDialog(dialog);

                if (resultCode == DialogBase.RESULT_OK) {
                    DialogBase parent = getCurrentActiveDialog();
                    if (parent != null && parent instanceof PicturePoiDialog) {
                        ((PicturePoiDialog)parent).deletePicture();
                        return;
                    }
                }

                DialogBase parentDialog = getCurrentActiveDialog();
                if (parentDialog != null) {
                    parentDialog.unlockEvent();
                }
                break;
            }

            case VersionUpDialog: {
                playClickSe(resultCode == DialogBase.RESULT_OK);
                closeDialog(dialog);

                if (resultCode == DialogBase.RESULT_OK) {
                    if (data == null) {
                        initLoading();
                    }
                    else if (data instanceof String) {
                        gotoGooglePlay((String) data);
                    }
                }

                break;
            }

            case BoughtItem: {
                playClickSe(false);
                closeFloatingDialog(dialog);
                DialogBase parentDialog = getCurrentActiveDialog();
                if (parentDialog != null) {
                    if (parentDialog instanceof ItemDialog) {
                        ((ItemDialog) parentDialog).refresh();
                    }
                    parentDialog.unlockEvent();
                }
                break;
            }

            case CaptureWaiting: {
                closeDialog(dialog);
                mCaptureWaitingDialog = null;
                break;
            }

            case Unimplemented:
            case SynthesisNotHaveReceipe:
            case SynthesisAlreadyGot:
            case GameSaveAlreadyMaxCapacity:
            case ItemUseAlreadyEmptyCan:
            case ItemUseAlreadyUsed:
            case DonwgradeError:
            case NotHavePrevItem:
            case ItemUseDisabled:
            case ItemUseNotChangeCan:
            case ItemSetBuyNoKey:
            case ItemSetBuyAchievedMaxPossession: {
                playClickSe(false);
                closeFloatingDialog(dialog);
                DialogBase parentDialog = getCurrentActiveDialog();
                if (parentDialog != null) {
                    parentDialog.unlockEvent();
                }
                break;
            }

            case SearchFriend: {
                playClickSe(resultCode == DialogBase.RESULT_OK);

                SearchResponse response = (SearchResponse)data;

                if (resultCode == DialogBase.RESULT_OK
                        && response != null) {

                    // API: /friends/search_by_code(.:format)
                    if (response.responseCode.equals(FriendActionCode.DATA_GET)) {
                        onEvent(
                                FragmentEventCode.GetFriendSearch.getValue(),
                                new FriendsSearchByCodeParam(response.getSearchText()));
                        return;
                    }

                    // API: /friend_code/use(.:format)
                    if (response.responseCode.equals(FriendActionCode.APPLY)) {
                        DebugLog.i("GBActivityBase - onEvent SearchFriend");
                        onEvent(
                                FragmentEventCode.RequestAddFriend.getValue(),
                                new FriendCodeUseParam(response.getSearchText()));
                        return;
                    }
                }

                closeDialog(dialog);

                onEvent(
                        FragmentEventCode.GetFriendList.getValue(),
                        null);

                break;
            }

            case InviteFriend: {
                playClickSe(resultCode == DialogBase.RESULT_OK);

                if (resultCode == DialogBase.RESULT_OK) {

                    if (data != null && data instanceof GomipoiFriendsResponse) {
                        // API: /friend_invitations(.:format)
                        onEvent(
                                FragmentEventCode.PostFriendInvitations.getValue(),
                                new FriendInvitationsParam(((GomipoiFriendsResponse)data).userId));
                    } else {
                        dialog.unlockEvent();
                    }
                    return;
                }

                closeDialog(dialog);
                unlockEvent();
                break;
            }

            case DeleteFriend: {
                DebugLog.i("GBActivityBase - onEvent DeleteFriend");
                playClickSe(resultCode == DialogBase.RESULT_OK);
                if (resultCode == DialogBase.RESULT_OK) {
                    // Delete friend here
                    if (data != null && data instanceof GomipoiFriendsResponse) {
                        DebugLog.i("GBActivityBase - onEvent DeleteFriend data: " + data);
                        Fragment fragment = getCurrentActiveFragment();
                        if (fragment instanceof FriendListFragment) {
                            ((FriendListFragment) fragment).deleteFriend(data);
                        }
                    } else {
                        dialog.unlockEvent();
                    }
                    return;
                }

                closeDialog(dialog);
                unlockEvent();
                break;
            }

            case DeleteFriendSuccess: {
                DebugLog.i("GBActivityBase - onEvent DeleteFriendSuccess");
                if (resultCode == DialogBase.RESULT_OK) {
                    Fragment fragment = getCurrentActiveFragment();
                    if (fragment instanceof FriendListFragment) {
                        ((FriendListFragment) fragment).refreshFriendList();
                    }
                }
                closeDialog(dialog);
                unlockEvent();
                break;
            }

            case GiveFriend: {
                playClickSe(resultCode == DialogBase.RESULT_OK);

                if (resultCode == DialogBase.RESULT_OK) {

                    if (data != null && data instanceof GomipoiFriendsResponse) {
                        // API: /friend_presents(.:format)_FriendList
                        onEvent(
                                FragmentEventCode.Send_Present_FriendList.getValue(),
                                new FriendPresentsParam(((GomipoiFriendsResponse)data).userId));
                    } else {
                        dialog.unlockEvent();
                    }
                    return;
                }

                closeDialog(dialog);
                unlockEvent();
                break;
            }

            case GimmeFriend: {
                playClickSe(resultCode == DialogBase.RESULT_OK);

                if (resultCode == DialogBase.RESULT_OK) {
                    if (data != null && data instanceof GomipoiFriendsResponse) {
                        // API: /friend_present_requests(.:format)_FriendList
                        onEvent(
                                FragmentEventCode.RequestGimme_FriendList.getValue(),
                                new FriendPresentRequestsParam(((GomipoiFriendsResponse)data).userId));
                    } else {
                        dialog.unlockEvent();
                    }
                    return;
                }

                closeDialog(dialog);
                unlockEvent();
                break;
            }

            case FriendMessage: {
                if (resultCode == DialogBase.RESULT_OK
                        && data != null
                        && data instanceof FriendMessageDialogResponse) {
                    FriendMessageDialogResponse response = (FriendMessageDialogResponse)data;
                    if (response.responseCode.equals(FriendActionCode.DATA_GET)) {
                        onEvent(FragmentEventCode.GetFriendMessage.getValue(), null);
                        return;
                    }

//                    if (response.responseCode.equals(FriendActionCode.SEND)
//                            && response.targetData != null) {
//                        playClickSe(true);
//
//                        // API: /friend_presents(.:format)_MessageList
//                        onEvent(
//                                FragmentEventCode.Send_Present_MessageList.getValue(),
//                                new FriendPresentsParam(response.targetData.fromUserId));
//
//                        return;
//                    }

                    if (response.responseCode.equals(FriendActionCode.RECEIVE)
                            && response.targetData != null && response.targetData instanceof SystemMessagesResponse) {
                        playClickSe(true);

                        // API: /friendship_bonuses/receive(.:format)
                        onEvent(
                                FragmentEventCode.Receive_FriendshipBonus.getValue(),
                                new FriendshipBonusParam(((SystemMessagesResponse) response.targetData).systemMessageId));
                        return;
                    }

                    if (response.responseCode.equals(FriendActionCode.INVITE)
                            && response.targetData != null && response.targetData instanceof FriendMessagesResponse) {
                        playClickSe(true);
                        closeDialog(dialog);

                        onChangedActivity(
                                ChangeActivityCode.InstallOther.getValue(),
                                ((FriendMessagesResponse) response.targetData).inviteAppId);

                        return;
                    }

                    if (response.responseCode.equals(FriendActionCode.DELETE)
                            && response.targetData != null && response.targetData instanceof FriendMessagesResponse) {

                        playClickSe(true);
                        closeDialog(dialog);

                        DebugLog.i("GBActivityBase - onEvent response");
                        return;
                    }
                }

                playClickSe(false);
                closeDialog(dialog);

                onEvent(
                        FragmentEventCode.GetFriendList.getValue(),
                        null);
                break;
            }

            case IconCan_Select: {
                if (resultCode == DialogBase.RESULT_OK) {
                    if (data != null && data instanceof IconCanSelectDialogResponse) {
                        IconCanSelectDialogResponse response = (IconCanSelectDialogResponse)data;

                        if (response.responseCode.equals(FriendActionCode.USE_ITEM)) {
                            closeDialog(dialog);
                            onShowDialog(DialogCode.Item.getValue(), null);
                            return;
                        }

                        if (response.responseCode.equals(FriendActionCode.DATA_GET)) {
                            onEvent(
                                    FragmentEventCode.GetGimmeList.getValue(),
                                    null);
                            return;
                        }

                        if (response.responseCode.equals(FriendActionCode.GIMME)) {
                            playClickSe(true);
                            onEvent(
                                    FragmentEventCode.RequestGimme_GimmeList.getValue(),
                                    new FriendPresentRequestsParam(response.targetData.userId));
                            return;
                        }
                    }
                }

                playClickSe(false);
                closeDialog(dialog);
                unlockEvent();
                break;
            }

            case SearchByCode_Invalid:
            case CodeUse_NotMatch:
            case CodeUse_IsSelf:
            case CodeUse_AlreadyFriend:
            case CodeUse_ReachedUpperLimitSelf:
            case CodeUse_ReachedUpperLimitFriend:
            case Present_ExceedUpperLimitSelf:
            case Present_ExceedUpperLimitFriend:
            case Presents_ExceedUpperLimitSelf:
            case Presents_ExceedUpperLimitFriend:
            case PresentsUse_Expiration:
            case PresentsUse_NoEffect:
            case PresentRequest_ExceedUpperLimit:
            case PresentRequests_ExceedUpperLimit:{
                playClickSe(false);
                closeFloatingDialog(dialog);
                unlockEvent();

                DialogBase parent = getCurrentActiveDialog();
                if (parent != null) {
                    parent.unlockEvent();
                }
                break;
            }

            case LoginGuestConfirm: {
                playClickSe(resultCode == DialogBase.RESULT_OK);
                closeDialog(dialog);
                if (resultCode == DialogBase.RESULT_OK) {
                    showConnectionDialog();
                    onEvent(FragmentEventCode.PostUserGuestRegister.getValue(), null);
                }
                unlockEvent();

                break;
            }

            case GuestLimitedFuncFriend: {
                playClickSe(resultCode == DialogBase.RESULT_OK);
                closeDialog(dialog);
                if (resultCode == DialogBase.RESULT_OK) {
                    // 会員登録画面に遷移
                    Fragment fragment = getCurrentActiveFragment();
                    onChangedFragment(
                            fragment,
                            RegistFragment.newInstance(),
                            true,
                            RegistFragment.getName(getApplicationContext()),
                            true,
                            null);
                }
                unlockEvent();
                break;
            }

            default: {
                playClickSe(false);
                closeDialog(dialog);
                unlockEvent();
                break;
            }
        }
    }

    // ------------------------------
    // ConnectionManager.OnConnectionManagerListener
    // ------------------------------
    @Override
    // ログアウトAPI成功後の処理
    public void ConnectionManager_OnDeleteLoginDataForLogout(boolean isNeedShowDialog) {
        if (getApp() != null && getApp().isLogouting()) {
            return;
        }

        logout(false);

        if (isNeedShowDialog) {
            onShowDialog(DialogCode.LogoutConfirm.getValue(), null);
        } else {
            logoutedFinish();
        }
    }

    @Override
    // 強制ログアウト
    public void ConnectionManager_OnAuthorizedError() {
        if (getApp() != null && getApp().isLogouting()) {
            return;
        }

        logout(true);
        onShowDialog(DialogCode.UnauthorizedError.getValue(), null);
    }

    @Override
    public void ConnectionManager_OnShowConnectDialog() {
        showConnectionDialog();
    }

    @Override
    public void ConnectionManager_OnHideConnectDialog() {
        closeConnectionDialog();
    }

    @Override
    public void ConnectionManager_OnShowNetworkErrorDialog() {
        if (mNetworkErrorkDialog != null) {
            return;
        }
        onShowDialog(DialogCode.NetworkError.getValue(), null);
    }

    @Override
    public void ConnectionManager_OnShowGetNetworkErrorDialog(ConnectionManager.RetryData retryData) {
        if (mGetNetworkErrorWithRetryDialog != null) {
            return;
        }
        onShowDialog(DialogCode.GetNetworkErrorWithRetry.getValue(), retryData);
    }

    @Override
    public void ConnectionManager_OnShowPostNetworkErrorDialog(ConnectionManager.RetryData retryData) {
        if (mNetworkErrorWithRetryDialog != null) {
            return;
        }
        onShowDialog(DialogCode.NetworkErrorWithRetry.getValue(), retryData);
    }

    @Override
    public void ConnectionManager_OnError(ConnectionCode code) {
        showErrorDialog();
    }

    // ------------------------------
    // OnApplicationListener
    // ------------------------------
    @Override
    public void onLoadServerData() {
        initLoading();
    }

    @Override
    public void onShowRetryDialog(ConnectionManager.RetryData retryData) {
        if (retryData == null) {
            onShowDialog(DialogCode.NetworkError.getValue(), null);
        } else {
            onShowDialog(DialogCode.ServiceNetworkErrorWithRetry.getValue(), retryData);
        }
    }

    @Override
    public void onSendSaveData(GomipoiGameSaveParam gameSaveParam) {
        if (mIsNeedGetRetry) {
            mIsNeedGetRetry = false;
            initLoading();
        }

        if (gameSaveParam != null && gameSaveParam.nextStage != -1) {
            GomipoiGameSaveParam.PlaceType placeType = GomipoiGameSaveParam.PlaceType.DEFAULT;
            StageType stage = StageType.valueOf(gameSaveParam.nextStage);
            switch (stage) {
                case DefaultRoom:
                    placeType = GomipoiGameSaveParam.PlaceType.DEFAULT;
                    break;

                case PoikoRoom:
                    placeType = GomipoiGameSaveParam.PlaceType.POIKO_ROOM;
                    break;

                case Garden:
                    placeType = GomipoiGameSaveParam.PlaceType.GARDEN;
                    break;

                default:
                    break;
            }

            onEvent(FragmentEventCode.Game_ChangeRoom.getValue(), new GomipoiGameMovePlaceParam(placeType));
        }
    }

    @Override
    public void onShowAuthorizeErrorDialog() {
        if (getApp() != null && getApp().isLogouting()) {
            return;
        }

        if (!isEnabledConnection()) {
            return;
        }

        // 強制ログアウト
        logout(true);
        onShowDialog(DialogCode.UnauthorizedError.getValue(), null);
    }

    @Override
    public void onAlreadyMaxCapacity(GomipoiGameSaveParam newGameSaveParam) {
        if (getApp() == null || !(newGameSaveParam instanceof GomipoiGameSaveParam)) {
            showErrorDialog();
            return;
        }

        final GomipoiGameSaveParam saveParam = newGameSaveParam;
        getApp().getSaveDataServiceManager().getService().saveData(saveParam, getDeviceId());
    }

    // ------------------------------
    // OnUUIDManagerListener
    // ------------------------------
    @Override
    public void UUIDManager_OnPermissionError() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PERMISSION_EXSTORAGE_REQUEST_FOR_UUID);
    }

    @Override
    public void UUIDManager_OnStorageError() {
        onShowDialog(DialogCode.StorageError.getValue(), null);
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    /**
     * デバイスIDを返す
     */
    public final String getDeviceId() {
        if (mDeviceId == null) {
            mDeviceId = UUIDManager.getUUID(getApplicationContext(), this);
        }
//        DebugLog.i("UUID = " + mDeviceId);
        return mDeviceId;
    }

    // ------------------------------
    // Function
    // ------------------------------
    /**
     * GBApplicationのインスタンスを返す
     */
    protected final GBApplication getApp() {
        Application application = getApplication();
        if (application == null || !(application instanceof GBApplication)) {
            return null;
        }
        return (GBApplication)application;
    }

    /**
     * リサイズするViewのIDを返す
     */
    protected int getResizeBaseViewId() {
        return 0;
    }

    /**
     * BGMを再生するか？
     */
    protected boolean isNeedBgmPlay() {
        return true;
    }

    /**
     * BGMの種類。デフォルトでNORMAL
     */
    protected BgmManager.BgmType getBgmType() {
        return BgmManager.BgmType.NORMAL;
    }

    /**
     * クリック時のSEを再生する
     */
    protected final void playClickSe(boolean isYes) {
        if (getApp() == null) {
            return;
        }

        if (isYes) {
            getApp().getSeManager().playSe(SeData.YES);
            return;
        }

        getApp().getSeManager().playSe(SeData.NO);
    }

    /**
     * ConnectionManagerのインスタンスを返す
     */
    protected ConnectionManager getConnectionManager() {
        return getConnectionManager(true);
    }

    /**
     * ConnectionManagerのインスタンスを返す
     */
    protected ConnectionManager getConnectionManager(boolean isNeedGetDeviceId) {
        if (mConnectionManager == null) {
            mConnectionManager = new ConnectionManager(
                    getApplicationContext(),
                    this);
        }
        if (isNeedGetDeviceId) {
            mConnectionManager.setDeviceId(getDeviceId());
        }
        return mConnectionManager;
    }

    /**
     * エラーダイアログを表示する
     */
    protected void showErrorDialog() {
        if (mErrorDialog != null) {
            return;
        }

        closeConnectionDialog();
        onShowDialog(DialogCode.ERROR.getValue(), null);
    }

    /**
     * 通信中ダイアログを表示する
     */
    protected void showConnectionDialog() {
        if (mConnectionDialog == null) {
            lockEvent();
            onShowDialog(DialogCode.Connection.getValue(), null);
        }
    }

    /**
     * 通信中ダイアログを閉じる
     */
    protected void closeConnectionDialog() {
        if (mConnectionDialog != null) {
            closeFloatingDialog(mConnectionDialog);
            mConnectionDialog = null;
        }
    }

    /**
     * ログアウト処理を行う(データの処理のみ)
     *  isForced: true 重複ログイン等による強制ログアウト false ユーザーによるログアウト
     */
    protected void logout(boolean isForced) {
        if (getApp() == null || getApp().isLogouting()) {
            return;
        }

        TopActivity.sShouldSendDeviceToken = true;
        getApp().logoutStart();
        getApp().getPreferenceManager().logout(isForced);
        getApp().setAccessToken(null);
        JniBridge.nativeLogout();
    }

    /**
     * ログアウト処理を行う(遷移処理)
     */
    protected void logoutedFinish() {
        if (getApp() == null || !getApp().isLogouting()) {
            return;
        }

        if (this instanceof TopActivity) {
            getApp().logoutEnd();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
        GBActivityBase.this.finish();
    }

    /**
     * 通信可能なActivityかを返す
     */
    protected boolean isEnabledConnection() {
        return true;
    }

    /**
     * 初期ローディング処理を行う
     */
    protected void initLoading() {
        if (mServiceNetworkErrorWithRetryDialog != null) {
            mIsNeedGetRetry = true;
            return;
        }
        mIsNeedGetRetry = false;

        // システム環境チェック
        // チェック後メンテナンスなどがなければユーザーデータ取得
        onEvent(FragmentEventCode.GetSystemCheck.getValue(),null);

//        if (getApp() != null && (getApp().getAccessToken() == null || getApp().getAccessToken().length() == 0)) {
//            if (getApp().getPreferenceManager().isNeedTutorial()) {
//                // ログアウトの処理
//                logout();
//                logoutedFinish();
//                return;
//            }
//
//            JniBridge.nativeOnStartGetData();
//            onEvent(
//                    FragmentEventCode.PostUserSessions.getValue(),
//                    new UserSessionsParam(
//                            UserSessionsParam.ParentActivity.OTHER_ACTIVITY,
//                            getApp().getPreferenceManager().getUserId(),
//                            getApp().getPreferenceManager().getUserPassword()));
//            return;
//        }
//
//        if (!isEnabledConnection()) {
//            onShowDialog(DialogCode.GetNetworkErrorWithRetry.getValue(), null);
//            return;
//        }
//
//        JniBridge.nativeOnStartGetData();
//        onEvent(FragmentEventCode.GetAllUserData.getValue(), null);
    }

    /**
     * 強制アップデートの通知のレシーバーを登録する
     */
    protected final void setForcedUpdateNotificationReceiver() {
        mForcedUpdateNotificationReceiver = new ForcedUpdateNotificationReceiver();
        registerReceiver(
                mForcedUpdateNotificationReceiver,
                new IntentFilter(dForcedUpdateAction));
    }

    /**
     * 強制アップデートの通知のレシーバーを解除する
     */
    protected final void unregistForcedUpdateNotificationReceiver() {
        if (mForcedUpdateNotificationReceiver != null) {
            unregisterReceiver(mForcedUpdateNotificationReceiver);
        }
    }

    /**
     * メンテナンスの通知のレシーバーを登録する
     */
    protected final void setMaintainanceNotificationReceiver() {
        mMaintainanceNotificationReceiver = new MaintainanceNotificationReceiver();
        registerReceiver(
                mMaintainanceNotificationReceiver,
                new IntentFilter(dMaintenanceAction));
    }

    /**
     * メンテナンスの通知のレシーバーを解除する
     */
    protected final void unregistMaintainanceNotificationReceiver() {
        if (mMaintainanceNotificationReceiver != null) {
            unregisterReceiver(mMaintainanceNotificationReceiver);
        }
    }

    /**
     * メンテナンスのダイアログを表示する
     */
    protected final void showMaintainanceAlert(String infoUrl, Map<String, Object> inMaintainInfo) {
        VersionUpDialog dialog = VersionUpDialog.newInstance(
                "VersionUpDialog",
                infoUrl,
                inMaintainInfo);
        showDialog(dialog);
    }

    /**
     * バージョンアップのダイアログを表示する
     */
    protected final void showVersionUpAlert(String appStoreUrl) {
        VersionUpDialog dialog = VersionUpDialog.newInstance("VersionUpDialog", appStoreUrl);
        showDialog(dialog);
    }

    /**
     * PlayStoreに遷移する
     */
    protected void gotoGooglePlay(String appUrl) {
        try {
            Uri uri = Uri.parse(appUrl);
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        } catch (ActivityNotFoundException e) {
            unlockEvent();
        }
    }

    protected void showCaptureWaitingDialog() {
        if (mCaptureWaitingDialog == null) {
            lockEvent();
            onShowDialog(DialogCode.CaptureWaiting.getValue(), null);
        }
    }

    protected void closeCaptureWaitingDialog() {
        if (mCaptureWaitingDialog != null) {
            closeFloatingDialog(mCaptureWaitingDialog);
            mCaptureWaitingDialog = null;
        }
    }

    private void handleRestoreEvent(HashMap<PurchaseManager.StoreItem, Purchase> storeItems) {
        Set<Map.Entry<PurchaseManager.StoreItem, Purchase>> set = storeItems.entrySet();
        final ArrayList<Purchase> purchases = new ArrayList<Purchase>();

        int jewel = 0;
        for (Map.Entry<PurchaseManager.StoreItem, Purchase> entry : set) {
            PurchaseManager.StoreItem storeItem = entry.getKey();

            switch (storeItem) {
                case jewel_10:
                    jewel += 10;
                    break;

                case jewel_50:
                    jewel += 50;
                    break;

                case jewel_100:
                    jewel += 100;
                    break;

                case jewel_300:
                    jewel += 300;
                    break;

                case jewel_500:
                    jewel += 500;
                    break;

                case jewel_1000:
                    jewel += 1000;
                    break;

                default:
                    break;
            }
            purchases.add(entry.getValue());
        }

        if (jewel > 0) {
            onEvent(
                    FragmentEventCode.PatchUserAppsJewel.getValue(),
                    new UserAppJewelParam(
                            UserAppJewelParam.Method.Restore,
                            jewel,
                            purchases));
        }
    }

    private void handlePurchaseEvent(PurchaseManager.PurchaseResult result, PurchaseManager.StoreItem storeItem, final Purchase purchaseInfo) {
        if (!availableDownload(result)) {
            return;
        }

        int jewel = 0;
        switch (storeItem) {
            case jewel_10:
                jewel = 10;
                break;

            case jewel_50:
                jewel = 50;
                break;

            case jewel_100:
                jewel = 100;
                break;

            case jewel_300:
                jewel = 300;
                break;

            case jewel_500:
                jewel = 500;
                break;

            case jewel_1000:
                jewel = 1000;
                break;

            default:
                break;
        }

        if (jewel > 0) {
            onEvent(
                    FragmentEventCode.PatchUserAppsJewel.getValue(),
                    new UserAppJewelParam(
                            UserAppJewelParam.Method.Shop,
                            jewel,
                            purchaseInfo));
        }
    }

    private boolean availableDownload(PurchaseManager.PurchaseResult result) {
        switch (result) {
            case PurchaseResult_purchased:
                // 正常動作系
                return true;
            case PurchaseResult_hasPurchased:
                // 購入済みアイテム
                return false;
            case PurchaseResult_unavailable:
                // サーバでアイテム取得失敗
                return false;
            case PurchaseResult_unknownError:
                // 不明なエラー
                return false;
            case PurchaseResult_devError:
                // apkの証明書エラー
                return false;
            case PurchaseResult_userCancel:
                // ユーザーキャンセル
                return false;
            default:
                return false;
        }
    }

    private boolean onPurchaseResultGot(int requestCode, int resultCode, Intent data) {
        if (mPurchaseManager == null) {
            return true;
        }
        else {
            // Pass on the activity result to the helper for handling
            if (!mPurchaseManager.handleActivityResult(requestCode, resultCode, data)) {
                // not handled, so handle it ourselves (here's where you'd perform any
                // handling of activity results not related to in-app billing...
                return false;
            }
            else return true;
        }
    }

    /**
     * データ取得
     */
    private void fetchAllData() {
        DebugLog.i("GBActivity - fetchAllData called");
        getConnectionManager().getAllData(new ConnectionManager.OnAllGetListener() {
            @Override
            public void AllGet_Ok() {
                DialogBase itemDialog = getCurrentActiveDialog();
                if (itemDialog != null && itemDialog instanceof ItemDialog) {
                    ((ItemDialog) itemDialog).onChangedGem();
                    itemDialog.unlockEvent();
                }

                if (!(GBActivityBase.this instanceof GameActivity)
                        && getApp() != null && getApp().getPlayerManager().isNeedShowNewsDialog()) {
                    if (layoutNews != null && webViewNews != null) {
                        layoutNews.setVisibility(View.VISIBLE);
                        getApp().getPlayerManager().shownNewsDialog();
                        HashMap<String, String> map = new HashMap<>();
                        map.put("X_PRIZE_EX_ACCESS_TOKEN", getApp().getAccessToken());
                        map.put("X_PRIZE_EX_ACCESS_APPLICATION_ID", String.valueOf(PJniBridge.nativeGetApplicationId()));
                        map.put("X_PRIZE_EX_ACCESS_DEVICE_ID", getDeviceId());
                        webViewNews.loadUrl(getApp().getPlayerManager().getNewsUrl(), map);
                    }
                } else {
                    if (!getApp().getPreferenceManager().getNotice1Hidden()
                            && getApp().getPlayerManager().isNeedShowNotice1Dialog()) {
                        onShowDialog(DialogCode.Notice1.getValue(), null);
                    } else if (!getApp().getPreferenceManager().getNotice2Hidden()
                            && getApp().getPlayerManager().isNeedShowNotice2Dialog()) {
                        onShowDialog(DialogCode.Notice2.getValue(), null);
                    } else if (!getApp().getPreferenceManager().getNotice3Hidden()
                            && getApp().getPlayerManager().isNeedShowNotice3Dialog()) {
                        onShowDialog(DialogCode.Notice3.getValue(), null);
                    }
                }

                if (GBActivityBase.this instanceof TopActivity) {
                    onEvent(FragmentEventCode.GetNotificationCheck.getValue(), null);
                    ((TopActivity)GBActivityBase.this).onFinishLoading();
                }

                JniBridge.nativeOnFinishedGetData();

                unlockEvent();
            }
        });
    }

    // ------------------------------
    // Inner-Class
    // ------------------------------
    /**
     * 強制アップデート通知のレシーバークラス
     */
    private final class ForcedUpdateNotificationReceiver extends BroadcastReceiver {

        public static final String KEY_INTENT_URL = "Key.Intent.Url";

        @Override
        public void onReceive(Context context, Intent intent) {
            // トップ画面以外でもメンテナンスアラートを表示する必要があるので、コメントアウト 2016.4.26
            //if ((GBActivityBase.this instanceof TopActivity)) {
                String appStoreUrl = intent.getStringExtra(KEY_INTENT_URL);
                showVersionUpAlert(appStoreUrl != null ? appStoreUrl : "");
//            } else {
//                finish();
//            }
        }
    }

    /**
     * メンテナンス通知のレシーバークラス
     */
    private final class MaintainanceNotificationReceiver extends BroadcastReceiver {

        public static final String KEY_INTENT_URL = "Key.Intent.Url";
        public static final String KEY_INTENT_MAP = "Key.Intent.Map";

        @Override
        public void onReceive(Context context, Intent intent) {
            // トップ画面以外でもメンテナンスアラートを表示する必要があるので、コメントアウト 2016.4.26
            //if ((GBActivityBase.this instanceof TopActivity)) {
                String url = intent.getStringExtra(KEY_INTENT_URL);
                HashMap<String, Object> maintainanceMap = (HashMap<String, Object>)intent.getSerializableExtra(KEY_INTENT_MAP);
                showMaintainanceAlert(url, maintainanceMap);
//            } else {
//                finish();
//            }
        }
    }

    /**
     * ユーザデータ復元用
     */
    private interface RestoreCallback {
        void onRestored(boolean isSuccessful);
    }
}
