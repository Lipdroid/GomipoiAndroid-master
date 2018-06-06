package com.topmission.gomipoi;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import app.activity.TopActivity;
import app.activity.TutorialActivity;
import app.application.GBApplication;
import app.define.DebugMode;
import lib.activity.FragmentActivityBase;
import lib.timer.OnWaitTimerListener;
import lib.timer.WaitTimer;

public class SplashActivity extends FragmentActivityBase implements OnWaitTimerListener {

    // ------------------------------
    // Define
    // ------------------------------
    private final long WAIT_INTERVAL = 3000L;

    // ------------------------------
    // Member
    // ------------------------------
    private WaitTimer mWaitTimer;

    // ------------------------------
    // Override
    // ------------------------------
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        setContentView(R.layout.activity_splash);

        final View layoutMain = findViewById(R.id.layoutMain);
        layoutMain.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    layoutMain.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    layoutMain.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                ((GBApplication) getApplication()).setDeviceSize(layoutMain.getWidth(), layoutMain.getHeight());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWaitTimer().start();
    }

    @Override
    protected void onPause() {
        getWaitTimer().stop();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mWaitTimer != null) {
            mWaitTimer.stop();
            mWaitTimer = null;
        }
        super.onDestroy();
    }

    @Override
    protected boolean isHiddenActionBar() {
        return true;
    }

    @Override
    protected boolean isKeepScreenOn() {
        return true;
    }

    // ------------------------------
    // OnWaitTimerListener
    // ------------------------------
    @Override
    public void onCompletedTimer(Object data) {
        if (isLockedEvent()) {
            return;
        }
        lockEvent();

        if (DebugMode.isHaveToShowTutorial) {
            startActivity(new Intent(getApplicationContext(), TutorialActivity.class));
            this.finish();
            return;
        }

        // 初回起動かどうかで繊維先をふり分ける
        if (((GBApplication) getApplication()).getPreferenceManager().isNeedTutorial()) {
            startActivity(new Intent(getApplicationContext(), TutorialActivity.class));
        } else {
            Intent intent = new Intent(getApplicationContext(), TopActivity.class);
            Bundle extras = getIntent().getExtras();
            boolean isFromNotification = false;
            if (extras != null && extras.containsKey("from")) {
                isFromNotification = true;
            } else {
                isFromNotification = false;
            }

            intent.putExtra("fromNotification", isFromNotification);

            startActivity(intent);
        }

        this.finish();
    }

    // ------------------------------
    // Accesser
    // ------------------------------

    // ------------------------------
    // Function
    // ------------------------------
    /**
     * タイマーのインスタンスを返す
     */
    private WaitTimer getWaitTimer() {
        if (mWaitTimer == null) {
            mWaitTimer = new WaitTimer(WAIT_INTERVAL, this);
        }
        return mWaitTimer;
    }

}
