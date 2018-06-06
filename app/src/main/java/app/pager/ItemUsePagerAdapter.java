package app.pager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.topmission.gomipoi.R;

import java.util.List;

import app.application.GBApplication;
import app.data.ListItemData;
import app.define.ItemId;
import app.jni.JniBridge;
import app.view.OutlineTextView;

/**
 *
 */
public class ItemUsePagerAdapter extends ItemPagerAdapter {

    // ------------------------------
    // Constructor
    // ------------------------------
    public ItemUsePagerAdapter(Context context, List<ListItemData> itemList, OnItemPagerAdapterListener mListener) {
        super(context, itemList, mListener);
    }

    // ------------------------------
    // Override
    // ------------------------------
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_item_use_pager, container, false);
        view.setTag(position);

        View layoutItem1 = view.findViewById(R.id.layoutItem1);
        if (layoutItem1 != null) {
            int index = 3 * position;
            ListItemData item = (mItemList.size() > index && index >= 0) ? mItemList.get(index) : null;

            ImageView imageViewItem1 = (ImageView) layoutItem1.findViewById(R.id.imageViewItem1);
            ImageView imageViewItemHave1 = (ImageView) layoutItem1.findViewById(R.id.imageViewItemHave1);
            OutlineTextView outlineTextViewHave1 = (OutlineTextView) layoutItem1.findViewById(R.id.outlineTextViewHave1);
            ImageView imageViewItemUseCheck1 = (ImageView) layoutItem1.findViewById(R.id.imageViewItemUseCheck1);
            ImageView imageViewGrayout1 = (ImageView) layoutItem1.findViewById(R.id.imageViewGrayout1);

            setupItem(item, layoutItem1, imageViewItem1, imageViewItemHave1, outlineTextViewHave1, imageViewItemUseCheck1, imageViewGrayout1);
        }

        View layoutItem2 = view.findViewById(R.id.layoutItem2);
        if (layoutItem2 != null) {
            int index = 3 * position + 1;
            ListItemData item = (mItemList.size() > index && index >= 0) ? mItemList.get(index) : null;

            ImageView imageViewItem2 = (ImageView) layoutItem2.findViewById(R.id.imageViewItem2);
            ImageView imageViewItemHave2 = (ImageView) layoutItem2.findViewById(R.id.imageViewItemHave2);
            OutlineTextView outlineTextViewHave2 = (OutlineTextView) layoutItem2.findViewById(R.id.outlineTextViewHave2);
            ImageView imageViewItemUseCheck2 = (ImageView) layoutItem2.findViewById(R.id.imageViewItemUseCheck2);
            ImageView imageViewGrayout2 = (ImageView) layoutItem2.findViewById(R.id.imageViewGrayout2);

            setupItem(item, layoutItem2, imageViewItem2, imageViewItemHave2, outlineTextViewHave2, imageViewItemUseCheck2, imageViewGrayout2);
        }

        View layoutItem3 = view.findViewById(R.id.layoutItem3);
        if (layoutItem3 != null) {
            int index = 3 * position + 2;
            ListItemData item = (mItemList.size() > index && index >= 0) ? mItemList.get(index) : null;

            ImageView imageViewItem3 = (ImageView) layoutItem3.findViewById(R.id.imageViewItem3);
            ImageView imageViewItemHave3 = (ImageView) layoutItem3.findViewById(R.id.imageViewItemHave3);
            OutlineTextView outlineTextViewHave3 = (OutlineTextView) layoutItem3.findViewById(R.id.outlineTextViewHave3);
            ImageView imageViewItemUseCheck3 = (ImageView) layoutItem3.findViewById(R.id.imageViewItemUseCheck3);
            ImageView imageViewGrayout3 = (ImageView) layoutItem3.findViewById(R.id.imageViewGrayout3);

            setupItem(item, layoutItem3, imageViewItem3, imageViewItemHave3, outlineTextViewHave3, imageViewItemUseCheck3, imageViewGrayout3);
        }

        ((GBApplication) mContext).getResizeManager().resize(view);
        container.addView(view);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layoutItem1:
            case R.id.layoutItem2:
            case R.id.layoutItem3: {
                if (v.getTag() != null && v.getTag() instanceof ListItemData) {
                    if (mListener != null) {
                        ListItemData item = (ListItemData) v.getTag();
                        if (!isShowGrayout(item)) {
                            mListener.OnItemPagerAdapterListener_onClickedUseItem(item);
                        }
                    }
                }
                break;
            }
        }
    }

    // ------------------------------
    // Function
    // ------------------------------
    private void setupItem(ListItemData item, View layoutItem, ImageView imageViewItem,
                           ImageView imageViewItemHave, OutlineTextView outlineTextViewHave,
                           ImageView imageViewItemUseCheck, ImageView imageViewGrayout) {
        layoutItem.setTag(item);
        layoutItem.setOnClickListener(this);

        if (item == null) {
            if (imageViewItem != null) {
                imageViewItem.setVisibility(View.GONE);
            }

            if (imageViewItemHave != null) {
                imageViewItemHave.setVisibility(View.GONE);
            }

            if (outlineTextViewHave != null) {
                outlineTextViewHave.setVisibility(View.GONE);
            }

            if (imageViewItemUseCheck != null) {
                imageViewItemUseCheck.setVisibility(View.GONE);
            }

            if (imageViewGrayout != null) {
                imageViewGrayout.setVisibility(View.GONE);
            }
        }
        else {
            if (imageViewItem != null) {
                imageViewItem.setVisibility(View.VISIBLE);
                imageViewItem.setImageResource(item.getCardResourceId());
            }

            if (imageViewItemHave != null) {
                imageViewItemHave.setVisibility(item.isItemCanOwnMultiple() ? View.VISIBLE : View.GONE);
            }

            if (outlineTextViewHave != null) {
                outlineTextViewHave.setVisibility(item.isItemCanOwnMultiple() ? View.VISIBLE : View.GONE);
                outlineTextViewHave.setOutlineTextAligh(OutlineTextView.ALIGN_CENTER);
                outlineTextViewHave.setText(item.getHaveCountText());
            }

            if (imageViewItemUseCheck != null) {
                imageViewItemUseCheck.setVisibility(item.isVisibleUseCheck() ? View.VISIBLE : View.GONE);
                imageViewItemUseCheck.setSelected(item.isNeedCheck());
            }

            if (imageViewGrayout != null) {
                boolean disabled = isShowGrayout(item);
                imageViewGrayout.setEnabled(!disabled);
                imageViewGrayout.setVisibility(disabled ? View.VISIBLE : View.GONE);
                if (imageViewItemHave != null && disabled) {
                    imageViewItemHave.setVisibility(View.GONE);
                }
                if (outlineTextViewHave != null && disabled) {
                    outlineTextViewHave.setVisibility(View.GONE);
                }
            }
        }
    }

    private boolean isShowGrayout(ListItemData item) {
        if (item.id.equals(ItemId.Z_DRINK)
                && (JniBridge.nativeIsBonusTime() ||
                JniBridge.nativeIsUsedZDrink() ||
                JniBridge.nativeIsUsedDrop())) {
            return true;
        }

        if (item.id.equals(ItemId.DROP)
                && (JniBridge.nativeIsBonusTime() ||
                JniBridge.nativeIsUsedZDrink() ||
                JniBridge.nativeIsUsedDrop())) {
            return true;
        }

        return !item.alreadyOwnItemWithNonConsumable();
    }

}
