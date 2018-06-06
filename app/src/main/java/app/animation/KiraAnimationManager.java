package app.animation;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;

import lib.animation.view.OnViewAnimationListener;
import lib.animation.view.ViewAnimationManager;


/**
 * 合成成功キラキラアニメーション管理クラス
 * @author Yuya Hirayama (foot-loose)
 *
 */
public class KiraAnimationManager extends ViewAnimationManager {

	// ------------------------------
	// Member
	// ------------------------------
	private long mDurationTime = 1000L;
	private long mOffsetTime = 500L;

	// ------------------------------
	// Constructor
	// ------------------------------
	/**
	 * Constructor
	 * @param animationCode
	 * @param view
	 * @param listener
	 */
	public KiraAnimationManager(int animationCode, View view,
								OnViewAnimationListener listener) {
		super(animationCode, view, listener);
	}

	// ------------------------------
	// Function
	// ------------------------------

	/**
	 * アニメーションの時間と、オフセット時間を設定
	 * @param duration
	 * @param offset
	 */
	public void setAnimationTimer(long duration, long offset) {
		mDurationTime = duration;
		mOffsetTime = offset;
	}

	// ------------------------------
	// Override
	// ------------------------------
	@Override
	public Animation createViewAnimation() {
		AnimationSet anim = new AnimationSet(false);
		
		AlphaAnimation alphaAnim = new AlphaAnimation(0, 1);
		alphaAnim.setDuration(mDurationTime);
		alphaAnim.setStartOffset(mOffsetTime);
		alphaAnim.setRepeatCount(Animation.INFINITE);
		alphaAnim.setRepeatMode(Animation.REVERSE);
		anim.addAnimation(alphaAnim);

		return anim;
	}
}
