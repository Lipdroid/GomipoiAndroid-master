package app.googleanalytics;

import android.content.Context;

import app.define.DebugMode;
import lib.googleanalytics.AnalyticsTrackerBase;

/**
 * GoogleAnalytics連携用のTrackerクラス
 */
public class JPTracker extends AnalyticsTrackerBase {

    // ------------------------------
    // Constructor
    // ------------------------------
    public JPTracker(Context context) {
        super(context);
    }

    // ------------------------------
    // Override
    // ------------------------------
    @Override
    protected String getTrackingId() {
        return DebugMode.isSendTestAnalytics ? "UA-61641168-1" : "";
    }
}
