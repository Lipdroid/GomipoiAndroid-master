package lib.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * ダイアログ基幹クラス
 * @author Yuya Hirayama (foot-loose)
 *
 */
public class DialogBase extends DialogFragment {

	// ------------------------------
	// Define
	// ------------------------------
	public static final int RESULT_OK 		= 1;
	public static final int RESULT_NG 		= 2;
	public static final int RESULT_CANCEL 	= 3;
	
	// ------------------------------
	// Member
	// ------------------------------
	private View contentView;
	
	private boolean mIsLocked;
	private boolean mIsShowing;
	private OnDialogEventListener mDialogEventListener;
	
	// ------------------------------
	// Constructor
	// ------------------------------
	/**
	 * Constructor
	 */
	public DialogBase() {
		mIsShowing = false;
		mIsLocked = false;
	}
	
	// ------------------------------
	// Override
	// ------------------------------
	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = new Dialog(getActivity()) {
			@Override
			public void onBackPressed() {
				if (!onClickedBackButton()) {
					return;
				}
				super.onBackPressed();
			}
		};
    	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    	dialog.getWindow().setFlags(
    			WindowManager.LayoutParams.FLAG_FULLSCREEN, 
    			WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
    	this.setCancelable(isPermitCancel());
    	dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    	dialog.setCanceledOnTouchOutside(isPermitTouchOutside());
    	
    	// 継承先で好きにレイアウトを作ってもらう
    	contentView = createDialogLayout(dialog);
    	dialog.setContentView(contentView);
    	
		return dialog;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		if (activity instanceof OnDialogEventListener) {
			mDialogEventListener = (OnDialogEventListener) activity;
		}
	}
	
	@Override
	public void onDetach() {
		mDialogEventListener = null;
		super.onDetach();
	}
	
	@Override
	public void onCancel(DialogInterface dialog) {
		sendResult(RESULT_CANCEL, null);
	}
	
	// ------------------------------
	// Accesser
	// ------------------------------
	/**
	 * 表示中かどうかを返す
	 * @return
	 */
	public boolean isShowing() {
		return mIsShowing;
	}
	
	/**
	 * ダイアログを表示する
	 */
	public void show(FragmentManager manager) {
		if (isShowing())
			return;
		mIsShowing = true;
		
		if (manager == null) {
			mIsShowing = false;
			return;
		}
		
		if (getDialogName() == null) {
			mIsShowing = false;
			return;
		}
		
		FragmentTransaction transaction = manager.beginTransaction();
		if (transaction == null) {
			mIsShowing = false;
			return;
		}
		
		transaction.add(this, getDialogName());
		transaction.commitAllowingStateLoss();
	}
	
	/**
	 * 非表示にする
	 */
	public void close(FragmentManager manager) {
		if (!isShowing())
			return;
		mIsShowing = false;
		
		if (manager == null) {
			mIsShowing = true;
			return;
		}
		
		FragmentTransaction transaction = manager.beginTransaction();
		if (transaction == null) {
			mIsShowing = true;
			return;
		}
		
		transaction.remove(this);
		transaction.commitAllowingStateLoss();
	}
	
	/**
	 * ダイアログコードを返す
	 * @return
	 */
	public int getDialogCode() {
		return 0;
	}

	/**
	 * ダイアログ名を返す
	 * @return
	 */
	public String getDialogName() {
		return "";
	}
	

	// ------------------------------
	// Function
	// ------------------------------
	/**
	 * ContentViewを返す
	 * @return
	 */
	protected View getContentView() {
		return contentView;
	}
	
	/**
	 * コールバックリスナーのインスタンスを返す
	 * @return
	 */
	protected OnDialogEventListener getOnDialogEventListener() {
		return mDialogEventListener;
	}
	
	/**
	 * イベント通知を送る
	 * @param resultCode
	 * @param object
	 */
	protected void sendResult(int resultCode, Object object) {
		if (mDialogEventListener == null) {
			return;
		}
		mDialogEventListener.onEvent(this, resultCode, object);
	}
	
	/**
	 * イベントロックがかかっているかを返す
	 * @return
	 */
	protected boolean isEventLocked() {
		return mIsLocked;
	}
	
	/**
	 * イベントをロックする
	 */
	public void lockEvent() {
		mIsLocked = true;
	}
	
	/**
	 * イベントロックを解除する
	 */
	public void unlockEvent() {
		mIsLocked = false;
	}
	
	/**
	 * キャンセル処理を許可するかどうかを返す
	 */
	protected boolean isPermitCancel() {
		return true;
	}
	
	/**
	 * 外部タッチでダイアログ非表示処理を許可するかどうかを返す
	 * @return
	 */
	protected boolean isPermitTouchOutside() {
		return true;
	}
	
	/**
	 * ダイアログのレイアウトを整形する
	 */
	protected View createDialogLayout(Dialog dialog) {
		// 継承先でお好きにどうぞ
		return null;
	}
	
	/**
	 * LayoutInflaterのインスタンスを返す
	 * @return
	 */
	protected LayoutInflater getInflater() {
		return (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	protected boolean onClickedBackButton() {
		return true;
	}
}
