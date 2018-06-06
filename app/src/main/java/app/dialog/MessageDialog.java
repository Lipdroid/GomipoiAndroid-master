package app.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.topmission.gomipoi.R;

import java.io.Serializable;

import app.animation.button.ButtonAnimationManager;
import app.define.DialogCode;
import app.dialog.define.MessageDialogType;
import common.dialog.GBDialogBase;

/**
 * メッセージを伝えるだけの汎用Dialogクラス
 */
public class MessageDialog extends GBDialogBase implements View.OnClickListener {

    // ------------------------------
    // Define
    // ------------------------------
    private static final String ARG_KEY_CODE = "#1#" + String.valueOf(System.currentTimeMillis());
    private static final String ARG_KEY_NAME = "#2#" + String.valueOf(System.currentTimeMillis());
    private static final String ARG_KEY_TYPE = "#3#" + String.valueOf(System.currentTimeMillis());
    private static final String ARG_KEY_MESSAGE = "#4#" + String.valueOf(System.currentTimeMillis());
    private static final String ARG_KEY_LEFT_BUTTON_TEXT = "#5#" + String.valueOf(System.currentTimeMillis());
    private static final String ARG_KEY_RIGHT_BUTTON_TEXT = "#6#" + String.valueOf(System.currentTimeMillis());
    private static final String ARG_KEY_DATA = "#7#" + String.valueOf(System.currentTimeMillis());
    private static final String ARG_KEY_PERMIT_CANCEL = "#8#" + String.valueOf(System.currentTimeMillis());

    private Button buttonLeft;
    private Button buttonRight;

    private ButtonAnimationManager mButtonLeftManager;
    private ButtonAnimationManager mButtonRightManager;

    // ------------------------------
    // NewInstance
    // ------------------------------
    public static MessageDialog newInstance(DialogCode code, FragmentManager fragmentManager,
            String name, MessageDialogType type) {
        return MessageDialog.newInstance(code, fragmentManager, name, type, null);
    }

    public static MessageDialog newInstance(DialogCode code, FragmentManager fragmentManager,
            String name, MessageDialogType type, boolean isPermitCancel) {
        return MessageDialog.newInstance(code, fragmentManager, name, type, null, isPermitCancel);
    }

    public static MessageDialog newInstance(DialogCode code, FragmentManager fragmentManager,
            String name, MessageDialogType type, Serializable data) {
        return MessageDialog.newInstance(code, fragmentManager, name, type, data, true);
    }

    public static MessageDialog newInstance(DialogCode code, FragmentManager fragmentManager,
            String name, MessageDialogType type, Serializable data, boolean isPermitCancel) {
        MessageDialog dialog = new MessageDialog();
        Bundle args = new Bundle();
        args.putInt(ARG_KEY_CODE, code.getValue());
        args.putString(ARG_KEY_NAME, name);
        args.putInt(ARG_KEY_TYPE, type.getValue());
        if (data != null) {
            args.putSerializable(ARG_KEY_DATA, data);
        }
        args.putBoolean(ARG_KEY_PERMIT_CANCEL, isPermitCancel);
        dialog.setArguments(args);
        return dialog;
    }

    // ------------------------------
    // Constructor
    // ------------------------------
    public MessageDialog() {
        super();
    }

    // ------------------------------
    // Override
    // ------------------------------
    @Override
    public int getDialogCode() {
        return getArguments().getInt(ARG_KEY_CODE);
    }

    @Override
    public String getDialogName() {
        return getArguments().getString(ARG_KEY_NAME);
    }

    @Override
    protected View createDialogLayout(Dialog dialog) {
        View view = getInflater().inflate(R.layout.dialog_message, null);
        if (view == null) {
            return super.createDialogLayout(dialog);
        }

        TextView textViewMessage = (TextView) view.findViewById(R.id.textViewMessage);
        if (textViewMessage != null) {
            textViewMessage.setText(getArguments().getString(ARG_KEY_MESSAGE));
        }

        buttonLeft = (Button) view.findViewById(R.id.buttonLeft);
        if (buttonLeft != null) {
            buttonLeft.setBackgroundResource(getArguments().getInt(ARG_KEY_LEFT_BUTTON_TEXT));
            mButtonLeftManager = new ButtonAnimationManager(buttonLeft, this);
        }

        buttonRight = (Button) view.findViewById(R.id.buttonRight);
        if (buttonRight != null) {
            if (isRightButtonVisible()) {
                buttonRight.setVisibility(View.VISIBLE);
                buttonRight.setBackgroundResource(getArguments().getInt(ARG_KEY_RIGHT_BUTTON_TEXT));
                mButtonRightManager = new ButtonAnimationManager(buttonRight, this);
            } else {
                buttonRight.setVisibility(View.GONE);
            }
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        mButtonLeftManager = null;
        mButtonRightManager = null;

        if (buttonLeft != null) {
            buttonLeft.setOnClickListener(null);
        }

        if (buttonRight != null) {
            buttonRight.setOnClickListener(null);
        }

        super.onDestroyView();
    }

    @Override
    protected boolean isPermitCancel() {
        return getArguments().getBoolean(ARG_KEY_PERMIT_CANCEL);
    }

    @Override
    protected boolean isPermitTouchOutside() {
        return getArguments().getBoolean(ARG_KEY_PERMIT_CANCEL);
    }

    @Override
    protected void sendResult(int resultCode, Object object) {
        super.sendResult(resultCode, getArguments().getSerializable(ARG_KEY_DATA));
    }

    @Override
    protected boolean onClickedBackButton() {
        if (isRightButtonVisible()) {
            sendResult(RESULT_NG, null);
        }
        else {
            sendResult(RESULT_OK, null);
        }
        return false;//super.onClickedBackButton();
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
            case R.id.buttonLeft:
                sendResult(RESULT_OK, null);
                break;

            case R.id.buttonRight:
                sendResult(RESULT_NG, null);
                break;

            default:
                unlockEvent();
                break;
        }

    }

    // ------------------------------
    // Accesser
    // ------------------------------
    /**
     * 表示するメッセージをセットする
     * @param message [String]
     */
    public final void setMessage(String message) {
        getArguments().putString(ARG_KEY_MESSAGE, message);
    }

    /**
     * 左側のボタンのテキストをセットする
     * @param buttonText [String]
     */
    public final void setLeftButtonText(int buttonText) {
        getArguments().putInt(ARG_KEY_LEFT_BUTTON_TEXT, buttonText);
    }

    /**
     * 右側のボタンのテキストをセットする
     * @param buttonText [String]
     */
    public final void setRightButtonText(int buttonText) {
        getArguments().putInt(ARG_KEY_RIGHT_BUTTON_TEXT, buttonText);
    }

    // ------------------------------
    // Function
    // ------------------------------
    /**
     * 右のボタンを表示するかを返す
     */
    private boolean isRightButtonVisible() {
        return MessageDialogType.valueOf(getArguments().getInt(ARG_KEY_TYPE)).isNeedVisibleRightButton();
    }

}
