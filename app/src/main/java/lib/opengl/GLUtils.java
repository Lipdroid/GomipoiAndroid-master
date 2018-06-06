package lib.opengl;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;

/**
 * OpenGL関連Utilityクラス
 */
public class GLUtils {

    // ------------------------------
    // Accesser
    // ------------------------------
    /**
     * OpenGL ES 2.0以上を端末がサポートしているかを返す
     * @param context [ApplicationContext]
     */
    public static boolean isSupportedGLES2(Context context) {
        ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        return (configurationInfo.reqGlEsVersion >= 0x20000);
    }

}
