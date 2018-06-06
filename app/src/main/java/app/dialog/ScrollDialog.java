package app.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.topmission.gomipoi.R;

import app.data.http.GomipoiGarbageRecipeParam;
import app.define.DialogCode;
import app.define.SeData;
import app.pager.ScrollPagerAdapter;
import common.dialog.GBDialogBase;

/**
 *
 */
public class ScrollDialog extends GBDialogBase
        implements View.OnClickListener, ViewPager.OnPageChangeListener {

    // ------------------------------
    // Define
    // ------------------------------
    public static final int ACTION_GET_DATA = 1;
    public static final int RECIPE_MAX_PAGE = 7;

    private static final String ARG_KEY_NAME = "#1#" + System.currentTimeMillis();
    private static final String ARG_KEY_PAGE = "#2#" + System.currentTimeMillis();

    // ------------------------------
    // Member
    // ------------------------------
    private ImageButton buttonArrowLeft;
    private ImageButton buttonArrowRight;
    private ViewPager viewPager;
    private LinearLayout layoutPage;

    // ------------------------------
    // NewInstance
    // ------------------------------
    public static ScrollDialog newInstance(String name) {
        ScrollDialog dialog = new ScrollDialog();
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
        return DialogCode.Scroll.getValue();
    }

    @Override
    public String getDialogName() {
        return getArguments().getString(ARG_KEY_NAME);
    }

    @Override
    protected View createDialogLayout(Dialog dialog) {
        View view = getInflater().inflate(R.layout.dialog_scroll, null);
        if (view == null) {
            return super.createDialogLayout(dialog);
        }

        buttonArrowLeft = (ImageButton) view.findViewById(R.id.buttonArrowLeft);
        if (buttonArrowLeft != null) {
            buttonArrowLeft.setOnClickListener(this);
        }

        buttonArrowRight = (ImageButton) view.findViewById(R.id.buttonArrowRight);
        if (buttonArrowRight != null) {
            buttonArrowRight.setOnClickListener(this);
        }

        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        if (viewPager != null) {
            viewPager.setAdapter(null);
            viewPager.addOnPageChangeListener(this);
        }

        layoutPage = (LinearLayout) view.findViewById(R.id.layoutPage);
        if (layoutPage != null) {
            makePageViews();
        }

        sendResult(RESULT_OK, ACTION_GET_DATA);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshPage();
    }

    @Override
    public void onDestroyView() {
        if (buttonArrowLeft != null) {
            buttonArrowLeft.setOnClickListener(null);
        }

        if (buttonArrowRight != null) {
            buttonArrowRight.setOnClickListener(null);
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
    public void setData(GomipoiGarbageRecipeParam data) {
        if (viewPager != null) {
            viewPager.setAdapter(new ScrollPagerAdapter(getApplicationContext(), data.mRecipeList));
        }
        refreshPage();
    }

    // ------------------------------
    // Function
    // ------------------------------
    private void makePageViews() {
        int height = layoutPage.getLayoutParams().height;
        int margin = 8 * height / 16;

        for (int i = 0; i < RECIPE_MAX_PAGE; i++) {
            ImageView imageView = new ImageView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(height, height);
            params.rightMargin = (i == RECIPE_MAX_PAGE - 1) ? 0 : margin;

            imageView.setImageResource(R.drawable.selector_item_page);
            layoutPage.addView(imageView, params);
        }
    }

    /**
     * ページを更新する
     */
    private void refreshPage() {
        int currentPage = getArguments().getInt(ARG_KEY_PAGE);

        if (buttonArrowLeft != null) {
            buttonArrowLeft.setVisibility(currentPage == 0 ? View.GONE : View.VISIBLE);
        }
        if (buttonArrowRight != null) {
            if (viewPager != null && viewPager.getAdapter() != null) {
                buttonArrowRight.setVisibility(currentPage == viewPager.getAdapter().getCount() - 1 ? View.GONE : View.VISIBLE);
            } else {
                buttonArrowRight.setVisibility(View.GONE);
            }
        }

        if (layoutPage != null) {
            if (viewPager != null && viewPager.getAdapter() != null) {
                int pageCount = viewPager.getAdapter().getCount();

                int childCount = layoutPage.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    layoutPage.getChildAt(i).setVisibility(pageCount > i ? View.VISIBLE : View.GONE);
                    layoutPage.getChildAt(i).setSelected(currentPage == i);
                }

            }
            else {
                int childCount = layoutPage.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    layoutPage.getChildAt(i).setVisibility(View.GONE);
                }
            }
        }
    }
}
