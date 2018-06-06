package app.data.http;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lib.json.JsonUtils;

/**
 */
public class FriendsForPresentRequestResponse implements Serializable {

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
    public static final String API = "friends/for_present_request";

    // ------------------------------
    // Member
    // ------------------------------
    public int userId;
    public String nickname;
    public int canPresentRequest;

    // ------------------------------
    // Constructor
    // ------------------------------
    public FriendsForPresentRequestResponse(HashMap<String, Object> map) {
        this.userId = JsonUtils.getIntElement(map, "user_id");
        this.nickname = JsonUtils.getStringElement(map, "nickname");
        this.canPresentRequest = JsonUtils.getIntElement(map, "can_present_request");
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    public static List<Object> getList(HashMap<String, Object> jsonData) {
        if (jsonData == null) {
            return new ArrayList<>();
        }

        return (List<Object>)jsonData.get("friends");
    }

    public final boolean canPresentRequest() {
        return canPresentRequest != 0;
    }

    public final String getTestLog() {
        return "nickname=" + nickname + "\nuserId=" + String.valueOf(userId);
    }

}
