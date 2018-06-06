package app.data.http;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Herve on 2016/09/13.
 */
public class FriendshipBonusParam implements Serializable {

    // ------------------------------
    // Enum
    // ------------------------------
    public enum ResultCode implements Serializable {
        UNKNOWN(-1),
        OK(0),
        EXPIRATION(1), // 期限切れ
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
    public static final String API = "friendship_bonuses/receive";

    // ------------------------------
    // Member
    // ------------------------------
    public int systemMessageId;

    // ------------------------------
    // Constructor
    // ------------------------------
    public FriendshipBonusParam(int systemMessageId) {
        this.systemMessageId = systemMessageId;
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    public HashMap<String, String> makeParam() {
        HashMap<String, String> map = new HashMap<>();
        map.put("system_message_id", String.valueOf(systemMessageId));
        return map;
    }

}
