package app.data.http;

import java.io.Serializable;
import java.util.HashMap;

import app.data.ListItemData;

/**
 *
 */
public class GomipoiItemUseParam implements Serializable {

    // ------------------------------
    // Enum
    // ------------------------------
    public enum ResultCode implements Serializable {
        UNKNOWN(-1),
        OK(0),
        NotHave(1),
        AlreadyEmptyCan(2),
        AlreadyUsed(3),
        Disabled(4),
        NotChangeCan(5),
        DailyCountReached(6),
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
    public static final String API = "gomipoi_items/use";

    // ------------------------------
    // Member
    // ------------------------------
    public ListItemData itemData;

    // ------------------------------
    // Constructor
    // ------------------------------
    public GomipoiItemUseParam(ListItemData itemData) {
        this.itemData = itemData;
    }

    // ------------------------------
    // Override
    // ------------------------------
    public Object cloneParam() {
        return new GomipoiItemUseParam(itemData);
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    public HashMap<String, String> makeParam() {
        HashMap<String, String> map = new HashMap<>();
        map.put("item_code", itemData.itemCode.getValue());
        return map;
    }

}
