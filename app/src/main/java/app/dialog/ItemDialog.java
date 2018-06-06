package app.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.topmission.gomipoi.R;

import app.data.ItemDialogResponse;
import app.data.ListItemData;
import app.define.DialogCode;
import app.define.SeData;
import app.jni.JniBridge;
import app.number.NumberUtils;
import app.pager.ItemBuyPagerAdapter;
import app.pager.ItemUsePagerAdapter;
import app.pager.OnItemPagerAdapterListener;
import app.view.OutlineTextView;
import common.dialog.GBDialogBase;

/**
 *
 */
public class ItemDialog extends GBDialogBase implements View.OnClickListener, OnItemPagerAdapterListener, ViewPager.OnPageChangeListener {

    // ------------------------------
    // Define
    // ------------------------------
    private static final String ARG_KEY_INDEX = "#1#" + System.currentTimeMillis();
    private static final String ARG_KEY_PAGE = "#2#" + System.currentTimeMillis();

    private static final int INDEX_USE = 0;
    private static final int INDEX_BUY = 1;

    // ------------------------------
    // Member
    // ------------------------------
    private ViewPager viewPager;
    private ImageButton buttonUseIndex;
    private ImageButton buttonBuyIndex;
    private ImageView imageViewDescript;
    private LinearLayout pageLayout;

    private ImageView imageViewGemFrame;
    private OutlineTextView outlineTextViewGem;

    private ImageButton buttonArrowLeft;
    private ImageButton buttonArrowRight;

    // ------------------------------
    // NewInstance
    // ------------------------------
    public static ItemDialog newInstance() {
        ItemDialog dialog = new ItemDialog();
        Bundle args = new Bundle();
        args.putInt(ARG_KEY_INDEX, INDEX_USE);
        args.putInt(ARG_KEY_PAGE, 0);
        dialog.setArguments(args);
        return dialog;
    }

    // ------------------------------
    // Override
    // ------------------------------
    @Override
    public int getDialogCode() {
        return DialogCode.Item.getValue();
    }

    @Override
    public String getDialogName() {
        return "ItemDialog";
    }

    @Override
    protected View createDialogLayout(Dialog dialog) {
        View view = getInflater().inflate(R.layout.dialog_item, null);
        if (view == null) {
            return super.createDialogLayout(dialog);
        }

        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        if (viewPager != null) {
            viewPager.setAdapter(
                    new ItemUsePagerAdapter(
                            getApplicationContext(),
                            ListItemData.getUseList(),
                            this));
            viewPager.addOnPageChangeListener(this);
        }

        buttonUseIndex = (ImageButton) view.findViewById(R.id.buttonUseIndex);
        if (buttonUseIndex != null) {
            buttonUseIndex.setOnClickListener(this);
        }

        buttonBuyIndex = (ImageButton) view.findViewById(R.id.buttonBuyIndex);
        if (buttonBuyIndex != null) {
            buttonBuyIndex.setOnClickListener(this);
        }

        imageViewDescript = (ImageView) view.findViewById(R.id.imageViewDescript);
        if (imageViewDescript != null) {
            imageViewDescript.setImageResource(R.drawable.item_descript_use);
        }

        imageViewGemFrame = (ImageView) view.findViewById(R.id.imageViewGemFrame);
        if (imageViewGemFrame != null) {
            imageViewGemFrame.setOnClickListener(this);
        }

        outlineTextViewGem = (OutlineTextView) view.findViewById(R.id.outlineTextViewGem);
        if (outlineTextViewGem != null) {
            outlineTextViewGem.setOutlineTextAligh(OutlineTextView.ALIGN_RIGHT);
        }

        pageLayout = (LinearLayout) view.findViewById(R.id.layoutPage);
        makePageViews();

        buttonArrowLeft = (ImageButton) view.findViewById(R.id.buttonArrowLeft);
        if (buttonArrowLeft != null) {
            buttonArrowLeft.setOnClickListener(this);
        }

        buttonArrowRight = (ImageButton) view.findViewById(R.id.buttonArrowRight);
        if (buttonArrowRight != null) {
            buttonArrowRight.setOnClickListener(this);
        }

        onChangedGem();
        changeIndex();

        sendResult(RESULT_OK, new ItemDialogResponse(ItemDialogResponse.GET_NEWEST_INFO, null));

        return view;
    }

    @Override
    public void onDestroyView() {
        if (buttonArrowRight != null) {
            buttonArrowRight.setOnClickListener(null);
        }

        if (buttonArrowLeft != null) {
            buttonArrowLeft.setOnClickListener(null);
        }

        if (imageViewGemFrame != null) {
            imageViewGemFrame.setOnClickListener(null);
        }

        if (buttonBuyIndex != null) {
            buttonBuyIndex.setOnClickListener(null);
        }

        if (buttonUseIndex != null) {
            buttonUseIndex.setOnClickListener(null);
        }

        if (viewPager != null) {
            viewPager.clearOnPageChangeListeners();
            viewPager.setAdapter(null);
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
            case R.id.buttonUseIndex: {
                if (getArguments().getInt(ARG_KEY_INDEX) == INDEX_USE) {
                    unlockEvent();
                    return;
                }

                if (getApp() != null) {
                    getApp().getSeManager().playSe(SeData.YES);
                }

                getArguments().putInt(ARG_KEY_INDEX, INDEX_USE);
                changeIndex();
                break;
            }

            case R.id.buttonBuyIndex: {
                if (getArguments().getInt(ARG_KEY_INDEX) == INDEX_BUY) {
                    unlockEvent();
                    return;
                }

                if (getApp() != null) {
                    getApp().getSeManager().playSe(SeData.YES);
                }

                getArguments().putInt(ARG_KEY_INDEX, INDEX_BUY);
                changeIndex();
                break;
            }

            case R.id.imageViewGemFrame: {
                sendResult(RESULT_OK, new ItemDialogResponse(ItemDialogResponse.MOVE_TO_SHOP, null));
                break;
            }

            case R.id.buttonArrowLeft: {
                if (getApp() != null) {
                    getApp().getSeManager().playSe(SeData.YES);
                }

                if (viewPager != null) {
                    viewPager.setCurrentItem(getArguments().getInt(ARG_KEY_PAGE) - 1, true);
                }
                break;
            }

            case R.id.buttonArrowRight: {
                if (getApp() != null) {
                    getApp().getSeManager().playSe(SeData.YES);
                }

                if (viewPager != null) {
                    viewPager.setCurrentItem(getArguments().getInt(ARG_KEY_PAGE) + 1, true);
                }
                break;
            }

            default: {
                unlockEvent();
                break;
            }

        }
    }

