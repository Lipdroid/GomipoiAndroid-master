package lib.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import java.util.List;

import lib.application.ApplicationBase;
import lib.dialog.DialogBase;
import lib.dialog.OnDialogEventListener;
import lib.fragment.FragmentTranslateAnimationBase;
import lib.fragment.FragmentUtils;
import lib.fragment.OnFragmentListener;

/**
 * Fragmentを管理するActivityの基幹クラス
 * @author Yuya Hirayama (foot-loose)
 *
 */
public class FragmentActivityBase extends ActivityBase implements OnFragmentListener, OnDialogEventListener {

	// ------------------------------
	// Member
	// ------------------------------
	protected FragmentTranslateAnimationBase mFragmentAnimation;
	private Fragment mCurrentActiveFragment;
	private DialogBase mCurrentActiveDialog;
	
	// ------------------------------
	// OnDialogEventListener
	// ------------------------------
	@Override
	public void onEvent(DialogBase dialog, int resultCode, Object data) {
	}

	// ------------------------------
	// OnFragmentListener
	// ------------------------------
	@Override
	public boolean isLockedEvent() {
		return ((ApplicationBase) getApplication()).isLockedEvent();
	}

	@Override
	public void onChangedActivity(int requestCode, Object data) {
	}

	@Override
	public void onChangedActivity(int requestCode, Object data, int x, int y) {
	}

	@Override
	public void onChangedFragment(Fragment currentFragment,
			Fragment newFragment, boolean isStack, String name,
			boolean isNeedAnim, Object data) {
		
		// 遷移アニメが指定されていたら、そっちを使用する
		if (data != null && data instanceof FragmentTranslateAnimationBase) {
			changeFragment(newFragment, name, isStack, (FragmentTranslateAnimationBase)data);
			return;
		}
		
		// アニメーションがない場合
		FragmentTranslateAnimationBase animation = getFragmentAnimation();
		if (animation == null) {
			changeFragment(newFragment, name, isStack);
			return;
		}
		
		animation.setNeedAnimation(isNeedAnim);
		changeFragment(newFragment, name, isStack, animation);
	}

	@Override
	public void onClickedBackButton(Fragment currentFragment) {
		backScreen();
	}

	@Override
	public void onShowDialog(int dialogCode, Object data) {
	}

	@Override
	public void onEvent(int eventCode, Object data) {
		switch (eventCode) {
			case OnFragmentListener.EVENT_CODE_LOCK:
				lockEvent();
				break;
			case OnFragmentListener.EVENT_CODE_UNLOCK:
				unlockEvent();
				break;
		}		
	}

	@Override
	public void onEvent(int eventCode, Object data, View v) {
		switch (eventCode) {
			case OnFragmentListener.EVENT_CODE_LOCK:
				lockEvent();
				break;
			case OnFragmentListener.EVENT_CODE_UNLOCK:
				unlockEvent();
				break;
		}
	}
	
	// ------------------------------
	// Override
	// ------------------------------
	@Override
	public void onBackPressed() {
		// 更に前にスタックされたFragmentがあれば、アクティブなFragmentを差し替える
		FragmentManager manager = getSupportFragmentManager();
		List<Fragment> fragmentList = manager.getFragments();
		if (fragmentList != null && fragmentList.size() > 1) {
			onFinishedFragment(mCurrentActiveFragment);

			int removeCount = 2;
			if (fragmentList.get(fragmentList.size() - 1) == null) {
				removeCount = 3;
			}
			if (fragmentList.size() - removeCount >= 0) {
				mCurrentActiveFragment = fragmentList.get(fragmentList.size() - removeCount);
			} else {
				mCurrentActiveFragment = null;
			}
		}
		super.onBackPressed();
	}
	
	@Override
	protected void onDestroy() {
		mCurrentActiveFragment = null;
		super.onDestroy();
	}
	
	// ------------------------------
	// Function
	// ------------------------------
	/**
	 * Fragmentを包含するFrameLayoutのIDを返す
	 */
	protected int getFragmentContainerId() {
		return 0;
	}
	
