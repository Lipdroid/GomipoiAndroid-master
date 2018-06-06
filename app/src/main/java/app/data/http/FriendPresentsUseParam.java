package app.data.http;

import java.io.Serializable;
import java.util.HashMap;

/**
 */
public class FriendPresentsUseParam implements Serializable {

    // ------------------------------
    // Enum
    // ------------------------------
    public enum ResultCode implements Serializable {
        UNKNOWN(-1),
        OK(0),
        EXPIRATION(1), // 期限切れのため受け取れない
        NO_EFFECT(2), // プレゼント使用の効果が無いため、現在使用できない
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
    public static final String API = "friend_presents/use";

    // ------------------------------
    // Member
    // ------------------------------
    public int friendMessageId;

    // ------------------------------
    // Constructor
    // ------------------------------
    public FriendPresentsUseParam(int friendMessageId) {
        this.friendMessageId = friendMessageId;
    }

    // ------------------------------
    // Override
    // ------------------------------
    public Object cloneParam() {
        return new FriendPresentsUseParam(friendMessageId);
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    public HashMap<String, String> makeParam() {
        HashMap<String, String> map = new HashMap<>();
        map.put("friend_message_id", String.valueOf(friendMessageId));
        return map;
    }

}
