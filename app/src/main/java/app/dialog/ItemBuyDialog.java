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
import lib.convert.UnitUtils;

/**
 *
 */
public class ItemBuyDialog extends GBDialogBase implements View.OnClickListener {

    // ------------------------------
    // Define
    // ------------------------------
    private static final String ARG_KEY_ITEM = "#1#" + System.currentTimeMillis();

    // ------------------------------
    // Member
    // ------------------------------
    private ImageView imageViewParts;
    private ImageView imageViewCount;
    private ImageView imageViewName;
    private Button buttonLeft;
    private Button buttonRight;

    private ButtonAnimationManager mButtonLeftManager;
    private ButtonAnimationManager mButtonRightManager;


    // ------------------------------
    // NewInstance
    // ------------------------------
    public static ItemBuyDialog newInstance(ListItemData item) {
        ItemBuyDialog dialog = new ItemBuyDialog();
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
        return DialogCode.ItemBuy.getValue();
    }

    @Override
    public String getDialogName() {
        return "ItemBuyDialog";
    }

    @Override
    protected View createDialogLayout(Dialog dialog) {
        View view = getInflater().inflate(R.layout.dialog_buy_item, null);
        if (view == null) {
            return super.createDialogLayout(dialog);
        }

        ListItemData item = (ListItemData) getArguments().getSerializable(ARG_KEY_ITEM);
        imageViewParts = (ImageView) view.findViewById(R.id.imageViewParts);
        if (imageViewParts != null && item != null) {
            imageViewParts.setImageResource(item.getPartsResourceId());
        }

        imageViewName = (ImageView) view.findViewById(R.id.imageViewName);
        if (imageViewName != null && item != null) {
            imageViewName.setImageResource(item.getPartsTextResourceId());
        }

        OutlineTextView outlineTextViewGem = (OutlineTextView) view.findViewById(R.id.outlineTextViewGem);
        if (outlineTextViewGem != null) {
            outlineTextViewGem.setOutlineTextAligh(OutlineTextView.ALIGN_CENTER);
            outlineTextViewGem.setOutlineTextWidth((int) UnitUtils.getPxFromDp(getApplicationContext(), 10));
            outlineTextViewGem.setText(item != null ? item.getPriceText() : "");
        }

        TextView textViewAttention = (TextView) view.findViewById(R.id.textViewAttention);
        if (textViewAttention != null && item != null) {
            textViewAttention.setVisibility(item.isShowAttentionMessage() ? View.VISIBLE : View.GONE);
            if (item.itemCode.equals(ItemCode.OTON) || item.itemCode.equals(ItemCode.KOTATSU)) {
                textViewAttention.setVisibility(View.GONE);
            }
        }

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
                sendResult(RESULT_OK, getArguments().getSerializable(ARG_KEY_ITEM));
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
}
