package app.data.http;

import org.json.JSONException;

import java.io.Serializable;
import java.util.HashMap;

import lib.json.JsonParser;
import lib.json.JsonUtils;

/**
 */
public class NotificationsCheckResponse implements Serializable {

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
    public static final String API = "notifications/check";

    // ------------------------------
    // Member
    // ------------------------------
    public boolean unread;

    // ------------------------------
    // Constructor
    // ------------------------------
    public NotificationsCheckResponse(HashMap<String, Object> jsonData) {
        this.unread = JsonUtils.getBooleanElement(jsonData, "unread");
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    public String getTestLog() {
        return "unread=" + unread;
    }
}
