package app.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.topmission.gomipoi.R;

import app.animation.button.ButtonAnimationManager;
import app.define.DialogCode;
import app.define.SeData;
import app.number.NumberUtils;
import app.sound.SeManager;
import app.view.OutlineTextView;
import common.dialog.GBDialogBase;

/**
 *
 */
public class PicturePoiResultGetDialog extends GBDialogBase implements View.OnClickListener {

    // ------------------------------
    // Define
    // ------------------------------
    private static final String ARG_KEY_NAME = "#1#" + System.currentTimeMillis();
    private static final String ARG_KEY_DATA = "#2#" + System.currentTimeMillis();
    private static final String ARG_KEY_PLAY_SOUND = "#3#" + System.currentTimeMillis();

    private static final long ROTATE_ANIMATION = 3000l;

    // ------------------------------
    // Member
    // ------------------------------
    private Button buttonBack;
    private ImageView imageViewLight;

    // ------------------------------
    // NewInstance
    // ------------------------------
    public static PicturePoiResultGetDialog newInstance(String name, int addValue, boolean playSound) {
        PicturePoiResultGetDialog dialog = new PicturePoiResultGetDialog();
        Bundle args = new Bundle();
        args.putString(ARG_KEY_NAME, name);
        args.putInt(ARG_KEY_DATA, addValue);
        args.putBoolean(ARG_KEY_PLAY_SOUND, playSound);
        dialog.setArguments(args);
        return dialog;
    }

    // ------------------------------
    // Override
    // ------------------------------
    @Override
    public int getDialogCode() {
        return DialogCode.PicturePoiResultGet.getValue();
    }

    @Override
    public String getDialogName() {
        return getArguments().getString(ARG_KEY_NAME);
    }

    @Override
    protected View createDialogLayout(Dialog dialog) {
        View view = getInflater().inflate(R.layout.dialog_picture_poi_result_get, null);
        if (view == null) {
            return super.createDialogLayout(dialog);
        }

        OutlineTextView outlineTextView = (OutlineTextView) view.findViewById(R.id.outlineTextView);
        if (outlineTextView != null) {
            outlineTextView.setOutlineTextAligh(OutlineTextView.ALIGN_CENTER);
            int addValue = getArguments().getInt(ARG_KEY_DATA, 3);
            outlineTextView.setText(NumberUtils.getNumberFormatText(addValue));
//            outlineTextView.setText(NumberUtils.getNumberFormatText(3));
        }

        buttonBack = (Button) view.findViewById(R.id.buttonBack);
        if (buttonBack != null) {
            new ButtonAnimationManager(buttonBack, this);
        }

        imageViewLight = (ImageView) view.findViewById(R.id.imageViewLight);



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (imageViewLight != null) {
            ViewGroup.LayoutParams params = imageViewLight.getLayoutParams();
            RotateAnimation animation = new RotateAnimation(0f, 360f, params.width / 2f, params.height / 2f);
            animation.setDuration(ROTATE_ANIMATION);
            animation.setInterpolator(new LinearInterpolator());
            animation.setRepeatMode(Animation.RESTART);
            animation.setRepeatCount(Animation.INFINITE);
            imageViewLight.startAnimation(animation);
        }

        if (getArguments() != null) {
            boolean playSound = getArguments().getBoolean(ARG_KEY_PLAY_SOUND, false);
            if (playSound) {
                getApp().getSeManager().playSe(SeData.LEVELUP);

                getArguments().putBoolean(ARG_KEY_PLAY_SOUND, false);
            }
        }
    }

    @Override
    public void onStop() {
        if (imageViewLight != null) {
            imageViewLight.clearAnimation();
        }
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        if (buttonBack != null) {
            buttonBack.setOnClickListener(null);
        }

        super.onDestroyView();
    }

    @Override
    protected boolean isPermitCancel() {
        return true;
    }

    @Override
    protected boolean isPermitTouchOutside() {
        return false;
    }

    // ------------------------------
    // View.OnClickListener
    // ------------------------------
    @Override
    public void onClick(View v) {
        if (isEventLocked()) {
            return;
        }
        lockEvent();

        switch (v.getId()) {
            case R.id.buttonBack: {
                sendResult(RESULT_NG, null);
                break;
            }

            default: {
                unlockEvent();
                break;
            }
        }
    }
}
