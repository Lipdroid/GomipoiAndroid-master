package app.preference;

import android.content.Context;

import app.define.GarbageId;
import app.define.ItemId;
import app.jni.JniBridge;
import common.activity.GBActivityBase;
import lib.log.DebugLog;
import lib.preference.PreferenceBaseManager;

/**
 * SharedPreferenceの管理クラス
 */
public class GBPreferenceManager extends PreferenceBaseManager {

    // ------------------------------
    // Define
    // ------------------------------
    private final String PREF_KEY_SOUND_ENABLED = PJniBridge.nativeGetKeyEnabledBgm();
    private final String PREF_KEY_SE_ENABLED = PJniBridge.nativeGetKeyEnabledSe();
    private final String PREF_KEY_USER_ID = PJniBridge.nativeGetKeyUserId();
    private final String PREF_KEY_USER_PASSWORD = PJniBridge.nativeGetKeyUserPassword();
    private final String PREF_KEY_PICTURE_POI_COUNT = PJniBridge.nativeGetKeyPicPoiCount();
    private final String PREF_KEY_PICTURE_POI_Date = PJniBridge.nativeGetKeyPicPoiDate();

    private final String PREF_KEY_BONUS_START_TIME = PJniBridge.nativeGetKeyBonusStartTime();
    private final String PREF_KEY_ADD_GARBAGE_START_TIME = PJniBridge.nativeGetKeyAddGarbageStartTime();
    private final String PREF_KEY_HERO_DRINK_APPEAR_TIME = PJniBridge.nativeGetKeyHeroDrinkAppearTime();
    private final String PREF_KEY_Z_DRINK_START_TIME = PJniBridge.nativeGetKeyZDrinkStartTime();
    private final String PREF_KEY_DROP_START_TIME = PJniBridge.nativeGetKeyDropStartTime();

    private final String PREF_KEY_UUID = "PrefKey#DEVICE_ID";
    private final String PREF_KEY_DEVICE_TOKEN = "PrefKey#DEVICE_TOKEN"; //Firebase通知用
    private final String PREF_KEY_DEVICE_TOKEN_NEW = "PrefKey#DEVICE_TOKEN_NEW"; //Firebase通知用

    private final String PREF_KEY_NOTICE1_HIDDEN = "PrefKey#Notice1Hidden";
    private final String PREF_KEY_NOTICE2_HIDDEN = "PrefKey#Notice2Hidden";
    private final String PREF_KEY_NOTICE3_HIDDEN = "PrefKey#Notice3Hidden";

