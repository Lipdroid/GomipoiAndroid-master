package app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.topmission.gomipoi.R;

import common.fragment.GBFragmentBase;

/**
 *
 */
public class TutorialPagerFragment extends GBFragmentBase {

    // ------------------------------
    // Define
    // ------------------------------
    private static final String ARG_KEY_PAGE = "#1#" + String.valueOf(System.currentTimeMillis());
    private static final String ARG_KEY_NAME = "#2#" + String.valueOf(System.currentTimeMillis());

    private static final int PAGE_1 = 0;
    private static final int PAGE_2 = 1;
    private static final int PAGE_3 = 2;
    private static final int PAGE_4 = 3;
    private static final int PAGE_5 = 4;
    private static final int PAGE_6 = 5;

    // ------------------------------
    // NewInstance
    // ------------------------------
    /**
     * インスタンスを返す
     */
    public static TutorialPagerFragment newInstance(int pageNo) {
        TutorialPagerFragment fragment = new TutorialPagerFragment();
        Bundle args = new Bundle();

        args.putInt(ARG_KEY_PAGE, pageNo);
        args.putString(ARG_KEY_NAME, String.valueOf(pageNo));

        fragment.setArguments(args);
        return fragment;
    }

    // ------------------------------
    // Override
    // ------------------------------
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutorial_pager, null);
        if (view == null) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        TextView textViewDescript = (TextView) view.findViewById(R.id.textViewDescript);
        if (textViewDescript != null) {
            textViewDescript.setText(getDescriptId());
        }

        ImageView imageViewTutorial = (ImageView) view.findViewById(R.id.imageViewTutorial);
        if (imageViewTutorial != null) {
            imageViewTutorial.setImageResource(getResourceId());
        }

        return view;
    }

    @Override
    protected boolean isFragmentPager() {
        return true;
    }

    // ------------------------------
    // Function
    // ------------------------------
    private int getDescriptId() {
        switch (getArguments().getInt(ARG_KEY_PAGE)) {
            case PAGE_1: {
                return R.string.tutorial_1;
            }

            case PAGE_2: {
                return R.string.tutorial_2;
            }

            case PAGE_3: {
                return R.string.tutorial_3;
            }

            case PAGE_4: {
                return R.string.tutorial_4;
            }

            case PAGE_5: {
                return R.string.tutorial_5;
            }

            case PAGE_6: {
                return R.string.tutorial_6;
            }
        }

        return R.string.tutorial_0;
    }

    private int getResourceId() {
        switch (getArguments().getInt(ARG_KEY_PAGE)) {
            case PAGE_1: {
                return R.drawable.tutorial1;
            }

            case PAGE_2: {
                return R.drawable.tutorial2;
            }

            case PAGE_3: {
                return R.drawable.tutorial3;
            }

            case PAGE_4: {
                return R.drawable.tutorial4;
            }

            case PAGE_5: {
                return R.drawable.tutorial5;
            }

            case PAGE_6: {
                return R.drawable.tutorial6;
            }
        }

        return 0;
    }

}
