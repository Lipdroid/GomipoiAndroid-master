package app.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.topmission.gomipoi.R;

import app.animation.button.ButtonAnimationManager;
import app.define.DialogCode;
import common.dialog.GBDialogBase;

/**
 *
 */
public class NewsDialog extends GBDialogBase implements View.OnClickListener {

    // ------------------------------
    // Define
    // ------------------------------
    private static final String ARG_KEY_NAME = "#1#" + System.currentTimeMillis();

    // ------------------------------
    // NewInstance
    // ------------------------------
    public static NewsDialog newInstance(String name) {
        NewsDialog dialog = new NewsDialog();
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
        return DialogCode.News.getValue();
    }

    @Override
    public String getDialogName() {
        return getArguments().getString(ARG_KEY_NAME);
    }

    @Override
    protected View createDialogLayout(Dialog dialog) {
        View view = getInflater().inflate(R.layout.dialog_news, null);
        if (view == null) {
            return super.createDialogLayout(dialog);
        }

        TextView textViewNewsMessage = (TextView) view.findViewById(R.id.textViewNewsMessage);
        if (textViewNewsMessage != null) {
            textViewNewsMessage.setText("○月○日 〜 ○月○日\n大抽選発表会\n開催中 !!!");
        }

        Button buttonResult = (Button) view.findViewById(R.id.buttonResult);
        if (buttonResult != null) {
            new ButtonAnimationManager(buttonResult, this);
        }

        return view;
    }

    @Override
    public void onDestroyView() {
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
            case R.id.buttonResult: {
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
