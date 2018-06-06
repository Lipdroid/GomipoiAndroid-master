package lib.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 使用時にはManifestに"<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />"の追加が必須
 */
public class NetworkUtils {

    // ------------------------------
    // Accesser
    // ------------------------------
    /**
     * ネットワークが使用可能かを返す
     * @param context [ApplicationContext]
     */
    public static boolean isEnabled(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (!(networkInfo == null || !networkInfo.isConnected()));
    }


}
