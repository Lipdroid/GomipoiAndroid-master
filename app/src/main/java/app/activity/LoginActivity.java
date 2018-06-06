package app.activity;

import android.os.Bundle;


import com.topmission.gomipoi.R;

import app.define.ChangeActivityCode;
import app.fragment.LoginTopFragment;
import common.activity.GBActivityBase;

/**
 * ログイン画面のActivityクラス
 */
public class LoginActivity extends GBActivityBase {

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
                LoginTopFragment.newInstance(),
                LoginTopFragment.getName(getApplicationContext()));
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
    protected boolean isEnabledConnection() {
        return false;
    }

    // ------------------------------
    // OnFragmentListener
    // ------------------------------
    @Override
    public void onChangedActivity(int requestCode, Object data) {
        ChangeActivityCode code = ChangeActivityCode.valueOf(requestCode);
        switch (code) {
            case Top: {
                super.onChangedActivity(requestCode, data);
                this.finish();
                break;
            }

            default: {
                super.onChangedActivity(requestCode, data);
                break;
            }
        }
    }


}
