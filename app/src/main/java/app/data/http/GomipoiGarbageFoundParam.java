package app.data.http;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.data.Garbage;
import app.define.GarbageId;

/**
 *
 */
public class GomipoiGarbageFoundParam implements Serializable {

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

    public enum Method implements Serializable {
        UNKNOWN(-1),
        Cleaning(0),
        Synthesis(1),
        ;

        private int mValue;

        Method(int mValue) {
            this.mValue = mValue;
        }

        public int getValue() {
            return mValue;
        }

        public static Method valueOf(int value) {
            for (Method code : values()) {
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
    public static final String API = "gomipoi_garbages/found";

    // ------------------------------
    // Member
    // ------------------------------
    public Method method;
    public List<Garbage> garbages;

    // ------------------------------
    // Constructor
    // ------------------------------
    public GomipoiGarbageFoundParam(Method method) {
        this.method = method;
        this.garbages = new ArrayList<>();
    }

    // ------------------------------
    // Override
    // ------------------------------
    public Object cloneParam() {
        GomipoiGarbageFoundParam param = new GomipoiGarbageFoundParam(method);
        param.garbages = garbages;
        return param;
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    public void addGarbage(Garbage garbage) {
        if (garbages == null) {
            garbages = new ArrayList<>();
        }
        garbages.add(garbage);
    }

    public void addGarbages(List<GarbageId> garbageCodes) {
        if (garbages == null) {
            garbages = new ArrayList<>();
        }

        for (int i = 0; i < garbageCodes.size(); i++) {
            addGarbage(new Garbage(garbageCodes.get(i), method.equals(Method.Cleaning) ? Garbage.FOUND_TYPE_CLEANING : Garbage.FOUND_TYPE_SYNTHESIS));
        }
    }

    public HashMap<String, String> makeParam() {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[");
        for (int i = 0; i < garbages.size(); i++) {
            if (i != 0) {
                jsonBuilder.append(",");
            }
            jsonBuilder.append(garbages.get(i).getJson());
        }
        jsonBuilder.append("]");

        HashMap<String, String> map = new HashMap<>();
        map.put("garbages", String.valueOf(jsonBuilder.toString()));
        return map;
    }

}
