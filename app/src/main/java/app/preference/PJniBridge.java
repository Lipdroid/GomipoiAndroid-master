package app.preference;

/**
 * Preference用のJniBridgeクラス
 */
public class PJniBridge {

    // ------------------------------
    // Native
    // ------------------------------
    public static native String nativeGetKeyEnabledSe();
    public static native String nativeGetKeyEnabledBgm();
    public static native String nativeGetBasicUsername();
    public static native String nativeGetBasicPassword();
    public static native String nativeGetKeyUserId();
    public static native String nativeGetKeyUserPassword();

    public static native String nativeGetKeyPicPoiDate();
    public static native String nativeGetKeyPicPoiCount();
    public static native String nativeGetKeyBonusStartTime();
    public static native String nativeGetKeyAddGarbageStartTime();
    public static native String nativeGetKeyHeroDrinkAppearTime();
    public static native String nativeGetKeyZDrinkStartTime();
    public static native String nativeGetKeyDropStartTime();

    public static native int nativeGetApplicationId();

    // ------------------------------
    // Function
    // ------------------------------

}
