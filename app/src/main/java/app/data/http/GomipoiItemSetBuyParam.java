package app.data.http;

import java.io.Serializable;
import java.util.HashMap;

import app.data.ListItemData;

/**
 */
public class GomipoiItemSetBuyParam implements Serializable {

    // ------------------------------
    // Enum
    // ------------------------------
    public enum ResultCode implements Serializable {
        UNKNOWN(-1),
        OK(0),
        GemShortage(1),
        AchievedMaxPossession(2),
        NoKey(3),
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
    public static final String API = "gomipoi_item_sets/buy";

    // ------------------------------
    // Member
    // ------------------------------
    public ListItemData itemData;

    // ------------------------------
    // Constructor
    // ------------------------------
    public GomipoiItemSetBuyParam(ListItemData itemData) {
        this.itemData = itemData;
    }

    // ------------------------------
    // Override
    // ------------------------------
    public Object cloneParam() {
        return new GomipoiItemSetBuyParam(itemData);
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    public HashMap<String, String> makeParam() {
        HashMap<String, String> map = new HashMap<>();
        map.put("item_set_code", itemData.itemSetCode.getValue());
        return map;
    }

}
