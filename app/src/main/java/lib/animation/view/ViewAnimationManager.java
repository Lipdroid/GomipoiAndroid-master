package lib.animation.view;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

/**
 * Viewアニメーションの管理クラス
 * @author Yuya Hirayama (foot-loose)
 *
 */
public abstract class ViewAnimationManager implements OnGlobalLayoutListener, AnimationListener {

	// ------------------------------
	// Define
	// ------------------------------
	protected final View dTargetView;
	protected final OnViewAnimationListener dListener;
	
	// ------------------------------
	// Member
	// ------------------------------
	protected Animation mViewAnimation;
	protected int mAnimationCode;
	
	// ------------------------------
	// Constructor
	// ------------------------------
	/**
	 * Constructor
	 * @param view
	 */
	public ViewAnimationManager(int animationCode, View view, OnViewAnimationListener listener) {
		dTargetView = view;
		dListener = listener;
		mAnimationCode = animationCode;
	}
	
	// ------------------------------
	// OnGlobalLayoutListener
	// ------------------------------
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi") @Override
	public void onGlobalLayout() {
		if (dTargetView == null) {
			return;
		}
		if (Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1 < Build.VERSION.SDK_INT) {
			dTargetView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
		} else {
			dTargetView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
		}
		dTargetView.startAnimation(getViewAnimation());
	}
	
	// ------------------------------
	// AnimationListener
	// ------------------------------
	@Override
	public void onAnimationStart(Animation animation) {
		if (dListener != null) {
			dListener.onAnimationStart(mAnimationCode, animation);
		}
	}
	
	@Override
	public void onAnimationRepeat(Animation animation) {
		if (dListener != null) {
			dListener.onAnimationRepeat(mAnimationCode, animation);
		}
	}
	
	@Override
	public void onAnimationEnd(Animation animation) {
		stop();
		if (dListener != null) {
			dListener.onAnimationEnd(mAnimationCode, animation);
		}
	}
	
	// ------------------------------
	// Abstract
	// ------------------------------
	/**
	 * アニメーションを作成し、返す
	 * @return
	 */
	public abstract Animation createViewAnimation();
	
	// ------------------------------
	// Accesser
	// ------------------------------
	/**
	 * アニメーションコードをセットする
	 * @param animationCode
	 */
	public void setAnimationCode(int animationCode) {
		mAnimationCode = animationCode;
	}
	
	/**
	 * リリースする
	 */
	public void release() {
		this.stop();
		mViewAnimation = null;
	}
	
	/**
	 * アニメーションを開始する
	 */
	public void start() {
		if (dTargetView == null) {
			return;
		}
		
		if (dTargetView.getWidth() == 0) {
			dTargetView.getViewTreeObserver().addOnGlobalLayoutListener(this);
			return;
		} 
		dTargetView.startAnimation(getViewAnimation());
	}
	
	/**
	 * アニメーションを停止する
	 */
	public void stop() {
		if (dTargetView == null) {
			return;
		}
		dTargetView.clearAnimation();
	}
	
	// ------------------------------
	// Function
	// ------------------------------
	/**
	 * Listenerを返す
	 * @return
	 */
	protected OnViewAnimationListener getViewAnimationListener() {
		return dListener;
	}
	
	/**
	 * Animationを返す
	 * @return
	 */
	private Animation getViewAnimation() {
		if (mViewAnimation == null) {
			mViewAnimation = createViewAnimation();
			mViewAnimation.setAnimationListener(this);
		}
		return mViewAnimation;
	}
	
}
