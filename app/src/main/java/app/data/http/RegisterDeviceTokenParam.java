package app.data.http;

import java.io.Serializable;
import java.util.HashMap;

import app.data.ListItemData;

/**
 * Created by Herve on 2016/10/18.
 */
public class RegisterDeviceTokenParam implements Serializable {

    // ------------------------------
    // Enum
    // ------------------------------
    public enum ResultCode implements Serializable {
        UNKNOWN(-1),
        OK(0)
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
    public static final String API = "users/register_device_token";

    // ------------------------------
    // Member
    // ------------------------------
    public String deviceToken;

    // ------------------------------
    // Constructor
    // ------------------------------
    public RegisterDeviceTokenParam(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    // ------------------------------
    // Override
    // ------------------------------
    public Object cloneParam() {
        return new RegisterDeviceTokenParam(deviceToken);
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    public HashMap<String, String> makeParam() {
        HashMap<String, String> map = new HashMap<>();
        map.put("device_token", deviceToken);
        return map;
    }
}
