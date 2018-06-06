package app.data.http;

import java.io.Serializable;
import java.util.HashMap;

/**
 */
public class FriendCodeUseParam implements Serializable {

    // ------------------------------
    // Enum
    // ------------------------------
    public enum ResultCode implements Serializable {
        UNKNOWN(-1),
        OK(0),
        NOT_MATCH_USER(1),              // 無効なフレンドコード（一致するユーザが存在しない）
        IS_SELF(2),                     // 無効なフレンドコード（アクセスユーザのフレンドコードが指定された）
        ALREADY_FRIEND(3),              // 既にフレンド関係
        REACHED_UPPER_LIMIT_SELF(4),    // アクセスユーザのフレンド数が上限に達している
        REACHED_UPPER_LIMIT_FRIEND(5),  // 相手ユーザのフレンド数が上限に達している
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
    public static final String API = "friend_code/use";

    // ------------------------------
    // Member
    // ------------------------------
    public String friendCode;

    // ------------------------------
    // Constructor
    // ------------------------------
    public FriendCodeUseParam(String friendCode) {
        this.friendCode = friendCode;
    }

    // ------------------------------
    // Override
    // ------------------------------
    public Object cloneParam() {
        return new FriendCodeUseParam(friendCode);
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    public HashMap<String, String> makeParam() {
        HashMap<String, String> map = new HashMap<>();
        map.put("friend_code", friendCode);
        return map;
    }

}
