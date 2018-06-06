package app.data.http;

import java.io.Serializable;
import java.util.HashMap;

import lib.json.JsonUtils;

/**
 */
public class UserGuestRegisterResponce implements Serializable {

    // ------------------------------
    // Enum
    // ------------------------------
    public static enum ResultCode implements Serializable {
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
    public static final String API = "users/guest_register";

    // ------------------------------
    // Member
    // ------------------------------
    public String account;
    public String password;

    // ------------------------------
    // Constructor
    // ------------------------------
    public UserGuestRegisterResponce(HashMap<String, Object> jsonData) {

        if (jsonData.get("account") != null) {
            this.account = JsonUtils.getStringElement(jsonData,"account");
        }

        if (jsonData.get("password") != null) {
            this.password = JsonUtils.getStringElement(jsonData, "password");
        }
    }

    public UserGuestRegisterResponce() {
        account = "";
        password = "";
    }

    // ------------------------------
    // Accesser
    // ------------------------------

}