    // ------------------------------
    // OnItemPagerAdapterListener
    // ------------------------------
    @Override
    public void OnItemPagerAdapterListener_onClickedBuyItem(ListItemData item) {
        if (isEventLocked()) {
            return;
        }
        lockEvent();
        sendResult(RESULT_OK, new ItemDialogResponse(ItemDialogResponse.SHOW_BUY_DIALOG, item));
    }

    @Override
    public void OnItemPagerAdapterListener_onShowAlreadyFullMaxLife() {

    }

    @Override
    public void OnItemPagerAdapterListener_onClickedUseItem(ListItemData item) {
        if (isEventLocked()) {
            return;
        }
        lockEvent();

        if (item.isSecretOfRoomItem()) {
            sendResult(RESULT_OK, new ItemDialogResponse(ItemDialogResponse.SHOW_DETAILS_DIALOG, item));
        }
        else {
            sendResult(RESULT_OK, new ItemDialogResponse(ItemDialogResponse.SHOW_USE_DIALOG, item));
        }
    }

    // ------------------------------
    // ViewPager.OnPageChangeListener
    // ------------------------------
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        if (getApp() != null) {
            getApp().getSeManager().playSe(SeData.PAGE);
        }

        getArguments().putInt(ARG_KEY_PAGE, position);
        refreshPage();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state != ViewPager.SCROLL_STATE_IDLE) {
            return;
        }

        unlockEvent();
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    /**
     * 宝石数の変更があった場合の処理
     */
    public void onChangedGem() {
        if (getApp() == null) {
            return;
        }

        int gem = JniBridge.nativeGetGem();
        if (outlineTextViewGem != null) {
            outlineTextViewGem.setText(NumberUtils.getNumberFormatText(gem));
        }

        unlockEvent();
    }

    /**
     * Pagerを更新する場合の処理
     */
    public void refresh() {
        if (viewPager != null) {
            viewPager.getAdapter().notifyDataSetChanged();
        }
    }

    // ------------------------------
    // Function
    // ------------------------------
    /**
     * インデックスの変更処理
     */
    private void changeIndex() {
        int index = getArguments().getInt(ARG_KEY_INDEX);

        if (buttonUseIndex != null) {
            buttonUseIndex.setSelected(index == INDEX_USE);
        }

        if (buttonBuyIndex != null) {
            buttonBuyIndex.setSelected(index == INDEX_BUY);
        }

        if (imageViewDescript != null) {
            imageViewDescript.setImageResource(
                    index == INDEX_USE ?
                            R.drawable.item_descript_use :
                            R.drawable.item_descript_buy);
        }

        if (viewPager != null) {
            viewPager.setAdapter(
                    index == INDEX_USE
                            ? new ItemUsePagerAdapter(
                                getApplicationContext(),
                                ListItemData.getUseList(),
                                this)
                            : new ItemBuyPagerAdapter(
                                getApplicationContext(),
                                ListItemData.getBuyList(),
                                this));
            getArguments().putInt(ARG_KEY_PAGE, 0);
        }

        refreshPage();

        unlockEvent();
    }

    private void makePageViews() {
        int pageCount = (ListItemData.getBuyList().size() - 1) / 3 + 1;
        int height = pageLayout.getLayoutParams().height;
        int margin = 8 * height / 16;

        for (int i = 0; i < pageCount; i++) {
            ImageView imageView = new ImageView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(height, height);
            params.rightMargin = (i == pageCount - 1) ? 0 : margin;

            imageView.setImageResource(R.drawable.selector_item_page);
            pageLayout.addView(imageView, params);
        }
    }

    /**
     * ページを更新する
     */
    private void refreshPage() {
        int currentPage = getArguments().getInt(ARG_KEY_PAGE);
        int index = getArguments().getInt(ARG_KEY_INDEX);
        int pageCount = viewPager.getAdapter().getCount();

        if (buttonArrowLeft != null) {
            buttonArrowLeft.setVisibility(currentPage == 0 ? View.GONE : View.VISIBLE);
        }
        if (buttonArrowRight != null) {
            buttonArrowRight.setVisibility(currentPage == pageCount - 1 ? View.GONE : View.VISIBLE);
        }

        if (pageLayout != null) {
            for (int i = 0; i < pageLayout.getChildCount(); i++) {
                View child = pageLayout.getChildAt(i);

                if (child != null) {
                    child.setSelected(currentPage == i);
                    child.setVisibility((index == INDEX_USE && i >= pageCount) ? View.GONE : View.VISIBLE);
                }
            }
        }
    }
}
