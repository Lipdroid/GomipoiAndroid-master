package app.data.http;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by jerro on 3/14/2018.
 */

public class ExchangePremiumTicketResponse implements Serializable {

    // ------------------------------
    // Enum
    // ------------------------------
    public enum ResultCode implements Serializable {
        UNKNOWN(-1),
        OK(0),
        FAILED(1);

        private int mValue;

        ResultCode(int mValue) {
            this.mValue = mValue;
        }

        public int getValue() {
            return mValue;
        }

        public static ExchangePremiumTicketResponse.ResultCode valueOf(int value) {
            for (ExchangePremiumTicketResponse.ResultCode code : values()) {
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
    public static String API = "/users/premium_ticket";

    // ------------------------------
    // Member
    // ------------------------------
    //private int friendId;

    // ------------------------------
    // Constructor
    // ------------------------------
    public ExchangePremiumTicketResponse(int friendId) {
        //this.friendId = friendId;
        //API = "friends/" + this.friendId;
    }

    public HashMap<String, String> makeParam() {
        HashMap<String, String> map = new HashMap<>();
        //map.put("id", Integer.toString(this.friendId));
        return map;
    }
}