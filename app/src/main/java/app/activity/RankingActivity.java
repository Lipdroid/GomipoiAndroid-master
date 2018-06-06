package app.activity;

import android.os.Bundle;

import com.topmission.gomipoi.R;

import app.fragment.RankingFragment;
import common.activity.GBActivityBase;

/**
 * ランキング画面のActivityクラス
 */
public class RankingActivity extends GBActivityBase {

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
        setFirstFragment(RankingFragment.newInstance(), RankingFragment.getName(getApplicationContext()));
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
