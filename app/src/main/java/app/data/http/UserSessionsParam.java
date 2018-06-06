package app.data.http;

import java.io.Serializable;
import java.util.HashMap;

/**
 *
 */
public class UserSessionsParam implements Serializable {

    // ------------------------------
    // Enum
    // ------------------------------
    public enum ResultCode implements Serializable {
        UNKNOWN(-1),
        OK(0),
        NOT_MATCH_USER_INFO(1),
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

    public enum ParentActivity implements Serializable {
        UNKNOWN(-1),
        LOGIN_ACTIVITY(0),
        SETTING_ACTIVITY_GUEST_REGISTER(1),
        TOP_ACTIVITY_GUEST_REGISTER(2),
        EXCHANGE_ACTIVITY_GUEST_REGISTER(3),
        OTHER_ACTIVITY(4),
        ;

        private int mValue;

        ParentActivity(int mValue) {
            this.mValue = mValue;
        }

        public int getValue() {
            return mValue;
        }

        public static ParentActivity valueOf(int value) {
            for (ParentActivity code : values()) {
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
    public static final String API = "user_sessions";

    // ------------------------------
    // Member
    // ------------------------------
    public ParentActivity parentActivity;
    public String account;
    public String password;
//    public String deviceToken;

    // ------------------------------
    // Constructor
    // ------------------------------
    public UserSessionsParam(ParentActivity parent, String account, String password, String deviceToken) {
        this.parentActivity = parent;
        this.account = account;
        this.password = password;
//        this.deviceToken = deviceToken;
    }

    // ------------------------------
    // Override
    // ------------------------------
    public Object cloneParam() {
        return new UserSessionsParam(parentActivity, account, password, null/*deviceToken*/);
    }

    // ------------------------------
    // Accessor
    // ------------------------------
    public HashMap<String, String> makeParam() {
        HashMap<String, String> map = new HashMap<>();
        map.put("account", String.valueOf(account));
        map.put("password", String.valueOf(password));

//        if (deviceToken != null)
//            map.put("device_token", String.valueOf(deviceToken));
        return map;
    }

    public final boolean isCalledLoginActivity() {
        return parentActivity.equals(ParentActivity.LOGIN_ACTIVITY);
    }

}
