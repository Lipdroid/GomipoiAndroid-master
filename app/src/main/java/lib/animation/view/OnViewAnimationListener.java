package lib.animation.view;

import android.view.animation.Animation;

/**
 * ViewAnimationManagerのコールバックインターフェース
 * @author Yuya Hirayama (foot-loose)
 *
 */
public interface OnViewAnimationListener {

	// ------------------------------
	// Abstract
	// ------------------------------
	/**
	 * アニメーション開始のコールバック
	 * @param animationCode
	 * @param animation
	 */
	public void onAnimationStart(int animationCode, Animation animation);
	
	/**
	 * アニメーション繰り返しのコールバック
	 * @param animationCode
	 * @param animation
	 */
	public void onAnimationRepeat(int animationCode, Animation animation);
	
	/**
	 * アニメーション終了のコールバック
	 * @param animationCode
	 * @param animation
	 */
	public void onAnimationEnd(int animationCode, Animation animation);
	
}
