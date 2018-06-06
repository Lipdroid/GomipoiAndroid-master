package app.data.http;

import java.io.Serializable;
import java.util.HashMap;

/**
 */
public class UserAppPointParam implements Serializable {

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
        LIQUIDATE(0),
        OTHER(1),
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
    public static final String API = "user_apps/point";

    // ------------------------------
    // Member
    // ------------------------------
    public Method method;
    public int add_point;

    // ------------------------------
    // Constructor
    // ------------------------------
    public UserAppPointParam(Method method, int add_point) {
        this.method = method;
        this.add_point = add_point;
    }

    // ------------------------------
    // Override
    // ------------------------------
    public Object cloneParam() {
        return new UserAppPointParam(method, add_point);
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    public HashMap<String, String> makeParam() {
        HashMap<String, String> map = new HashMap<>();
        map.put("add_point", String.valueOf(add_point));
        return map;
    }

    public boolean isLiquidate() {
        return method.equals(Method.LIQUIDATE);
    }
}
