package app.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.topmission.gomipoi.R;

import app.animation.button.ButtonAnimationManager;
import app.define.DialogCode;
import common.dialog.GBDialogBase;

/**
 *
 */
public class Notice2Dialog extends GBDialogBase implements View.OnClickListener {

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
    public static Notice2Dialog newInstance(String name) {
        Notice2Dialog dialog = new Notice2Dialog();
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
        return DialogCode.Notice2.getValue();
    }

    @Override
    public String getDialogName() {
        return getArguments().getString(ARG_KEY_NAME);
    }

    @Override
    protected View createDialogLayout(Dialog dialog) {
        View view = getInflater().inflate(R.layout.dialog_notice2, null);
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
                boolean isHidden = getApp().getPreferenceManager().getNotice2Hidden();
                getApp().getPreferenceManager().setNotice2Hidden(!isHidden);
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
        boolean isHidden = getApp().getPreferenceManager().getNotice2Hidden();
        if (checkButton != null) {
            checkButton.setSelected(isHidden);
        }
    }

}
