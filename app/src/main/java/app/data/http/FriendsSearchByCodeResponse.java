package app.data.http;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lib.json.JsonUtils;

/**
 */
public class FriendsSearchByCodeResponse implements Serializable {

    // ------------------------------
    // Enum
    // ------------------------------
    public enum ResultCode implements Serializable {
        UNKNOWN(-1),
        OK(0),
        INVALID_CODE(1), // 無効なフレンドコード（一致するユーザが存在しない）
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
    // Member
    // ------------------------------
    public String friendCode;
    public String nickname;

    // ------------------------------
    // Constructor
    // ------------------------------
    public FriendsSearchByCodeResponse(String friendCode, HashMap<String, Object> map) {
        this.friendCode = friendCode;
        this.nickname = JsonUtils.getStringElement(map, "nickname");
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    public static List<Object> getList(HashMap<String, Object> jsonData) {
        if (jsonData == null) {
            return new ArrayList<>();
        }
        return (List<Object>)jsonData.get("friend");
    }

    public final String getTestLog() {
        return "nickname=" + nickname + "\nfriendCode=" + friendCode;
    }

}
