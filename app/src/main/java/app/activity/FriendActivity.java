package app.activity;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.topmission.gomipoi.R;

import app.animation.button.ButtonAnimationManager;
import app.application.GBApplication;
import app.define.SeData;
import app.fragment.FriendListFragment;
import app.fragment.FriendListRankFragment;
import app.interfaces.FriendListRankListener;
import common.activity.GBActivityBase;
import lib.log.DebugLog;

/**
 * ショップ画面のActivityクラス
 */
public class FriendActivity extends GBActivityBase implements FriendListRankListener {

    private FriendListFragment mFragmentFriendList;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_common);
    }

    @Override
    protected void onCreateHandler(Bundle bundler) {
        super.onCreateHandler(bundler);

        mFragmentFriendList = FriendListFragment.newInstance();
        setFirstFragment(mFragmentFriendList, FriendListFragment.getName(getApplicationContext()));

        DebugLog.i("FriendActivity - onCreateHandler called");
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.layoutFragment;
    }

    @Override
    protected int getResizeBaseViewId() {
        return R.id.layoutFragment;
    }

    @Override
    public void onBackPressed() {
        DebugLog.i("FriendActivity - onBackPressed called");
        if (getCurrentActiveFragment() instanceof FriendListRankFragment) {
            this.replaceFragment(FriendListFragment.newInstance());
        }
        else {
            DebugLog.i("FriendActivity - onBackPressed super called");
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        DebugLog.i("FriendActivity - onResume called");
    }

    @Override
    public void replaceFragment(Fragment fragment) {
        DebugLog.i("FriendActivity - replaceFragment called");
        if (fragment == null) {
            onBackPressed();
        } else {
            if (fragment instanceof FriendListFragment) {
                mFragmentFriendList = (FriendListFragment) fragment;
            } else {
                mFragmentFriendList = null;
            }
            setFirstFragment(fragment, fragment.toString());
        }
    }
}
