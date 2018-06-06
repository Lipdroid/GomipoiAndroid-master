package app.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.topmission.gomipoi.R;

import app.animation.button.ButtonAnimationManager;
import app.data.ListItemData;
import app.define.DialogCode;
import common.dialog.GBDialogBase;

/**
 * Created by Herve on 2016/09/28.
 */
public class RoomSecretDialog extends GBDialogBase implements View.OnClickListener {

    // ------------------------------
    // Define
    // ------------------------------
    private static final String ARG_KEY_ITEM = "#1#" + System.currentTimeMillis();

    // ------------------------------
    // Member
    // ------------------------------
    private ImageView secretImageView;
    private ImageButton backButton;

    // ------------------------------
    // NewInstance
    // ------------------------------
    public static RoomSecretDialog newInstance(ListItemData item) {
        RoomSecretDialog dialog = new RoomSecretDialog();
        Bundle args = new Bundle();
        args.putSerializable(ARG_KEY_ITEM, item);
        dialog.setArguments(args);
        return dialog;
    }

    // ------------------------------
    // Override
    // ------------------------------
    @Override
    public int getDialogCode() {
        return DialogCode.RoomSecretDetail.getValue();
    }

    @Override
    public String getDialogName() {
        return "RoomSecretDetail";
    }

    @Override
    protected View createDialogLayout(Dialog dialog) {
        View view = getInflater().inflate(R.layout.dialog_room_secret, null);
        if (view == null) {
            return super.createDialogLayout(dialog);
        }

        ListItemData item = (ListItemData) getArguments().getSerializable(ARG_KEY_ITEM);
        secretImageView = (ImageView) view.findViewById(R.id.secret_image);
        if (secretImageView != null && item != null) {
            secretImageView.setImageResource(item.getImageForSecretPage());
        }

        backButton = (ImageButton) view.findViewById(R.id.back_button);
        if (backButton != null && item != null) {
            new ButtonAnimationManager(backButton, this);
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        if (backButton != null) {
            backButton.setOnClickListener(null);
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
            case R.id.back_button: {
                sendResult(RESULT_OK, getArguments().getSerializable(ARG_KEY_ITEM));
                break;
            }

            default: {
                unlockEvent();
                break;
            }

        }
    }
}
