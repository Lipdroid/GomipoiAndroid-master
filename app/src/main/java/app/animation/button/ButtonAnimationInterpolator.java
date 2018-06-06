package app.animation.button;

import android.view.animation.Interpolator;

/**
 * ボタンアニメーションのInterpolatorクラス
 */
public class ButtonAnimationInterpolator implements Interpolator {

    // ------------------------------
    // Override
    // ------------------------------
    @Override
    public float getInterpolation(float input) {
        if (input < 0.4f) {
            return (float) ((Math.cos((2.5f * input + 1f) * Math.PI) / 2.0f) + 0.5f) * 2f;
        } else if (input < 0.8f) {
            return (float) ((Math.cos((2.5f * input + 1f) * Math.PI) / 2.0f) + 0.5f) * 1.5f + 0.5f;
        }
        return (float) ((Math.cos((2.5f * input + 1f) * Math.PI) / 2.0f) + 0.5f) * 1f + 0.5f;
    }

}
