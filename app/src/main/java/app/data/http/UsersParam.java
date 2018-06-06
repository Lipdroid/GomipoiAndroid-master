package app.data.http;

import java.io.Serializable;
import java.util.HashMap;

/**
 */
public class UsersParam implements Serializable {

    // ------------------------------
    // Enum
    // ------------------------------
    public enum ResultCode implements Serializable {
        UNKNOWN(-1),
        OK(0),
        EXIST_SAME_ACCOUNT(1),
        EXIST_SAME_NICKNAME(2),
        INVALID_PASSWORD(3),
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
    public static final String API = "users";

    // ------------------------------
    // Member
    // ------------------------------
    public String nickname;
    public String account;
    public String password;
    public UserSessionsParam.ParentActivity parentActivity;

    // ------------------------------
    // Constructor
    // ------------------------------
    public UsersParam(String nickname, String account, String password, UserSessionsParam.ParentActivity parentActivity) {
        this.nickname = nickname;
        this.account = account;
        this.password = password;
        this.parentActivity = parentActivity;
    }

    // ------------------------------
    // Override
    // ------------------------------
    public Object cloneParam() {
        return new UsersParam(nickname, account, password, parentActivity);
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    public HashMap<String, String> makeParam() {
        HashMap<String, String> map = new HashMap<>();
        map.put("nickname", String.valueOf(nickname));
        map.put("account", String.valueOf(account));
        map.put("password", String.valueOf(password));
        return map;
    }

}
