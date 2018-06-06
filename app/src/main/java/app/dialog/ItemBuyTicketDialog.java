package app.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.topmission.gomipoi.R;

import app.animation.button.ButtonAnimationManager;
import app.define.DialogCode;
import app.number.NumberUtils;
import app.view.OutlineTextView;
import common.dialog.GBDialogBase;
import lib.convert.UnitUtils;

/**
 * Created by jerro on 3/20/2018.
 */

public class ItemBuyTicketDialog extends GBDialogBase implements View.OnClickListener {
    // ------------------------------
    // Define
    // ------------------------------
    private static final String ARG_KEY_ITEM = "#1#" + System.currentTimeMillis();

    // ------------------------------
    // Member
    // ------------------------------
    private Button buttonLeft;
    private Button buttonRight;

    private ButtonAnimationManager mButtonLeftManager;
    private ButtonAnimationManager mButtonRightManager;

    // ------------------------------
    // NewInstance
    // ------------------------------
    public static ItemBuyTicketDialog newInstance() {
        ItemBuyTicketDialog dialog = new ItemBuyTicketDialog();
        Bundle args = new Bundle();
        dialog.setArguments(args);
        return dialog;
    }

    // ------------------------------
    // Override
    // ------------------------------
    @Override
    public int getDialogCode() {
        return DialogCode.ItemBuyTicket.getValue();
    }

    @Override
    public String getDialogName() {
        return "ItemBuyTicketDialog";
    }

    @Override
    protected View createDialogLayout(Dialog dialog) {
        View view = getInflater().inflate(R.layout.dialog_exchange_ticket_buy, null);
        if (view == null) {
            return super.createDialogLayout(dialog);
        }

        OutlineTextView outlineTextViewGem = (OutlineTextView) view.findViewById(R.id.outlineTextViewGem);
        outlineTextViewGem.setOutlineTextAligh(OutlineTextView.ALIGN_CENTER);
        outlineTextViewGem.setOutlineTextWidth((int) UnitUtils.getPxFromDp(getApplicationContext(), 10));
        outlineTextViewGem.setText(NumberUtils.getNumberFormatText(1000));

        buttonLeft = (Button) view.findViewById(R.id.buttonLeft);
        if (buttonLeft != null) {
            mButtonLeftManager = new ButtonAnimationManager(buttonLeft, this);
        }

        buttonRight = (Button) view.findViewById(R.id.buttonRight);
        if (buttonRight != null) {
            mButtonRightManager = new ButtonAnimationManager(buttonRight, this);
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        mButtonLeftManager = null;
        mButtonRightManager = null;

        if (buttonRight != null) {
            buttonRight.setOnClickListener(null);
        }

        if (buttonLeft != null) {
            buttonLeft.setOnClickListener(null);
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
            case R.id.buttonLeft: {
                sendResult(RESULT_OK, null);
                break;
            }

            case R.id.buttonRight: {
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
