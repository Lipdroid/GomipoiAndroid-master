package app.activity;

import android.os.Bundle;

import com.topmission.gomipoi.R;

import app.fragment.InfoFragment;
import common.activity.GBActivityBase;

/**
 * 通知履歴画面のActivityクラス
 */
public class InfoActivity extends GBActivityBase {

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_common);
    }

    @Override
    protected void onCreateHandler(Bundle bundler) {
        super.onCreateHandler(bundler);
        setFirstFragment(
                InfoFragment.newInstance(),
                InfoFragment.getName(getApplicationContext()));
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
