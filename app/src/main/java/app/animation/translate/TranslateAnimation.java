package app.animation.translate;


import com.topmission.gomipoi.R;

import lib.fragment.FragmentTranslateAnimationBase;

/**
 * フェードの遷移アニメーションクラス
 */
public class TranslateAnimation extends FragmentTranslateAnimationBase {

	// ------------------------------
	// Override
	// ------------------------------
	@Override
	public boolean isNeed() {
		return true;
	}

	@Override
	public int getLeftEnter() {
		return R.anim.fade_enter;
	}

	@Override
	public int getRightEnter() {
		return R.anim.fade_enter;
	}

	@Override
	public int getLeftExit() {
		return R.anim.fade_exit;
	}

	@Override
	public int getRightExit() {
		return R.anim.fade_exit;
	}


}