	/**
	 * 現在アクティブなFragmentを返す
	 */
	protected Fragment getCurrentActiveFragment() {
		return mCurrentActiveFragment;
	}
	
	/**
	 * 先頭のFragmentをセットする
	 * @param fragment
	 * @param fragmentName
	 */
	protected void setFirstFragment(Fragment fragment, String fragmentName) {
		int containerId = getFragmentContainerId();
		setFirstFragment(containerId, fragment, fragmentName);
	}
	
	/**
	 * 先頭のFragmentをセットする
	 * @param containerId
	 * @param fragment
	 * @param fragmentName
	 */
	protected void setFirstFragment(int containerId, Fragment fragment, String fragmentName) {
		if (containerId == 0) {
			return;
		}
		
		mCurrentActiveFragment = fragment;
		FragmentUtils.replaceFragment(
				getSupportFragmentManager(),
				containerId,
				fragment,
				fragmentName,
				false);
	}

	/**
	 * フラグメントを変更する
	 * @param fragment
	 * @param fragmentName
	 * @param isStack
	 */
	protected void changeFragment(Fragment fragment, String fragmentName, boolean isStack) {
		int containerId = getFragmentContainerId();
		if (containerId == 0) {
			return;
		}
		
		mCurrentActiveFragment = fragment;
		FragmentUtils.replaceFragment(
				getSupportFragmentManager(),
				containerId,
				fragment,
				fragmentName,
				isStack);
	}

	/**
	 * フラグメントを変更する
	 * @param fragment
	 * @param fragmentName
	 * @param isStack
	 * @param animation
	 */
	protected void changeFragment(Fragment fragment, String fragmentName,
			boolean isStack, FragmentTranslateAnimationBase animation) {
		int containerId = getFragmentContainerId();
		if (containerId == 0) {
			return;
		}
		
		if (animation == null) {
			changeFragment(fragment, fragmentName, isStack);
			return;
		}
		
		mCurrentActiveFragment = fragment;
		FragmentUtils.replaceFragment(
				getSupportFragmentManager(),
				containerId,
				fragment,
				fragmentName,
				isStack,
				animation);
	}
	
	/**
	 * Fragmentの遷移アニメーションを返す
	 */
	protected FragmentTranslateAnimationBase getFragmentAnimation() {
		return null;
	}
	
	/**
	 * フラグメントの終了時の処理
	 * @param fragment
	 */
	protected void onFinishedFragment(Fragment fragment) {
	}
	
	/**
	 * 現在アクティブなDialogを返す
	 */
	protected DialogBase getCurrentActiveDialog() {
		return mCurrentActiveDialog;
	}
	
	/**
	 * Dialogを表示する
	 * @param dialog
	 */
	public void showDialog(DialogBase dialog) {
		if (mCurrentActiveDialog != null) {
			closeDialog(mCurrentActiveDialog);
		}
		
		mCurrentActiveDialog = dialog;
		dialog.show(getSupportFragmentManager());
	}

	/**
	 * ダイアログ上に更にダイアログを表示する
	 * @param dialog
	 */
	public void showFloatingDialog(DialogBase dialog) {
		if (dialog == null) {
			return;
		}

		dialog.show(getSupportFragmentManager());
	}

	/**
	 * ダイアログ上に表示したダイアログを閉じる
	 * @param dialog
	 */
	protected void closeFloatingDialog(DialogBase dialog) {
		if (dialog == null) {
			return;
		}

		dialog.close(getSupportFragmentManager());
	}
	
	/**
	 * ダイアログを閉じる
     * @param dialog
	 */
	protected void closeDialog(DialogBase dialog) {
		mCurrentActiveDialog = null;
		dialog.close(getSupportFragmentManager());
	}
	
	/**
	 * ダイアログを閉じる
	 */
	protected void closeDialog() {
		if (mCurrentActiveDialog == null) {
			return;
		}
		mCurrentActiveDialog.close(getSupportFragmentManager());
		mCurrentActiveDialog = null;
	}

}
