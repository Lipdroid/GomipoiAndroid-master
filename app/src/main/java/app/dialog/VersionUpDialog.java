package app.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.topmission.gomipoi.R;

import java.util.Map;

import app.animation.button.ButtonAnimationManager;
import app.define.DialogCode;
import common.dialog.GBDialogBase;

/**
 *
 */
public class VersionUpDialog extends GBDialogBase {

    // ------------------------------
    // Define
    // ------------------------------
    private static final String ARG_KEY_NAME = "#1#" + System.currentTimeMillis();

    private static final String KEY_ARGS_URL = "URL";
    private static final String KEY_ARGS_ISMAINTAIN = "IsMaintain";
    private static final String KEY_ARGS_MAINTAINTITLE = "MaintainTitle";
    private static final String KEY_ARGS_MAINTAINMESSAGE = "MaintainMessage";

    private static final String IDS_SERVER_INFO_NEEDS_UPDATEUP = "新しいバージョンが配信されています。\nアップデートしてください。";
    private static final String IDS_SERVER_INFO_UPDATEUP_BTN = "アップデート";
    private static final String IDS_SERVER_INFO_MAINTENANCE_BTN = "確認";

    // ------------------------------
    // Member
    // ------------------------------
    private TextView textViewMessage;
    private Button buttonLeft;

    // ------------------------------
    // NewInstance
    // ------------------------------
    public static VersionUpDialog newInstance(String name, String appStoreUrl) {
        return newInstance(name, appStoreUrl, null);
    }

    public static VersionUpDialog newInstance(String name, String appStoreUrl, Map<String, Object> inMaintainanceInfo) {
        VersionUpDialog dialog = new VersionUpDialog();
        Bundle args = new Bundle();
        args.putString(ARG_KEY_NAME, name);
        args.putString(KEY_ARGS_URL, appStoreUrl);
        if (inMaintainanceInfo != null) {
            args.putBoolean(KEY_ARGS_ISMAINTAIN, true);
            if (inMaintainanceInfo.containsKey("title")) {
                args.putString(KEY_ARGS_MAINTAINTITLE, (String) (inMaintainanceInfo.get("title")));
            }
            if (inMaintainanceInfo.containsKey("message")) {
                args.putString(KEY_ARGS_MAINTAINMESSAGE, (String) (inMaintainanceInfo.get("message")));
            }
        } else {
            args.putBoolean(KEY_ARGS_ISMAINTAIN, false);
        }

        dialog.setArguments(args);
        return dialog;
    }

    // ------------------------------
    // Override
    // ------------------------------
    @Override
    protected boolean isPermitTouchOutside() {
        return false;
    }

    @Override
    protected boolean isPermitCancel() {
        return false;
    }

    @Override
    public int getDialogCode() {
        return DialogCode.VersionUpDialog.getValue();
    }

    @Override
    public String getDialogName() {
        return getArguments().getString(ARG_KEY_NAME);
    }

    @Override
    protected View createDialogLayout(Dialog dialog) {
        LayoutInflater inflator = getInflater();
        View contentView = inflator.inflate(R.layout.dialog_version_up, null);

        boolean	isMaintain	=	getArguments().getBoolean(KEY_ARGS_ISMAINTAIN);

        TextView textViewTitle 	= (TextView) contentView.findViewById(R.id.textViewTitle);
        textViewMessage 	= (TextView) contentView.findViewById(R.id.textViewMessage);
        if (isMaintain) {
            if (getArguments().containsKey(KEY_ARGS_MAINTAINTITLE)) {
                textViewTitle.setVisibility(View.VISIBLE);
                textViewTitle.setText((String) getArguments().get(KEY_ARGS_MAINTAINTITLE));
            }

            if (getArguments().containsKey(KEY_ARGS_MAINTAINMESSAGE)) {
                textViewMessage.setText((String) getArguments().get(KEY_ARGS_MAINTAINMESSAGE));
            }
        } else {
            if (textViewTitle != null) {
                textViewTitle.setVisibility(View.GONE);
            }

            if (textViewMessage != null) {
                textViewMessage.setText(IDS_SERVER_INFO_NEEDS_UPDATEUP);
            }
        }

        buttonLeft = (Button) contentView.findViewById(R.id.buttonLeft);
        if (isMaintain) {
            buttonLeft.setText(IDS_SERVER_INFO_MAINTENANCE_BTN);
        } else {
            buttonLeft.setText(IDS_SERVER_INFO_UPDATEUP_BTN);
        }

        // YES NO TAPにアニメーションを設定
        ButtonAnimationManager animationManager = new ButtonAnimationManager(
                buttonLeft,
                new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sendResult(RESULT_OK, getArguments().getString(KEY_ARGS_URL));
            }

        });

        return contentView;
    }

}
