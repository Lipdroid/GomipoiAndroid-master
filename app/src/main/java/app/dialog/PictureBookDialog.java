package app.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.topmission.gomipoi.R;

import java.util.List;

import app.adapter.BookAdapter;
import app.adapter.BookSpAdapter;
import app.data.BookGarbageData;
import app.define.DialogCode;
import app.define.SeData;
import common.dialog.GBDialogBase;

/**
 *
 */
public class PictureBookDialog extends GBDialogBase
        implements View.OnClickListener, ViewPager.OnPageChangeListener,
                BookAdapter.OnBookAdapterListener {

    // ------------------------------
    // Define
    // ------------------------------
    private static final String ARG_KEY_NAME = "#1#" + System.currentTimeMillis();
    private static final String ARG_KEY_ListPageType = "#2#" + System.currentTimeMillis();
    private static final int MAX_DISPLAY_PAGE = 11;

    private static final int ListPageType_Garbage = 0;
    private static final int ListPageType_Sp = 1;


    // ------------------------------
    // Member
    // ------------------------------
    private ViewPager mBookViewPager = null;
    private ImageButton mArrowLeftImageButton = null;
    private ImageButton mArrowRightImageButton = null;
    private ImageButton buttonGarbageIndex;
    private ImageButton buttonSpIndex;
    private ImageView imageViewGarbageNew;
    private ImageView imageViewNew;
    private ImageView imageViewDescript;
    private View layoutPage;
    private View layoutSpPage;

    // ------------------------------
    // NewInstance
    // ------------------------------
    public static PictureBookDialog newInstance(String name) {
        PictureBookDialog dialog = new PictureBookDialog();
        Bundle args = new Bundle();
        args.putString(ARG_KEY_NAME, name);
        args.putInt(ARG_KEY_ListPageType, ListPageType_Garbage);
        dialog.setArguments(args);
        return dialog;
    }

    // ------------------------------
    // Override
    // ------------------------------
    @Override
    public int getDialogCode() {
        return DialogCode.PictureBook.getValue();
    }

    @Override
    public String getDialogName() {
        return getArguments().getString(ARG_KEY_NAME);
    }

    @Override
    protected View createDialogLayout(Dialog dialog) {
        View view = getInflater().inflate(R.layout.dialog_picture_book, null);
        if (view == null) {
            return super.createDialogLayout(dialog);
        }

        mBookViewPager = (ViewPager) view.findViewById(R.id.viewPager);

        mArrowLeftImageButton = (ImageButton) view.findViewById(R.id.buttonArrowLeft);
        if (mArrowLeftImageButton != null) {
            mArrowLeftImageButton.setOnClickListener(this);
        }

        mArrowRightImageButton = (ImageButton) view.findViewById(R.id.buttonArrowRight);
        if (mArrowRightImageButton != null) {
            mArrowRightImageButton.setOnClickListener(this);
        }

        buttonGarbageIndex = (ImageButton) view.findViewById(R.id.buttonGarbageIndex);
        if (buttonGarbageIndex != null) {
            buttonGarbageIndex.setSelected(true);
            buttonGarbageIndex.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isEventLocked()) {
                        return;
                    }
                    lockEvent();

                    if (getArguments().getInt(ARG_KEY_ListPageType) != ListPageType_Garbage) {
                        getArguments().putInt(ARG_KEY_ListPageType, ListPageType_Garbage);
                        changeIndex();
                    }
                    unlockEvent();

                }
            });
        }

        buttonSpIndex = (ImageButton) view.findViewById(R.id.buttonSpIndex);
        if (buttonSpIndex != null) {
            buttonSpIndex.setSelected(false);
            buttonSpIndex.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isEventLocked()) {
                        return;
                    }
                    lockEvent();

                    if (getArguments().getInt(ARG_KEY_ListPageType) != ListPageType_Sp) {
                        getArguments().putInt(ARG_KEY_ListPageType, ListPageType_Sp);
                        changeIndex();
                    }
                    unlockEvent();

                }
            });
        }

        imageViewNew = (ImageView) view.findViewById(R.id.imageViewNew);
        imageViewGarbageNew = (ImageView) view.findViewById(R.id.imageViewGarbageNew);
        imageViewDescript = (ImageView) view.findViewById(R.id.imageViewDescript);
        layoutPage = view.findViewById(R.id.layoutPage);
        layoutSpPage = view.findViewById(R.id.layoutSpPage);

        changeIndex();

        sendResult(
                RESULT_OK,
                new PictureBookDialogResponse(
                        PictureBookDialogResponse.ACTION_GET_DATA,
                        null));
        return view;
    }

    @Override
    public void onDestroyView() {
        if (mArrowRightImageButton != null) {
            mArrowRightImageButton.setOnClickListener(null);
        }
        if (mArrowLeftImageButton != null) {
            mArrowLeftImageButton.setOnClickListener(null);
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
            case R.id.buttonArrowLeft:
                if (getApp() != null) {
                    getApp().getSeManager().playSe(SeData.YES);
                }
                // 左矢印ボタン
                mBookViewPager.arrowScroll(View.FOCUS_LEFT);
                unlockEvent();
                break;
            case R.id.buttonArrowRight:
                if (getApp() != null) {
                    getApp().getSeManager().playSe(SeData.YES);
                }
                // 右矢印ボタン
                mBookViewPager.arrowScroll(View.FOCUS_RIGHT);
                unlockEvent();
                break;
            default: {
                unlockEvent();
                break;
            }
        }
    }

    // ------------------------------
    // ViewPager.OnPageChangeListener
    // ------------------------------
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        setShowPage(position);
    }

    @Override
    public void onPageSelected(int position) {
        if (getApp() != null) {
            getApp().getSeManager().playSe(SeData.PAGE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    // ------------------------------
    // BookAdapter.OnBookAdapterListener
    // ------------------------------
    @Override
    public void onClickedCell(BookGarbageData data) {
        if (isEventLocked()) {
            return;
        }
        lockEvent();

        sendResult(
                RESULT_OK,
                new PictureBookDialogResponse(
                        PictureBookDialogResponse.ACTION_CLICKED_ITEM,
                        data));
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    public final void refresh() {
        if (mBookViewPager != null) {
            mBookViewPager.getAdapter().notifyDataSetChanged();
        }

        List<BookGarbageData> spDataList = BookGarbageData.getSpList();
        boolean isNew = false;
        for (BookGarbageData data : spDataList) {
            if (data.isNew()) {
                isNew = true;
                break;
            }
        }
        if (imageViewNew != null) {
            imageViewNew.setVisibility(isNew ? View.VISIBLE : View.GONE);
        }

        List<BookGarbageData> garbageDataList = BookGarbageData.getList();
        isNew = false;
        for (BookGarbageData data : garbageDataList) {
            if (data.isNew()) {
                isNew = true;
                break;
            }
        }
        if (imageViewGarbageNew != null) {
            imageViewGarbageNew.setVisibility(isNew ? View.VISIBLE : View.GONE);
        }
    }

    // ------------------------------
    // function
    // ------------------------------
    /**
     * ViewPagerの設定をする
     */
    private void setBookViewPager() {
        // 図鑑のリストを読み込み
        List<BookGarbageData> garbageDataList = BookGarbageData.getList();
        List<BookGarbageData> spDataList = BookGarbageData.getSpList();

        boolean isNew = false;
        for (BookGarbageData data : spDataList) {
            if (data.isNew()) {
                isNew = true;
                break;
            }
        }
        if (imageViewNew != null) {
            imageViewNew.setVisibility(isNew ? View.VISIBLE : View.GONE);
        }

        isNew = false;
        for (BookGarbageData data : garbageDataList) {
            if (data.isNew()) {
                isNew = true;
                break;
            }
        }
        if (imageViewGarbageNew != null) {
            imageViewGarbageNew.setVisibility(isNew ? View.VISIBLE : View.GONE);
        }

        if (getArguments().getInt(ARG_KEY_ListPageType) == ListPageType_Garbage) {
            BookAdapter adapter = new BookAdapter(getApplicationContext(), garbageDataList, this);
            mBookViewPager.setAdapter(adapter);
            mBookViewPager.addOnPageChangeListener(this);
        } else {
            BookSpAdapter adapter = new BookSpAdapter(getApplicationContext(), spDataList, this);
            mBookViewPager.setAdapter(adapter);
            mBookViewPager.addOnPageChangeListener(this);
        }

    }

    private void changeIndex() {
        int type = getArguments().getInt(ARG_KEY_ListPageType);

        if (buttonGarbageIndex != null) {
            buttonGarbageIndex.setSelected(type == ListPageType_Garbage);
        }
        if (buttonSpIndex != null) {
            buttonSpIndex.setSelected(type == ListPageType_Sp);
        }
        if (imageViewDescript != null) {
            imageViewDescript.setVisibility(type == ListPageType_Garbage ? View.VISIBLE : View.INVISIBLE);
        }
        if (layoutPage != null) {
            layoutPage.setVisibility(type == ListPageType_Garbage ? View.VISIBLE : View.INVISIBLE);
        }
        if (layoutSpPage != null) {
            layoutSpPage.setVisibility(type == ListPageType_Sp ? View.VISIBLE : View.INVISIBLE);
        }

        setBookViewPager();
        setShowPage(0);
    }

    /**
     * ViewPagerのページに応じた番号、矢印の状態にする
     */
    private void setShowPage(int position) {
        if (getActivity() == null || getDialog() == null) {
            return;
        }

        if (mBookViewPager == null || mBookViewPager.getAdapter() == null)
            return;

        int maxPage = mBookViewPager.getAdapter().getCount();

        if (mArrowLeftImageButton != null) {
            mArrowLeftImageButton.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
        }

        if (mArrowRightImageButton != null) {
            mArrowRightImageButton.setVisibility(position == maxPage - 1 ? View.GONE : View.VISIBLE);
        }

        int startOffset = 0;
        if (maxPage > MAX_DISPLAY_PAGE) {
            //ページのオフセットを設定する
            int centerNumber = (MAX_DISPLAY_PAGE + 1) / 2;
            if (position < centerNumber) {
                startOffset = 0;
            }
            else if (position >= maxPage - centerNumber) {
                startOffset = maxPage - MAX_DISPLAY_PAGE;
            }
            else {
                startOffset = position - (centerNumber - 1);
            }
        }

        int totalPage = Math.min(maxPage, MAX_DISPLAY_PAGE);

        for (int i = 1; i <= totalPage; i++) {
            String idString = getArguments().getInt(ARG_KEY_ListPageType) == ListPageType_Garbage ? "imageViewPage" + i : "imageViewSpPage" + i;
            int resourceId = getResources().getIdentifier(idString, "id", getActivity().getPackageName());
            ImageView selectItemImageView = (ImageView) getDialog().findViewById(resourceId);

            if (selectItemImageView != null) {
                selectItemImageView.setSelected(startOffset + i - 1 == position);

                int imageResourceId = getResources().getIdentifier("selector_book_page_" + (startOffset + i), "drawable", getActivity().getPackageName());
                selectItemImageView.setImageResource(imageResourceId);
            }
        }
    }

    // ------------------------------
    // Inner-Class
    // ------------------------------
    public static class PictureBookDialogResponse {

        // ------------------------------
        // Define
        // ------------------------------
        public static final int ACTION_PAGE_CHECK_BONUS = 1;
        public static final int ACTION_CLICKED_ITEM = 2;
        public static final int ACTION_GET_DATA = 3;

        // ------------------------------
        // Member
        // ------------------------------
        public int action;
        public BookGarbageData data;

        // ------------------------------
        // Constructor
        // ------------------------------
        public PictureBookDialogResponse(int action, BookGarbageData data) {
            this.action = action;
            this.data = data;
        }
    }

}
