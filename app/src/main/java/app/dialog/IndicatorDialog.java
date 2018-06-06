package app.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import com.topmission.gomipoi.R;

import app.define.DialogCode;
import common.dialog.GBDialogBase;

/**
 *
 */
public class IndicatorDialog extends GBDialogBase {

    // ------------------------------
    // Define
    // ------------------------------

    // ------------------------------
    // Member
    // ------------------------------

    // ------------------------------
    // NewInstance
    // ------------------------------
    public static IndicatorDialog newInstance() {
        IndicatorDialog dialog = new IndicatorDialog();
        Bundle args = new Bundle();
        dialog.setArguments(args);
        return dialog;
    }

    // ------------------------------
    // Override
    // ------------------------------
    @Override
    public int getDialogCode() {
        return DialogCode.Connection.getValue();
    }

    @Override
    public String getDialogName() {
        return "Indicator";
    }

    @Override
    protected View createDialogLayout(Dialog dialog) {
        View view = getInflater().inflate(R.layout.dialog_indicator, null);
        if (view == null) {
            return super.createDialogLayout(dialog);
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    protected boolean isPermitCancel() {
        return false;
    }

    @Override
    protected boolean isPermitTouchOutside() {
        return false;
    }

}
