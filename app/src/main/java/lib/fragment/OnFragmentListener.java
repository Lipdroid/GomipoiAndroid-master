package lib.fragment;

import android.support.v4.app.Fragment;
import android.view.View;

/**
 * フラグメントイベントリスナー
 * @author Yuya Hirayama (foot-loose)
 *
 */
public interface OnFragmentListener {
	
	// ------------------------------
	// Define
	// ------------------------------
	public static final int EVENT_CODE_LOCK 	= 998;
	public static final int EVENT_CODE_UNLOCK 	= 999;

	// ------------------------------
	// Abstract
	// ------------------------------
	/**
	 * イベントロックの状態を返す
	 * @return
	 */
	public boolean isLockedEvent();
	
	/**
	 * Activity変更時のコールバック
	 * @param RequestCode
	 * @param data
	 */	
	public void onChangedActivity(int requestCode, Object data);
	
	/**
	 * Activity変更時のコールバック
	 * @param requestCode
	 * @param data
	 * @param x
	 * @param y
	 */
	public void onChangedActivity(int requestCode, Object data, int x, int y);

	/**
	 * フラグメント変更時のコールバック
	 * @param currentFragment
	 * @param newFragment
	 * @param isStack
	 * @param name
	 * @param isNeedAnim
	 * @param data
	 */
	public void onChangedFragment(Fragment currentFragment, Fragment newFragment,
								  boolean isStack, String name, boolean isNeedAnim, Object data);
	
	/**
	 * 戻るボタン押下時のコールバック
	 * @param currentFragment
	 */
	public void onClickedBackButton(Fragment currentFragment);

	/**
	 * ダイアログ表示要求時のコールバック
	 * @param dialogCode
	 * @param data
	 */
	public void onShowDialog(int dialogCode, Object data);
	
	/**
	 * コールバック先で何らかのイベントのハンドリングをしてもらう為のコールバック
	 * @param eventCode
	 * @param data
	 */
	public void onEvent(int eventCode, Object data);
	
	public void onEvent(int eventCode, Object data, View v);
}
