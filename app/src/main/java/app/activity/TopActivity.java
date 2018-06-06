package app.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.topmission.gomipoi.R;

import app.define.ChangeActivityCode;
import app.fragment.TopFragment;
import common.activity.GBActivityBase;

/**
 * トップ画面のActivityクラス
 */
public class TopActivity extends GBActivityBase {

    public static boolean sShouldSendDeviceToken = true;

    // ------------------------------
    // Override
    // ------------------------------
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_common);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ActivityCompat.checkSelfPermission(
                getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            initLoading();
        }
    }

    @Override
    protected void onCreateHandler(Bundle bundler) {
        super.onCreateHandler(bundler);
        setFirstFragment(
                TopFragment.newInstance(),
                TopFragment.getName(getApplicationContext()));
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
    public void onLoadServerData() {
    }

    public void onFinishLoading() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null && extras.containsKey("fromNotification")) {

            boolean isFromNotification = (Boolean) extras.get("fromNotification");
            if (isFromNotification) {
                intent.removeExtra("fromNotification");
                onChangedActivity(ChangeActivityCode.Info.getValue(), null);
            }
        }
    }
}