    // ------------------------------
    // Constructor
    // ------------------------------
    /**
     * Constructor
     * @param context [ApplicationContext]
     */
    public GBPreferenceManager(Context context) {
        super(context);

//        for (int i = GarbageId.Slime.getValue(); i <= GarbageId.Chest.getValue(); i++) {
//            if (isGotSynthesisResult(GarbageId.valueOf(i))) {
//                JniBridge.nativeSucceedSynthesis(i);
//            }
//        }
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    // PREF_KEY_SOUND_ENABLED
    public final boolean isSoundEnabled() {
        return getBoolean(PREF_KEY_SOUND_ENABLED, true);
    }

    public final void setSoundEnabled(boolean isEnabled) {
        setValue(PREF_KEY_SOUND_ENABLED, isEnabled);
    }

    // PREF_KEY_SE_ENABLED
    public final boolean isSeEnabled() {
        return getBoolean(PREF_KEY_SE_ENABLED, true);
    }

    public final void setSeEnabled(boolean isEnabled) {
        setValue(PREF_KEY_SE_ENABLED, isEnabled);
    }


    // PREF_KEY_USER_ID
    public final String getUserId() {
        return getString(PREF_KEY_USER_ID, "");
    }

    public final void setUserId(String userId) {
        setValue(PREF_KEY_USER_ID, userId);
    }

    // PREF_KEY_USER_PASSWORD
    public final String getUserPassword() {
        return getString(PREF_KEY_USER_PASSWORD, "");
    }

    public final void setUserPassword(String userPassword) {
        setValue(PREF_KEY_USER_PASSWORD, userPassword);
    }

    public final void logout(boolean isForced) {
        if (!isForced) {
            setUserId("");
            setUserPassword("");
        }
        setHeroDrinkAppearTime(0);
        setAddGarbageStartTime(0);
        setBonusStartTime(0);
    }

    public final boolean isEnabledReceiveBonus() {
        String currentDate = JniBridge.nativeGetCurrentDate();
        String savedDate = getPicturePoiDate();
        int savedCount = getPicturePoiCount();

        return (savedDate == null
                || (currentDate != null && !currentDate.equals(savedDate))
                || (currentDate != null && currentDate.equals(savedDate) && savedCount < GBActivityBase.GEM_EXCHANGE_COUNT));
    }

    // PREF_KEY_PICTURE_POI_COUNT
    public final int getPicturePoiCount() {
        String currentDate = JniBridge.nativeGetCurrentDate();
        String savedDate = getPicturePoiDate();
        if (savedDate == null || (currentDate != null && !currentDate.equals(savedDate))) {
            return 0;
        }
        int savedCount = getInt(PREF_KEY_PICTURE_POI_COUNT, 0) % 50;
        return savedCount;
    }

    public final void setPicturePoiCount(int value) {
        setValue(PREF_KEY_PICTURE_POI_COUNT, value);
    }

    // PREF_KEY_PICTURE_POI_Date
    public final String getPicturePoiDate() {
        return getString(PREF_KEY_PICTURE_POI_Date, null);
    }

    public final void setPicturePoiDate(String date) {
        setValue(PREF_KEY_PICTURE_POI_Date, date);
    }


    /**
     * チュートリアルを表示する必要があるかを返す
     */
    public final boolean isNeedTutorial() {
        return getUserId() == null
                || getUserId().length() == 0
                || getUserPassword() == null
                || getUserPassword().length() == 0;
    }

    // PREF_KEY_BONUS_START_TIME
    public final double getBonusStartTime() {
        return getDouble(PREF_KEY_BONUS_START_TIME, 0.0);
    }
    public final void setBonusStartTime(double value) {
        setValue(PREF_KEY_BONUS_START_TIME, value);
    }

    // PREF_KEY_HERO_DRINK_APPEAR_TIME
    public final double getHeroDrinkAppearTime() {
        return getDouble(PREF_KEY_HERO_DRINK_APPEAR_TIME, 0.0);
    }
    public final void setHeroDrinkAppearTime(double value) {
        setValue(PREF_KEY_HERO_DRINK_APPEAR_TIME, value);
    }

    // PREF_KEY_Z_DRINK_START_TIME
    public final double getZDrinkStartTime() {
        return getDouble(PREF_KEY_Z_DRINK_START_TIME, 0.0);
    }
    public final void setZDrinkStartTime(double value) {
        setValue(PREF_KEY_Z_DRINK_START_TIME, value);
    }

    // PREF_KEY_DROP_START_TIME
    public final double getDropStartTime() {
        return getDouble(PREF_KEY_DROP_START_TIME, 0.0);
    }
    public final void setDropStartTime(double value) {
        setValue(PREF_KEY_DROP_START_TIME, value);
    }

    // PREF_KEY_ADD_GARBAGE_START_TIME
    public final double getAddGarbageStartTime() {
        return getDouble(PREF_KEY_ADD_GARBAGE_START_TIME, 0.0);
    }
    public final void setAddGarbageStartTime(double value) {
        setValue(PREF_KEY_ADD_GARBAGE_START_TIME, value);
    }

    // PREF_KEY_UUID
    public final String getUuid() {
        return getString(PREF_KEY_UUID, null);
    }

    public final void setUuid(String value) {
        setValue(PREF_KEY_UUID, value);
    }

    // PREF_KEY_DEVICE_TOKEN
    public final String getDeviceToken() {
        return getString(PREF_KEY_DEVICE_TOKEN, null);
    }

    public final void setDeviceToken(String value) {
        setValue(PREF_KEY_DEVICE_TOKEN, value);
    }

    // PREF_KEY_DEVICE_TOKEN
    public final boolean getDeviceTokenNew() {
        return getBoolean(PREF_KEY_DEVICE_TOKEN_NEW, true);
    }

    public final void setDeviceTokenNew(boolean value) {
        setValue(PREF_KEY_DEVICE_TOKEN_NEW, value);
    }

    // PREF_KEY_NOTICE1_HIDDEN
    public final boolean getNotice1Hidden() {
        return getBoolean(PREF_KEY_NOTICE1_HIDDEN, false);
    }

    public final void setNotice1Hidden(boolean value) {
        setValue(PREF_KEY_NOTICE1_HIDDEN, value);
    }

    // PREF_KEY_NOTICE2_HIDDEN
    public final boolean getNotice2Hidden() {
        return getBoolean(PREF_KEY_NOTICE2_HIDDEN, false);
    }

    public final void setNotice2Hidden(boolean value) {
        setValue(PREF_KEY_NOTICE2_HIDDEN, value);
    }

    // PREF_KEY_NOTICE3_HIDDEN
    public final boolean getNotice3Hidden() {
        return getBoolean(PREF_KEY_NOTICE3_HIDDEN, false);
    }

    public final void setNotice3Hidden(boolean value) {
        setValue(PREF_KEY_NOTICE3_HIDDEN, value);
    }

}
