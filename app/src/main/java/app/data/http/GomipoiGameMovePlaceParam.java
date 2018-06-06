package app.data.http;

import java.io.Serializable;
import java.util.HashMap;

/**
 */
public class GomipoiGameMovePlaceParam implements Serializable {

    // ------------------------------
    // Enum
    // ------------------------------
    public enum ResultCode implements Serializable {
        UNKNOWN(-1),
        OK(0),
        AlreadyInRoom(1),
        NoKeyRoom(2),
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
    public static final String API = "gomipoi_games/move_place";

    // ------------------------------
    // Member
    // ------------------------------
    public GomipoiGameSaveParam.PlaceType placeType;

    // ------------------------------
    // Constructor
    // ------------------------------
    public GomipoiGameMovePlaceParam(GomipoiGameSaveParam.PlaceType placeType) {
        this.placeType = placeType;
    }

    // ------------------------------
    // Override
    // ------------------------------
    public Object cloneParam() {
        return new GomipoiGameMovePlaceParam(placeType);
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    public HashMap<String, String> makeParam() {
        HashMap<String, String> map = new HashMap<>();
        map.put("place_type", String.valueOf(placeType.getValue()));
        return map;
    }
}
