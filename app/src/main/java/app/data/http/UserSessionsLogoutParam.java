package app.data.http;

import java.io.Serializable;
import java.util.HashMap;

/**
 */
public class UserSessionsLogoutParam implements Serializable {

    // ------------------------------
    // Enum
    // ------------------------------
    public enum ResultCode implements Serializable {
        UNKNOWN(-1),
        OK(0),
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

    // ------------------------------
    // Define
    // ------------------------------
    public static final String API = "user_sessions";

    // ------------------------------
    // Member
    // ------------------------------

    // ------------------------------
    // Constructor
    // ------------------------------
    public UserSessionsLogoutParam() {
    }

    // ------------------------------
    // Override
    // ------------------------------
    public Object cloneParam() {
        return new UserSessionsLogoutParam();
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    public HashMap<String, String> makeParam() {
        return null;
    }

}
