package app.data.http;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.data.BookGarbageData;
import app.data.Garbage;
import app.define.GarbageId;

/**
 */
public class GomipoiGarbageSynthesesParam implements Serializable {

    // ------------------------------
    // Enum
    // ------------------------------
    public enum ResultCode implements Serializable {
        UNKNOWN(-1),
        OK(0),
        NotHaveReceipe(1),
        Failed(2),
        AlreadySucceed(3),
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
    public static final String API = "gomipoi_garbages/syntheses";

    // ------------------------------
    // Member
    // ------------------------------
    public BookGarbageData garbageId_1;
    public BookGarbageData garbageId_2;
    public BookGarbageData garbageId_3;

    // ------------------------------
    // Constructor
    // ------------------------------
    public GomipoiGarbageSynthesesParam(BookGarbageData garbageId_1, BookGarbageData garbageId_2, BookGarbageData garbageId_3) {
        this.garbageId_1 = garbageId_1;
        this.garbageId_2 = garbageId_2;
        this.garbageId_3 = garbageId_3;
    }

    // ------------------------------
    // Override
    // ------------------------------
    public Object cloneParam() {
        return new GomipoiGarbageSynthesesParam(garbageId_1, garbageId_2, garbageId_3);
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    public HashMap<String, String> makeParam() {
        HashMap<String, String> map = new HashMap<>();
        map.put("garbage_code_1", garbageId_1.getCode());
        map.put("garbage_code_2", garbageId_2.getCode());
        map.put("garbage_code_3", garbageId_3.getCode());
        return map;
    }

}
