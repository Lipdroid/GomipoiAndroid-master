package app.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.topmission.gomipoi.R;

import app.define.FragmentEventCode;
import app.define.SeData;
import app.pager.TutorialPagerAdapter;
import common.fragment.GBFragmentBase;

/**
 * チュートリアル画面
 */
public class TutorialFragment extends GBFragmentBase implements View.OnClickListener,
        ViewPager.OnPageChangeListener {

    // ------------------------------
    // NewInstance
    // ------------------------------
    /**
     * インスタンスを返す
     */
    public static TutorialFragment newInstance() {
        TutorialFragment fragment = new TutorialFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    // ------------------------------
    // Member
    // ------------------------------
    private ViewPager viewPager;
    private ImageButton buttonArrowLeft;
    private ImageButton buttonArrowRight;
    private ImageView imageViewPage1;
    private ImageView imageViewPage2;
    private ImageView imageViewPage3;
    private ImageView imageViewPage4;
    private ImageView imageViewPage5;
    private ImageView imageViewPage6;

    private int mCurrentPosition;

    // ------------------------------
    // Constructor
    // ------------------------------
    /**
     * Constructor
     */
    public TutorialFragment() {
        mCurrentPosition = 0;
    }

    // ------------------------------
    // Override
    // ------------------------------
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutorial, container, false);
        if (view == null) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        if (viewPager != null) {
            viewPager.setAdapter(new TutorialPagerAdapter(getChildFragmentManager()));
            viewPager.addOnPageChangeListener(this);
        }

        buttonArrowLeft = (ImageButton) view.findViewById(R.id.buttonArrowLeft);
        if (buttonArrowLeft != null) {
            buttonArrowLeft.setOnClickListener(this);
        }

        buttonArrowRight = (ImageButton) view.findViewById(R.id.buttonArrowRight);
        if (buttonArrowRight != null) {
            buttonArrowRight.setOnClickListener(this);
        }

        imageViewPage1 = (ImageView) view.findViewById(R.id.imageViewPage1);
        imageViewPage2 = (ImageView) view.findViewById(R.id.imageViewPage2);
        imageViewPage3 = (ImageView) view.findViewById(R.id.imageViewPage3);
        imageViewPage4 = (ImageView) view.findViewById(R.id.imageViewPage4);
        imageViewPage5 = (ImageView) view.findViewById(R.id.imageViewPage5);
        imageViewPage6 = (ImageView) view.findViewById(R.id.imageViewPage6);

        mCurrentPosition = 0;

        refreshPage();

        return view;
    }

    @Override
    public void onDestroyView() {
//        mButtonArrowLeftManager = null;
//        mButtonArrowRightManager = null;

        if (buttonArrowLeft != null) {
            buttonArrowLeft.setOnClickListener(null);
        }

        if (buttonArrowRight != null) {
            buttonArrowRight.setOnClickListener(null);
        }

        if (viewPager != null) {
            viewPager.clearOnPageChangeListeners();
            viewPager.setAdapter(null);
        }

        super.onDestroyView();
    }

    // ------------------------------
    // View.OnClickListener
    // ------------------------------
    @Override
    public void onClick(View v) {
        if (isLocked()) {
            return;
        }
        lockEvent();

        if (getApp() != null) {
            getApp().getSeManager().playSe(SeData.YES);
        }

        switch (v.getId()) {
            case R.id.buttonArrowLeft: {
                if (viewPager != null) {
                    mCurrentPosition -= 1;
                    viewPager.setCurrentItem(mCurrentPosition, true);
                }
                break;
            }

            case R.id.buttonArrowRight: {
                mCurrentPosition += 1;
                if (viewPager != null) {
                    viewPager.setCurrentItem(mCurrentPosition, true);
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

        if (position == TutorialPagerAdapter.MAX_PAGE - 1) {
            if (viewPager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    viewPager.stopNestedScroll();
                }
            }
            onFragmentEvent(FragmentEventCode.TutorialNext, null);
            return;
        }

        mCurrentPosition = position;
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
    // Acceser
    // ------------------------------
    /**
     * フラグメント名を返す
     * @param context [ApplicationContext]
     */
    public static String getName(Context context) {
        return context.getString(R.string.fragment_name_tutorial);
    }

    // ------------------------------
    // Function
    // ------------------------------
    /**
     * ページを更新する
     */
    private void refreshPage() {
        if (buttonArrowLeft != null) {
            buttonArrowLeft.setVisibility(mCurrentPosition == 0 ? View.GONE : View.VISIBLE);
        }
        if (imageViewPage1 != null) {
            imageViewPage1.setSelected(mCurrentPosition == 0);
        }
        if (imageViewPage2 != null) {
            imageViewPage2.setSelected(mCurrentPosition == 1);
        }
        if (imageViewPage3 != null) {
            imageViewPage3.setSelected(mCurrentPosition == 2);
        }
        if (imageViewPage4 != null) {
            imageViewPage4.setSelected(mCurrentPosition == 3);
        }
        if (imageViewPage5 != null) {
            imageViewPage5.setSelected(mCurrentPosition == 4);
        }
        if (imageViewPage6 != null) {
            imageViewPage6.setSelected(mCurrentPosition == 5);
        }
    }

}
