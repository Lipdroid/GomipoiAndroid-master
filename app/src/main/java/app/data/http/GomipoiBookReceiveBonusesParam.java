package app.data.http;

import java.io.Serializable;
import java.util.HashMap;

/**
 *
 */
public class GomipoiBookReceiveBonusesParam implements Serializable {

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
    public static final String API = "gomipoi_books/receive_bonuses";

    // ------------------------------
    // Member
    // ------------------------------
    public int[] receive_bonus_pages;

    // ------------------------------
    // Constructor
    // ------------------------------
    public GomipoiBookReceiveBonusesParam(int[] pages) {
        receive_bonus_pages = pages;
    }

    // ------------------------------
    // Override
    // ------------------------------
    public Object cloneParam() {
        return new GomipoiBookReceiveBonusesParam(receive_bonus_pages);
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    public HashMap<String, String> makeParam() {
        HashMap<String, String> map = new HashMap<>();

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < receive_bonus_pages.length; i++) {
            if (i == 0) {
                builder.append("[");
            } else if (i != 0) {
                builder.append(",");
            }
            builder.append(String.valueOf(receive_bonus_pages[i]));

            if (i == receive_bonus_pages.length - 1) {
                builder.append("]");
            }
        }

        map.put("receive_bonus_pages", builder.toString());
        return map;
    }

}
