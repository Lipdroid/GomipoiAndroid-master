package app.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import app.fragment.TutorialPagerFragment;


/**
 */
public class TutorialPagerAdapter extends FragmentPagerAdapter {

    // ------------------------------
    // Define
    // ------------------------------
    public static final int MAX_PAGE = 7;

    // ------------------------------
    // Member
    // ------------------------------

    // ------------------------------
    // Constructor
    // ------------------------------
    public TutorialPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    // ------------------------------
    // Override
    // ------------------------------
    @Override
    public Fragment getItem(int position) {
        return TutorialPagerFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return MAX_PAGE;
    }

    // ------------------------------
    // Function
    // ------------------------------
}
