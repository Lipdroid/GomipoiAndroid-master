package app.data.http;

import java.io.Serializable;
import java.util.HashMap;

/**
 */
public class FriendInvitationsParam implements Serializable {

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
    public static final String API = "friend_invitations";

    // ------------------------------
    // Member
    // ------------------------------
    public int toUserId;

    // ------------------------------
    // Constructor
    // ------------------------------
    public FriendInvitationsParam(int toUserId) {
        this.toUserId = toUserId;
    }

    // ------------------------------
    // Override
    // ------------------------------
    public Object cloneParam() {
        return new FriendInvitationsParam(toUserId);
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
