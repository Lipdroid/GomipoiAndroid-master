package lib.qrcode;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import app.define.DebugMode;

/**
 *
 * Created by kazuya on 2017/09/28.
 */
public class QRCodeUtil {
    private static final String TEST_SERVER_SUFFIX = "com.app-daydelight.dev-prize-exchange";
    private static final String STAGING_SERVER_SUFFIX = "com.top-mission-app.stg-prize-exchange";
    private static final String PRODUCTION_SERVER_SUFFIX = "com.top-mission-app.prize-exchange";

    /**
     * generate suffix string
     * @return qr-code suffix
     */
    @NonNull
    private static String suffix() {
        String suffix = PRODUCTION_SERVER_SUFFIX;
        if (DebugMode.isTestServer) {
            suffix = TEST_SERVER_SUFFIX;
        }
        if (DebugMode.isStagingServer) {
            suffix = STAGING_SERVER_SUFFIX;
        }

        return "." + suffix;
    }
    /**
     * create qr-code string ( friend code + suffix )
     * @param friendCode friendCode
     * @return qr-code string
     */
    public static String qrCodeString(@NonNull String friendCode) {
        String suffix = suffix();

        return friendCode + suffix;
    }


    /**
     * find friend code. If not found ALC suffix, return null.
     * @param qrCodeString qr-code string
     * @return friend code
     */
    @Nullable
    public static String friendCode(@Nullable String qrCodeString) {
        if (android.text.TextUtils.isEmpty(qrCodeString)) {
            return null;
        }

        String suffix = suffix();
        if (!qrCodeString.endsWith(suffix)) {
            return null;
        }

        return qrCodeString.replace(suffix, "");
    }
}
