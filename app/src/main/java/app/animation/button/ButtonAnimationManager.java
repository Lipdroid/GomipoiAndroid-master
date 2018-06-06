package app.animation.button;

import android.view.View;
import android.view.animation.Interpolator;


import com.topmission.gomipoi.R;

import lib.animation.button.ButtonAnimationManagerBase;

/**
 */
public class ButtonAnimationManager extends ButtonAnimationManagerBase {

    // ------------------------------
    // Define
    // ------------------------------
    private Interpolator interpolator = new ButtonAnimationInterpolator();

    // ------------------------------
    // Constructor
    // ------------------------------
    public ButtonAnimationManager(View targetView, View.OnClickListener parentClickListener) {
        super(targetView, parentClickListener);
    }

    // ------------------------------
    // Ovrride
    // ------------------------------
    @Override
    protected int getOutAnim() {
        return R.anim.button_scale_reduction;
    }

    @Override
    protected int getPressedAnim() {
        return R.anim.button_scale_expansion;
    }

    @Override
    protected int getReleaseAnim() {
        return R.anim.button_scale_reduction;
    }

    @Override
    protected Interpolator getReleaseAnimInterpolator() {
        return interpolator;
    }
}
