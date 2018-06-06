package app.animation.translate;


import com.topmission.gomipoi.R;

import lib.fragment.FragmentTranslateAnimationBase;

/**
 * 横スライドの遷移アニメーションクラス
 */
public class SlideTranslateAnimation extends FragmentTranslateAnimationBase {

	// ------------------------------
	// Override
	// ------------------------------
	@Override
	public boolean isNeed() {
		return true;
	}

	@Override
	public int getLeftEnter() {
		return R.anim.translate_enter_left;
	}

	@Override
	public int getRightEnter() {
		return R.anim.translate_enter_right;
	}

	@Override
	public int getLeftExit() {
		return R.anim.translate_exit_left;
	}

	@Override
	public int getRightExit() {
		return R.anim.translate_exit_right;
	}


}
