package app.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.topmission.gomipoi.R;

import app.animation.button.ButtonAnimationManager;
import app.data.ListItemData;
import app.define.DialogCode;
import app.define.ItemCode;
import app.view.OutlineTextView;
import common.dialog.GBDialogBase;

/**
 *
 */
public class ItemUseDialog extends GBDialogBase implements View.OnClickListener {

    // ------------------------------
    // Define
    // ------------------------------
    private static final String ARG_KEY_ITEM = "#1#" + System.currentTimeMillis();

    private Button buttonLeft;
    private Button buttonRight;

    // ------------------------------
    // NewInstance
    // ------------------------------
    public static ItemUseDialog newInstance(ListItemData item) {
        ItemUseDialog dialog = new ItemUseDialog();
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
        return DialogCode.ItemUse.getValue();
    }

    @Override
    public String getDialogName() {
        return "ItemUseDialog";
    }

    @Override
    protected View createDialogLayout(Dialog dialog) {
        View view = getInflater().inflate(R.layout.dialog_use_item, null);
        if (view == null) {
            return super.createDialogLayout(dialog);
        }

        ListItemData item = (ListItemData) getArguments().getSerializable(ARG_KEY_ITEM);
        ImageView imageViewParts = (ImageView) view.findViewById(R.id.imageViewParts);
        if (imageViewParts != null && item != null) {
            imageViewParts.setImageResource(item.getPartsResourceId());
        }

        ImageView imageViewName = (ImageView) view.findViewById(R.id.imageViewName);
        if (imageViewName != null && item != null) {
            imageViewName.setImageResource(item.getPartsTextResourceId());
        }

        ImageView imageViewTextRetainCount = (ImageView) view.findViewById(R.id.imageViewTextRetainCount);
        if (imageViewTextRetainCount != null && item != null) {
            imageViewTextRetainCount.setVisibility(isHideRetain() ? View.GONE : View.VISIBLE);
        }

        OutlineTextView outlineTextViewRetainCount = (OutlineTextView) view.findViewById(R.id.outlineTextViewRetainCount);
        if (outlineTextViewRetainCount != null && item != null) {
            outlineTextViewRetainCount.setVisibility(isHideRetain() ? View.GONE : View.VISIBLE);
            outlineTextViewRetainCount.setOutlineTextAligh(OutlineTextView.ALIGN_RIGHT);
            outlineTextViewRetainCount.setText(item.getHaveCountText());
        }

        TextView textViewMessage = (TextView) view.findViewById(R.id.textViewMessage);
        if (textViewMessage != null && item != null) {
            textViewMessage.setVisibility(View.VISIBLE);
        }

        buttonLeft = (Button) view.findViewById(R.id.buttonLeft);
        if (buttonLeft != null && item != null) {
            buttonLeft.setBackgroundResource(R.drawable.button_yes);
            new ButtonAnimationManager(buttonLeft, this);
        }

        buttonRight = (Button) view.findViewById(R.id.buttonRight);
        if (buttonRight != null && item != null) {
            buttonRight.setVisibility(View.VISIBLE);
            new ButtonAnimationManager(buttonRight, this);
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        if (buttonRight != null) {
            buttonRight.setOnClickListener(null);
        }

        if (buttonLeft != null) {
            buttonLeft.setOnClickListener(null);
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
            case R.id.buttonLeft: {
                ListItemData item = (ListItemData)getArguments().getSerializable(ARG_KEY_ITEM);
                if (item == null) {
                    sendResult(RESULT_NG, getArguments().getSerializable(ARG_KEY_ITEM));
                    return;
                }

                sendResult(RESULT_OK, item);
                break;
            }

            case R.id.buttonRight: {
                sendResult(RESULT_NG, getArguments().getSerializable(ARG_KEY_ITEM));
                break;
            }

            default: {
                unlockEvent();
                break;
            }

        }
    }

    // ------------------------------
    // Function
    // ------------------------------
    private boolean isHideRetain() {
        ListItemData item = (ListItemData) getArguments().getSerializable(ARG_KEY_ITEM);
        if (item == null) {
            return false;
        }

        if (item.itemCode.equals(ItemCode.POIKO) || item.itemCode.equals(ItemCode.OTON) || item.itemCode.equals(ItemCode.KOTATSU)) {
            return true;
        }

        return false;
    }

}
