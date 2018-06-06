package app.activity;

import android.os.Bundle;

import com.topmission.gomipoi.R;

import app.fragment.NewsFragment;
import common.activity.GBActivityBase;

/**
 * ヘルプ画面のActivityクラス
 */
public class NewsActivity extends GBActivityBase {

    // ------------------------------
    // Define
    // ------------------------------
    public static final String EXTRAS_KEY_URL = "#1#" + System.currentTimeMillis();

    // ------------------------------
    // Override
    // ------------------------------
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_common);
    }

    @Override
    protected void onCreateHandler(Bundle bundler) {
        super.onCreateHandler(bundler);
        setFirstFragment(
                NewsFragment.newInstance(getIntent().getStringExtra(EXTRAS_KEY_URL)),
                NewsFragment.getName(getApplicationContext()));
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
