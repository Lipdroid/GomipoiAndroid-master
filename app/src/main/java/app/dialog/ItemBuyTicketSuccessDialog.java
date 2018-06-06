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
import common.dialog.GBDialogBase;

/**
 * Created by jerro on 3/20/2018.
 */

public class ItemBuyTicketSuccessDialog extends GBDialogBase implements View.OnClickListener {
    // ------------------------------
    // Define
    // ------------------------------
    private static final String ARG_KEY_ITEM = "#1#" + System.currentTimeMillis();

    private ImageView imageViewLight;
    private Button buttonRight;

    private ButtonAnimationManager mButtonLeftManager;
    private ButtonAnimationManager mButtonRightManager;

    // ------------------------------
    // NewInstance
    // ------------------------------
    public static ItemBuyTicketSuccessDialog newInstance() {
        ItemBuyTicketSuccessDialog dialog = new ItemBuyTicketSuccessDialog();
        Bundle args = new Bundle();
        dialog.setArguments(args);
        return dialog;
    }

    // ------------------------------
    // Override
    // ------------------------------
    @Override
    public int getDialogCode() {
        return DialogCode.ItemBuyTicketSuccess.getValue();
    }

    @Override
    public String getDialogName() {
        return "ItemBuyTicketSuccessDialog";
    }

    @Override
    protected View createDialogLayout(Dialog dialog) {
        View view = getInflater().inflate(R.layout.dialog_exchange_ticket_get, null);
        if (view == null) {
            return super.createDialogLayout(dialog);
        }

        imageViewLight = (ImageView) view.findViewById(R.id.imageViewLight);

        buttonRight = (Button) view.findViewById(R.id.buttonRight);
        if (buttonRight != null) {
            mButtonRightManager = new ButtonAnimationManager(buttonRight, this);
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (imageViewLight != null) {
            ViewGroup.LayoutParams params = imageViewLight.getLayoutParams();
            RotateAnimation animation = new RotateAnimation(0f, 360f, params.width / 2f, params.height / 2f);
            animation.setDuration(3000);
            animation.setInterpolator(new LinearInterpolator());
            animation.setRepeatMode(Animation.RESTART);
            animation.setRepeatCount(Animation.INFINITE);
            imageViewLight.startAnimation(animation);
        }
    }

    @Override
    public void onDestroyView() {
        mButtonLeftManager = null;
        mButtonRightManager = null;

        if (buttonRight != null) {
            buttonRight.setOnClickListener(null);
        }

        super.onDestroyView();
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

            case R.id.buttonRight: {
                sendResult(RESULT_OK, null);
                break;
            }

            default: {
                unlockEvent();
                break;
            }

        }
    }
}