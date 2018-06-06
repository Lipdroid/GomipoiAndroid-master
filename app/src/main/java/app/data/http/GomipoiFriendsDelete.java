package app.data.http;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lib.json.JsonUtils;

/**
 * Created by jerro on 21/02/2018.
 */

public class GomipoiFriendsDelete implements Serializable {

    // ------------------------------
    // Enum
    // ------------------------------
    public enum ResultCode implements Serializable {
        UNKNOWN(-1),
        OK(0),
        FAILED(1)
        ;

        private int mValue;

        ResultCode(int mValue) {
            this.mValue = mValue;
        }

        public int getValue() {
            return mValue;
        }

        public static GomipoiFriendsDelete.ResultCode valueOf(int value) {
            for (GomipoiFriendsDelete.ResultCode code : values()) {
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
    public static String API = "NA";

    // ------------------------------
    // Member
    // ------------------------------
    private int friendId;

    // ------------------------------
    // Constructor
    // ------------------------------
    public GomipoiFriendsDelete(int friendId) {
        this.friendId = friendId;
        API = "friends/" + this.friendId;
    }

    public HashMap<String, String> makeParam() {
        HashMap<String, String> map = new HashMap<>();
        map.put("id", Integer.toString(this.friendId));
        return map;
    }
}