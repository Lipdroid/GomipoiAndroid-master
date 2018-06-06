package app.data.http;

import java.io.Serializable;
import java.util.HashMap;

import app.data.BookGarbageData;

/**
 *
 */
public class GomipoiBookCheckParam implements Serializable {

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
    public static final String API = "gomipoi_books/check";

    // ------------------------------
    // Member
    // ------------------------------
    public BookGarbageData garbageData;

    // ------------------------------
    // Constructor
    // ------------------------------
    public GomipoiBookCheckParam(BookGarbageData data) {
        garbageData = data;
    }

    // ------------------------------
    // Override
    // ------------------------------
    public Object cloneParam() {
        return new GomipoiBookCheckParam(garbageData);
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    public HashMap<String, String> makeParam() {
        HashMap<String, String> map = new HashMap<>();
        map.put("garbage_code", garbageData.getCode());
        return map;
    }

}
