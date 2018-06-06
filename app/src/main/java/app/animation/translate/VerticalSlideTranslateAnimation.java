package app.animation.translate;


import com.topmission.gomipoi.R;

import lib.fragment.FragmentTranslateAnimationBase;

/**
 * 縦スライドの遷移アニメーションクラス
 */
public class VerticalSlideTranslateAnimation extends FragmentTranslateAnimationBase {

	// ------------------------------
	// Override
	// ------------------------------
	@Override
	public boolean isNeed() {
		return true;
	}

	@Override
	public int getLeftEnter() {
		return R.anim.vertical_translate_enter_left;
	}

	@Override
	public int getRightEnter() {
		return R.anim.vertical_translate_enter_right;
	}

	@Override
	public int getLeftExit() {
		return R.anim.vertical_translate_exit_left;
	}

	@Override
	public int getRightExit() {
		return R.anim.vertical_translate_exit_right;
	}


}
