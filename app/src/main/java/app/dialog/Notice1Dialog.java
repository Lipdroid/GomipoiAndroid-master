package app.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.topmission.gomipoi.R;

import app.animation.button.ButtonAnimationManager;
import app.define.DialogCode;
import app.preference.GBPreferenceManager;
import common.dialog.GBDialogBase;

/**
 *
 */
public class Notice1Dialog extends GBDialogBase implements View.OnClickListener {

    // ------------------------------
    // Define
    // ------------------------------
    private static final String ARG_KEY_NAME = "#1#" + System.currentTimeMillis();

    // ------------------------------
    // Member
    // ------------------------------
    private Button checkButton;
    private Button closeButton;

    // ------------------------------
    // NewInstance
    // ------------------------------
    public static Notice1Dialog newInstance(String name) {
        Notice1Dialog dialog = new Notice1Dialog();
        Bundle args = new Bundle();
        args.putString(ARG_KEY_NAME, name);
        dialog.setArguments(args);
        return dialog;
    }

    // ------------------------------
    // Override
    // ------------------------------
    @Override
    public int getDialogCode() {
        return DialogCode.Notice1.getValue();
    }

    @Override
    public String getDialogName() {
        return getArguments().getString(ARG_KEY_NAME);
    }

    @Override
    protected View createDialogLayout(Dialog dialog) {
        View view = getInflater().inflate(R.layout.dialog_notice1, null);
        if (view == null) {
            return super.createDialogLayout(dialog);
        }

        checkButton = (Button) view.findViewById(R.id.checkButton);
        if (checkButton != null) {
            new ButtonAnimationManager(checkButton, this);
        }

        closeButton = (Button) view.findViewById(R.id.closeButton);
        if (closeButton != null) {
            new ButtonAnimationManager(closeButton, this);
        }

        unpdateCheckButton();

        return view;
    }

    @Override
    public void onDestroyView() {
        if (checkButton != null) {
            checkButton.setOnClickListener(null);
        }
        if (closeButton != null) {
            closeButton.setOnClickListener(null);
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
            case R.id.checkButton:
                boolean isHidden = getApp().getPreferenceManager().getNotice1Hidden();
                getApp().getPreferenceManager().setNotice1Hidden(!isHidden);
                unpdateCheckButton();
                unlockEvent();
                break;

            case R.id.closeButton:
                sendResult(RESULT_OK, null);
                break;

            default: {
                unlockEvent();
                break;
            }

        }
    }

    // ------------------------------
    // Function
    // ------------------------------
    private void unpdateCheckButton() {
        boolean isHidden = getApp().getPreferenceManager().getNotice1Hidden();
        if (checkButton != null) {
            checkButton.setSelected(isHidden);
        }
    }

}
