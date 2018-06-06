package app.data.http;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import app.billing.util.Purchase;

/**
 *
 */
public class UserAppJewelParam implements Serializable {

    // ------------------------------
    // Enum
    // ------------------------------
    public enum ResultCode implements Serializable {
        UNKNOWN(-1),
        OK(0),
        Shortage(1),
        ;

        private int mValue;

        ResultCode(int mValue) {
            this.mValue = mValue;
        }

        public int getValue() {
            return mValue;
        }

        public static ResultCode valueOf(int value) {
            for (ResultCode code : values()) {
                if (code.getValue() == value) {
                    return code;
                }
            }
            return UNKNOWN;
        }
    }

    public enum Method implements Serializable {
        UNKNOWN(-1),
        Shop(0),
        PicturePoi(1),
        PictureBookPageComplete(2),
        Restore(3),
        ;

        private int mValue;

        Method(int mValue) {
            this.mValue = mValue;
        }

        public int getValue() {
            return mValue;
        }

        public static Method valueOf(int value) {
            for (Method code : values()) {
                if (code.getValue() == value) {
                    return code;
                }
            }
            return UNKNOWN;
        }
    }

    // ------------------------------
    // Define
    // ------------------------------
    public static final String API = "user_apps/jewel";

    // ------------------------------
    // Member
    // ------------------------------
    public Method method;
    public int add_jewel_count;
    // 写真ぽい用
    public int newPoiCount;
    // 課金(Shop用)
    public Purchase boughtInfo;
    // 課金(Restore用)
    public ArrayList<Purchase> restoreInfos;

    // ------------------------------
    // Constructor
    // ------------------------------
    public UserAppJewelParam(Method method, int addJwelCount) {
        this.method = method;
        this.add_jewel_count = addJwelCount;
    }

    public UserAppJewelParam(Method method, int addJwelCount, int newPoiCount) {
        this.method = method;
        this.add_jewel_count = addJwelCount;
        this.newPoiCount = newPoiCount;
    }

    public UserAppJewelParam(Method method, int addJwelCount, Purchase boughtInfo) {
        this.method = method;
        this.add_jewel_count = addJwelCount;
        this.boughtInfo = boughtInfo;
    }

    public UserAppJewelParam(Method method, int addJwelCount, ArrayList<Purchase> restoreInfos) {
        this.method = method;
        this.add_jewel_count = addJwelCount;
        this.restoreInfos = restoreInfos;
    }

    public UserAppJewelParam(Method method, int addJwelCount, int newPoiCount, Purchase boughtInfo, ArrayList<Purchase> restoreInfos) {
        this.method = method;
        this.add_jewel_count = addJwelCount;
        this.newPoiCount = newPoiCount;
        this.boughtInfo = boughtInfo;
        this.restoreInfos = restoreInfos;
    }

    // ------------------------------
    // Override
    // ------------------------------
    public Object cloneParam() {
        return new UserAppJewelParam(method, add_jewel_count, newPoiCount, boughtInfo, restoreInfos);
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    public HashMap<String, String> makeParam() {
        HashMap<String, String> map = new HashMap<>();
        map.put("add_jewel_count", String.valueOf(add_jewel_count));
        return map;
    }

}
