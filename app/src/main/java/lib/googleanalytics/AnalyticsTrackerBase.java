package lib.googleanalytics;

import android.content.Context;

//import com.google.android.gms.analytics.GoogleAnalytics;
//import com.google.android.gms.analytics.HitBuilders;
//import com.google.android.gms.analytics.Tracker;

/**
 *
 */
public class AnalyticsTrackerBase {

    // ------------------------------
    // Define
    // ------------------------------
//    private final Context context;

    // ------------------------------
    // Member
    // ------------------------------
//    private Tracker mTracker;

    // ------------------------------
    // Constructor
    // ------------------------------
    /**
     *
     * @param context
     */
    public AnalyticsTrackerBase(Context context) {
//        this.context = context;
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    public void onCreate() {
//        GoogleAnalytics googleAnalytics = GoogleAnalytics.getInstance(context.getApplicationContext());
//        Tracker tracker = googleAnalytics.newTracker(getTrackingId());
//        mTracker = tracker;
    }

    public void onDestroy() {
//        mTracker = null;
    }

    /**
     *
     * @param screenName
     */
    public void sendView(String screenName) {
//        if (mTracker == null) {
//            return;
//        }
//
//        mTracker.setScreenName(screenName);
//        mTracker.send(new HitBuilders.AppViewBuilder().build());
    }

    /**
     *
     * @param category
     * @param action
     * @param label
     */
    public void sendEvent(String category, String action, String label) {
//        sendEvent(category, action, label, 1);
    }

    /**
     *
     * @param category
     * @param action
     * @param label
     * @param value
     */
    public void sendEvent(String category, String action, String label, long value) {
//        if (mTracker == null) {
//            return;
//        }
//
//        mTracker.send(new HitBuilders.EventBuilder().
//                setCategory(category).setAction(action).setLabel(label).setValue(value).build());
    }

    // ------------------------------
    // Function
    // ------------------------------
    protected String getTrackingId() {
        return "";
    }

}
