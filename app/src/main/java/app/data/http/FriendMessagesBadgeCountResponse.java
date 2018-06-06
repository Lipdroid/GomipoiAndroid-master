package app.data.http;

import java.io.Serializable;
import java.util.HashMap;

import lib.json.JsonUtils;

/**
 */
public class FriendMessagesBadgeCountResponse implements Serializable {

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
    public static final String API = "friend_messages/badge_count";

    // ------------------------------
    // Member
    // ------------------------------
    public int unreadCount;

    // ------------------------------
    // Constructor
    // ------------------------------
    public FriendMessagesBadgeCountResponse(HashMap<String, Object> map) {
        this.unreadCount = JsonUtils.getIntElement(map, "unread_count");
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    public final String getTestLog() {
        return "unread_count=" + String.valueOf(unreadCount);
    }

}
