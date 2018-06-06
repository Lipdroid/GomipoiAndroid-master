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
import app.define.ItemCode;
import app.define.ItemId;
import app.define.StageType;
import app.jni.JniBridge;
import app.view.OutlineTextView;

/**
 *
 */
public class ItemBuyPagerAdapter extends ItemPagerAdapter {

    // ------------------------------
    // Constructor
    // ------------------------------
    public ItemBuyPagerAdapter(Context context, List<ListItemData> itemList, OnItemPagerAdapterListener mListener) {
        super(context, itemList, mListener);
    }

    // ------------------------------
    // Override
    // ------------------------------
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_item_buy_pager, container, false);
        view.setTag(position);

        View layoutItem1 = view.findViewById(R.id.layoutItem1);
        if (layoutItem1 != null) {
            int index = 3 * position;
            ListItemData item = (mItemList.size() > index && index >= 0) ? mItemList.get(index) : null;
            ImageView imageViewItem1 = (ImageView) layoutItem1.findViewById(R.id.imageViewItem1);
            ImageView imageViewItemHave1 = (ImageView) layoutItem1.findViewById(R.id.imageViewItemHave1);
            OutlineTextView outlineTextViewHave1 = (OutlineTextView) layoutItem1.findViewById(R.id.outlineTextViewHave1);
            ImageView imageViewGrayout1 = (ImageView) layoutItem1.findViewById(R.id.imageViewGrayout1);

            setupItem(item, layoutItem1, imageViewItem1, imageViewItemHave1, outlineTextViewHave1, imageViewGrayout1);
        }

        View layoutItem2 = view.findViewById(R.id.layoutItem2);
        if (layoutItem2 != null) {
            int index = 3 * position + 1;
            ListItemData item = (mItemList.size() > index && index >= 0) ? mItemList.get(index) : null;
            layoutItem2.setTag(item);
            layoutItem2.setOnClickListener(this);

            int visibility = item != null ? View.VISIBLE : View.GONE;
            boolean disabled = isShowGrayout(item);
            layoutItem2.setEnabled(!disabled);

            ImageView imageViewItem2 = (ImageView) layoutItem2.findViewById(R.id.imageViewItem2);
            if (imageViewItem2 != null) {
                imageViewItem2.setVisibility(visibility);
                imageViewItem2.setImageResource(item != null ? item.getCardResourceId() : 0);
            }

            if (disabled) {
                ImageView imageViewItemHave2 = (ImageView) layoutItem2.findViewById(R.id.imageViewItemHave2);
                if (imageViewItemHave2 != null) {
                    imageViewItemHave2.setVisibility(View.GONE);
                }

                OutlineTextView outlineTextViewHave2 = (OutlineTextView) layoutItem2.findViewById(R.id.outlineTextViewHave2);
                if (outlineTextViewHave2 != null) {
                    outlineTextViewHave2.setVisibility(View.GONE);
                }
            } else {
                ImageView imageViewItemHave2 = (ImageView) layoutItem2.findViewById(R.id.imageViewItemHave2);
                if (imageViewItemHave2 != null) {
                    imageViewItemHave2.setVisibility(visibility);
                }

                OutlineTextView outlineTextViewHave2 = (OutlineTextView) layoutItem2.findViewById(R.id.outlineTextViewHave2);
                if (outlineTextViewHave2 != null) {
                    outlineTextViewHave2.setVisibility(visibility);
                    outlineTextViewHave2.setOutlineTextAligh(OutlineTextView.ALIGN_CENTER);
                    outlineTextViewHave2.setText(item != null ? item.getPriceText() : "");
                }
            }

            ImageView imageViewGrayout2 = (ImageView) layoutItem2.findViewById(R.id.imageViewGrayout2);
            if (imageViewGrayout2 != null) {
                imageViewGrayout2.setVisibility(disabled ? View.VISIBLE : View.GONE);
            }
        }

        View layoutItem3 = view.findViewById(R.id.layoutItem3);
        if (layoutItem3 != null) {
            int index = 3 * position + 2;
            ListItemData item = (mItemList.size() > index && index >= 0) ? mItemList.get(index) : null;
            layoutItem3.setTag(item);
            layoutItem3.setOnClickListener(this);

            int visibility = item != null ? View.VISIBLE : View.GONE;
            boolean disabled = isShowGrayout(item);
            layoutItem3.setEnabled(!disabled);

            ImageView imageViewItem3 = (ImageView) layoutItem3.findViewById(R.id.imageViewItem3);
            if (imageViewItem3 != null) {
                imageViewItem3.setVisibility(visibility);
                imageViewItem3.setImageResource(item != null ? item.getCardResourceId() : 0);
            }

            if (disabled) {
                ImageView imageViewItemHave3 = (ImageView) layoutItem3.findViewById(R.id.imageViewItemHave3);
                if (imageViewItemHave3 != null) {
                    imageViewItemHave3.setVisibility(View.GONE);
                }

                OutlineTextView outlineTextViewHave3 = (OutlineTextView) layoutItem3.findViewById(R.id.outlineTextViewHave3);
                if (outlineTextViewHave3 != null) {
                    outlineTextViewHave3.setVisibility(View.GONE);
                }
            } else {
                ImageView imageViewItemHave3 = (ImageView) layoutItem3.findViewById(R.id.imageViewItemHave3);
                if (imageViewItemHave3 != null) {
                    imageViewItemHave3.setVisibility(visibility);
                }

                OutlineTextView outlineTextViewHave3 = (OutlineTextView) layoutItem3.findViewById(R.id.outlineTextViewHave3);
                if (outlineTextViewHave3 != null) {
                    outlineTextViewHave3.setVisibility(visibility);
                    outlineTextViewHave3.setOutlineTextAligh(OutlineTextView.ALIGN_CENTER);
                    outlineTextViewHave3.setText(item != null ? item.getPriceText() : "");
                }
            }

            ImageView imageViewGrayout3 = (ImageView) layoutItem3.findViewById(R.id.imageViewGrayout3);
            if (imageViewGrayout3 != null) {
                imageViewGrayout3.setVisibility(disabled ? View.VISIBLE : View.GONE);
            }
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
                        mListener.OnItemPagerAdapterListener_onClickedBuyItem((ListItemData) v.getTag());
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
                           ImageView imageViewGrayout) {
        layoutItem.setTag(item);
        layoutItem.setOnClickListener(this);

        if (item == null) {
            layoutItem.setVisibility(View.GONE);
            imageViewItem.setVisibility(View.GONE);
            imageViewItemHave.setVisibility(View.GONE);
            outlineTextViewHave.setVisibility(View.GONE);
            imageViewGrayout.setVisibility(View.GONE);
        }
        else {
            boolean disabled = isShowGrayout(item);
            layoutItem.setEnabled(!disabled);

            if (imageViewItem != null) {
                imageViewItem.setVisibility(View.VISIBLE);
                imageViewItem.setImageResource(item.getCardResourceId());
            }

            if (disabled) {
                if (imageViewItemHave != null) {
                    imageViewItemHave.setVisibility(View.GONE);
                }

                if (outlineTextViewHave != null) {
                    outlineTextViewHave.setVisibility(View.GONE);
                }

                if (imageViewGrayout != null) {
                    imageViewGrayout.setVisibility(View.VISIBLE);
                }
            }
            else {
                if (imageViewItemHave != null) {
                    imageViewItemHave.setVisibility(View.VISIBLE);
                }

                if (outlineTextViewHave != null) {
                    outlineTextViewHave.setVisibility(View.VISIBLE);
                    outlineTextViewHave.setOutlineTextAligh(OutlineTextView.ALIGN_CENTER);
                    outlineTextViewHave.setText(item.getPriceText());
                }

                if (imageViewGrayout != null) {
                    imageViewGrayout.setVisibility(View.GONE);
                }


                if (item.id.equals(ItemId.GARDEN_KEY)) {
                    if (imageViewItemHave != null) {
                        imageViewItemHave.setVisibility(View.GONE);
                    }

                    if (outlineTextViewHave != null) {
                        outlineTextViewHave.setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    private boolean isShowGrayout(ListItemData item) {
        if (item == null) {
            return false;
        }

        if (item.id.equals(ItemId.BATTERY) && JniBridge.nativeGetItemOwnCount(ItemCode.AUTO_BRROM.getValue()) <= 0) {
            return true;
        }

        if ((item.id.equals(ItemId.BookOfSecrets1)
                || item.id.equals(ItemId.BookOfSecrets2)
                || item.id.equals(ItemId.BookOfSecrets3)
                || item.id.equals(ItemId.BookOfSecrets4)
                || item.id.equals(ItemId.BookOfSecrets5)
                || item.id.equals(ItemId.BookOfSecrets6)
                || item.id.equals(ItemId.PoikoRoomKey)
                || item.id.equals(ItemId.AUTO_BRROM)
                || item.id.equals(ItemId.OTON)
                || item.id.equals(ItemId.KOTATSU)
                || item.id.equals(ItemId.GARDEN_KEY)
                )
                && JniBridge.nativeGetItemOwnCount(item.itemCode.getValue()) > 0) {
            return true;
        }

        if ((item.id.equals(ItemId.DustboxMiddle) ||
            item.id.equals(ItemId.DustboxMiddlePoiko) ||
            item.id.equals(ItemId.DustboxBig) ||
            item.id.equals(ItemId.DustboxBigPoiko) ||
            item.id.equals(ItemId.GARBAGE_CAN_XL) ||
            item.id.equals(ItemId.GARBAGE_CAN_XL_ROOM)) &&
                JniBridge.nativeGetCurrentStage() == StageType.Garden.getValue()) {
            return true;
        }

        if ((item.id.equals(ItemId.RoomSecret1)
                || item.id.equals(ItemId.RoomSecret2)
                || item.id.equals(ItemId.RoomSecret3)
                || item.id.equals(ItemId.RoomSecret4)
                || item.id.equals(ItemId.RoomSecret5)
                || item.id.equals(ItemId.RoomSecret6)
                || item.id.equals(ItemId.DustboxMiddle)
                || item.id.equals(ItemId.DustboxMiddlePoiko)
                || item.id.equals(ItemId.DustboxBig)
                || item.id.equals(ItemId.DustboxBigPoiko))
                && item.alreadyOwnItemWithNonConsumable()) {
            return true;
        }

        return false;
    }
}
