package lib.animation.button;

import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

/**
 * ボタンアニメーション管理基幹クラス
 */
public class ButtonAnimationManagerBase implements View.OnTouchListener, Animation.AnimationListener {

    // ------------------------------
    // Define
    // ------------------------------
    private final View targetView;
    private final View.OnTouchListener parentTouchListener;
    private final View.OnClickListener parentClickListener;

    // ------------------------------
    // Member
    // ------------------------------
    private Animation mReleaseAnimation;
    private boolean mIsPressed;
    private boolean mIsAnimating;

    // ------------------------------
    // Constructor
    // ------------------------------
    public ButtonAnimationManagerBase(@NonNull View targetView, View.OnClickListener parentClickListener) {
        this.targetView = targetView;
        this.parentTouchListener = null;
        this.parentClickListener = parentClickListener;
        this.mIsPressed = false;
        this.mIsAnimating = false;

        this.targetView.setOnTouchListener(this);
    }

    public ButtonAnimationManagerBase(@NonNull View targetView, View.OnTouchListener parentTouchListener, View.OnClickListener parentClickListener) {
        this.targetView = targetView;
        this.parentTouchListener = parentTouchListener;
        this.parentClickListener = parentClickListener;
        this.mIsPressed = false;

        this.targetView.setOnTouchListener(this);
    }

    // ------------------------------
    // Override
    // ------------------------------
    @Override
    protected void finalize() throws Throwable {
        try {
            super.finalize();
        } finally {
            targetView.setOnTouchListener(null);
            targetView.setOnClickListener(null);

            if (mReleaseAnimation != null) {
                mReleaseAnimation.setInterpolator(null);
                mReleaseAnimation.setAnimationListener(null);
                mReleaseAnimation = null;
            }
        }
    }

    // ------------------------------
    // OnTouchListener
    // ------------------------------
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (this.parentTouchListener != null) {
            this.parentTouchListener.onTouch(v, event);
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mIsPressed && !mIsAnimating) {
                    mIsPressed = true;
                    startPressedAnimation(v);
                }
                return true;

            case MotionEvent.ACTION_MOVE:
                if (mIsPressed && !mIsAnimating) {
                    // ボタンからフォーカスが外れた場合
                    if (isMoveOut(v, event)) {
                        startOutAnimation(v);
                        mIsPressed = false;
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                if (mIsPressed && !mIsAnimating) {
                    startReleaseAnimation(v);
                    mIsPressed = false;
                }
                break;

            default:
                return true;
        }

        return false;
    }

    // ------------------------------
    // AnimationListener
    // ------------------------------
    @Override
    public void onAnimationEnd(Animation animation) {
        if (parentClickListener != null) {
            parentClickListener.onClick(targetView);
        }
        mIsAnimating = false;
    }

    @Override
    public void onAnimationStart(Animation animation) {
        mIsAnimating = true;
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }

    // ------------------------------
    // Accesser
    // ------------------------------

    // ------------------------------
    // Function
    // ------------------------------
    /**
     * 押下時のアニメーションを返す
     */
    protected int getPressedAnim() {
        return 0;
    }

    /**
     * 範囲外にタッチが外れた時のアニメーションを返す
     */
    protected int getOutAnim() {
        return 0;
    }

    /**
     * 離した時のアニメーションを返す
     */
    protected int getReleaseAnim() {
        return 0;
    }

    /**
     * 離した時のアニメーションのInterpolatorを返す
     */
    protected Interpolator getReleaseAnimInterpolator() {
        return null;
    }

    /**
     * 押下時のアニメーションを開始する
     */
    protected final void startPressedAnimation(View v) {
        if (getPressedAnim() == 0) {
            return;
        }

        Animation settingBtnsAnimation = AnimationUtils.loadAnimation(
                v.getContext(),
                getPressedAnim());
        settingBtnsAnimation.setFillAfter(true);
        v.startAnimation(settingBtnsAnimation);
    }

    /**
     * タッチ座標がボタンの範囲から外れた時のアニメーションを開始する
     */
    protected final void startOutAnimation(View v) {
        if (getOutAnim() == 0) {
            return;
        }

        Animation settingBtnsAnimation = AnimationUtils.loadAnimation(
                v.getContext(),
                getOutAnim());
        settingBtnsAnimation.setFillAfter(true);
        v.startAnimation(settingBtnsAnimation);
    }

    /**
     * 離した時のアニメーションを開始する
     */
    protected final void startReleaseAnimation(View v) {
        if (getReleaseAnim() == 0) {
            if (parentClickListener != null) {
                parentClickListener.onClick(targetView);
            }
            mIsAnimating = false;
            return;
        }

        if (mReleaseAnimation == null) {
            mReleaseAnimation = AnimationUtils.loadAnimation(
                    v.getContext(),
                    getReleaseAnim());
            if (getReleaseAnimInterpolator() != null) {
                mReleaseAnimation.setInterpolator(getReleaseAnimInterpolator());
            }
            mReleaseAnimation.setAnimationListener(this);
        }
        v.startAnimation(mReleaseAnimation);
    }

    /**
     * Viewの範囲から外れているかを返す
     */
    protected final boolean isMoveOut(View targetView, MotionEvent event) {
        return (event.getX() < 0
                || targetView.getWidth() < event.getX()
                || event.getY() < 0
                || targetView.getHeight() < event.getY());
    }

}
