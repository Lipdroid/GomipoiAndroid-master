package app.activity;

import android.os.Bundle;

import com.topmission.gomipoi.R;

import app.fragment.ShopFragment;
import common.activity.GBActivityBase;

/**
 * ショップ画面のActivityクラス
 */
public class ShopActivity extends GBActivityBase {

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
                ShopFragment.newInstance(),
                ShopFragment.getName(getApplicationContext()));
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
