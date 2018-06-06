package app.data.http;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Herve on 2016/09/16.
 */
public class GomipoiJirokichiBonusesParams implements Serializable {

    // ------------------------------
    // Enum
    // ------------------------------
    public enum ResultCode implements Serializable {
        UNKNOWN(-1),
        OK(0),
        NoBonus(1),
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
    public static final String API = "gomipoi_jirokichi_bonuses/receive";

    // ------------------------------
    // Member
    // ------------------------------
    private int mGem;

    // ------------------------------
    // Constructor
    // ------------------------------
    public GomipoiJirokichiBonusesParams(int gem) {
        mGem = gem;
    }

    // ------------------------------
    // Override
    // ------------------------------
    public Object cloneParam() {
        return new GomipoiJirokichiBonusesParams(mGem);
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    public HashMap<String, String> makeParam() {
        HashMap<String, String> map = new HashMap<>();
        return map;
    }

    public int getGem() {
        return mGem;
    }

}
