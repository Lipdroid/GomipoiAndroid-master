package app.activity;

import android.os.Bundle;


import com.topmission.gomipoi.R;

import app.fragment.ExchangeFragment;
import common.activity.GBActivityBase;

/**
 * 景品交換画面のActivityクラス
 */
public class ExchangeActivity extends GBActivityBase {

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_common);
    }

    @Override
    protected void onCreateHandler(Bundle bundler) {
        super.onCreateHandler(bundler);
        setFirstFragment(
                ExchangeFragment.newInstance(),
                ExchangeFragment.getName(getApplicationContext()));
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
