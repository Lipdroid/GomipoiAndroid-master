package app.activity;

import android.os.Bundle;

import com.topmission.gomipoi.R;

import app.fragment.ShowQRFragment;
import common.activity.GBActivityBase;

/**
 *
 * Created by kazuya on 2017/10/06.
 */

public class ShowQRActivity extends GBActivityBase {
    public static final String FRIEND_CODE_KEY = "friendCode";

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_common);
    }

    @Override
    protected void onCreateHandler(Bundle bundler) {
        super.onCreateHandler(bundler);
        setFirstFragment(ShowQRFragment.newInstance(getIntent().getStringExtra(FRIEND_CODE_KEY)),
                ShowQRFragment.class.getSimpleName());
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
