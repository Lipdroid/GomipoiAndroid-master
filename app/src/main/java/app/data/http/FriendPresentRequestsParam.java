package app.data.http;

import java.io.Serializable;
import java.util.HashMap;

/**
 */
public class FriendPresentRequestsParam implements Serializable {

    // ------------------------------
    // Enum
    // ------------------------------
    public enum ResultCode implements Serializable {
        UNKNOWN(-1),
        OK(0),
        EXCEED_UPPER_LIMIT(1), // 回数制限によりおねだりできない
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
    public static final String API = "friend_present_requests";

    // ------------------------------
    // Member
    // ------------------------------
    public int toUserId;

    // ------------------------------
    // Constructor
    // ------------------------------
    public FriendPresentRequestsParam(int toUserId) {
        this.toUserId = toUserId;
    }

    // ------------------------------
    // Override
    // ------------------------------
    public Object cloneParam() {
        return new FriendPresentRequestsParam(toUserId);
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    public HashMap<String, String> makeParam() {
        HashMap<String, String> map = new HashMap<>();
        map.put("to_user_id", String.valueOf(toUserId));
        return map;
    }

}
