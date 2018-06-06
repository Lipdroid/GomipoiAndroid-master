package app.activity;

import android.os.Bundle;

import com.topmission.gomipoi.R;

import app.fragment.HelpFragment;
import common.activity.GBActivityBase;

/**
 * ヘルプ画面のActivityクラス
 */
public class HelpActivity extends GBActivityBase {

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_common);
    }

    @Override
    protected void onCreateHandler(Bundle bundler) {
        super.onCreateHandler(bundler);
        setFirstFragment(
                HelpFragment.newInstance(),
                HelpFragment.getName(getApplicationContext()));
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
