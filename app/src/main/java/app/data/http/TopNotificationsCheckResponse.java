package app.data.http;

import java.io.Serializable;
import java.util.HashMap;

import lib.json.JsonUtils;

/**
 */
public class TopNotificationsCheckResponse implements Serializable {

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
    public static final String API = "top_notifications/check";

    // ------------------------------
    // Member
    // ------------------------------
    public boolean notification_unread;
    public boolean friend_unread;

    // ------------------------------
    // Constructor
    // ------------------------------
    public TopNotificationsCheckResponse(HashMap<String, Object> jsonData) {
        this.notification_unread = JsonUtils.getBooleanElement(jsonData, "notification_unread");
        this.friend_unread = JsonUtils.getBooleanElement(jsonData, "friend_unread");
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    public String getTestLog() {
        return "notification_unread=" + notification_unread + ", friend_unread=" + friend_unread;
    }
}
