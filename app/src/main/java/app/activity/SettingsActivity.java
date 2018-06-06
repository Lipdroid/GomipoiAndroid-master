package app.activity;

import android.os.Bundle;
import android.view.View;

import com.topmission.gomipoi.R;

import app.fragment.SettingsFragment;
import common.activity.GBActivityBase;

/**
 * 設定画面のActivityクラス
 */
public class SettingsActivity extends GBActivityBase {

    SettingsFragment fragment;

    // ------------------------------
    // Override
    // ------------------------------
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_common);
    }

    @Override
    public void onBackPressed() {
        if (fragment.webView.getVisibility() == View.VISIBLE) {
            fragment.webView.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreateHandler(Bundle bundler) {
        super.onCreateHandler(bundler);
        fragment = SettingsFragment.newInstance();
        setFirstFragment(
                fragment,
                SettingsFragment.getName(getApplicationContext()));
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.layoutFragment;
    }

    @Override
    protected int getResizeBaseViewId() {
        return R.id.layoutFragment;
    }
}
