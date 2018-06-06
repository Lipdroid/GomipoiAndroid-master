package app.notification;

import android.content.Context;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import app.preference.GBPreferenceManager;

/**
 * Created by Herve on 2016/09/16.
 */
public class InstanceIDListenerService extends FirebaseInstanceIdService {

    private static InstanceIDListenerService sService = null;

    // ------------------------------
    // Member
    // ------------------------------
    private InstanceIDListenerServiceListener mListener;

    private boolean mIsRefreshed;

    // ------------------------------
    // Constructor
    // ------------------------------
    public InstanceIDListenerService() {
        mIsRefreshed = false;
        sService = this;
    }

    // ------------------------------
    // Static
    // ------------------------------
    public static InstanceIDListenerService getInstance() {
        return sService;
    }

    // ------------------------------
    // Setup
    // ------------------------------
    public void setup(InstanceIDListenerServiceListener listener) {
        mListener = listener;
    }

    public void removeListener() {
        mListener = null;
    }

    public boolean isSetup() {
        return mIsRefreshed;
    }

    public static boolean isNew(Context context) {
        GBPreferenceManager prefManager = new GBPreferenceManager(context);
        return prefManager.getDeviceTokenNew();
    }

    public static String getDeviceToken(Context context) {
        GBPreferenceManager prefManager = new GBPreferenceManager(context);
        return prefManager.getDeviceToken();
    }

    // ------------------------------
    // Methods
    // ------------------------------
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        notifyToken(refreshedToken);
    }

    private void notifyToken(String refreshedToken) {
        if (refreshedToken != null) {
            mIsRefreshed = true;

            GBPreferenceManager prefManager = new GBPreferenceManager(getApplicationContext());
            String token = prefManager.getDeviceToken();

            if (token == null || !token.equals(refreshedToken)) {
                prefManager.setDeviceToken(refreshedToken);
                prefManager.setDeviceTokenNew(true);
            }

            if (mListener != null) {
                mListener.onTokenRefresh(refreshedToken, prefManager.getDeviceTokenNew());
            }
        }
    }

    public interface InstanceIDListenerServiceListener {
        void onTokenRefresh(String token, boolean isNew);
    }
}
