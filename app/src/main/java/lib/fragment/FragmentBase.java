package lib.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;

/**
 * フラグメント基幹クラス
 * @author Yuya Hirayama (foot-loose)
 *
 */
public class FragmentBase extends Fragment {

	// ------------------------------
	// Member
	// ------------------------------
	private OnFragmentListener mFragmentListener;
	
	// ------------------------------
	// Override
	// ------------------------------
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof OnFragmentListener) {
			mFragmentListener = (OnFragmentListener) activity;
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (!isFragmentPager()) {
			unlockEvent();
		}
	}
	
	@Override
	public void onPause() {
		if (!isFragmentPager()) {
			lockEvent();
		}
		super.onPause();
	}
	
	@Override
	public void onDetach() {
		mFragmentListener = null;
		super.onDetach();
	}
	
	
	// ------------------------------
	// Accesser
	// ------------------------------
	/**
	 * 処理ロック中かを返す
	 * @return
	 */
	public final boolean isLocked() {
		// Activity側のイベントロックを取得
		boolean isActivityLocked = false;
		if (mFragmentListener != null) {
			isActivityLocked = mFragmentListener.isLockedEvent();
		}
		return isActivityLocked;
	}
	
	/**
	 * タイトルを返す
	 * @return
	 */
	public String getTitle() {
		return "";
	}
	
	// ------------------------------
	// Function
	// ------------------------------
	/**
	 * フラグメントイベントリスナーを返す
	 * @return
	 */
	protected final OnFragmentListener getFragmentListener() {
		return mFragmentListener;
	}
	
	/**
	 * 処理をロックする
	 */
	protected void lockEvent() {
		if (mFragmentListener != null) {
			mFragmentListener.onEvent(OnFragmentListener.EVENT_CODE_LOCK, null);
		}
	}
	
	/**
	 * 処理ロックを解除する
	 */
	protected void unlockEvent() {
		if (mFragmentListener != null) {
			mFragmentListener.onEvent(OnFragmentListener.EVENT_CODE_UNLOCK, null);
		}
	}
	
	/**
	 * Pager用のフラグメントか?<br>
	 * これを設定しないと、EventLockがおかしくなります
	 * @return
	 */
	protected boolean isFragmentPager() {
		return false;
	}
}
