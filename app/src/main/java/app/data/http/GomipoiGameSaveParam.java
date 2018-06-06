package app.data.http;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import app.data.Garbage;
import app.define.GarbageId;

/**
 */
public class GomipoiGameSaveParam implements Serializable {

    // ------------------------------
    // Enum
    // ------------------------------
    public enum PlaceType {
        DEFAULT(1),
        POIKO_ROOM(2),
        GARDEN(3),
        ;

        private int mValue;

        PlaceType(int mValue) {
            this.mValue = mValue;
        }

        public int getValue() {
            return mValue;
        }

        public static PlaceType valueOf(int value) {
            for (PlaceType code : values()) {
                if (code.getValue() == value) {
                    return code;
                }
            }
            return DEFAULT;
        }
    }

    public enum ResultCode implements Serializable {
        UNKNOWN(-1),
        OK(0),
        AlreadyMaxCapacity(1),
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
    public static final String API = "gomipoi_games/save";

    // ------------------------------
    // Member
    // ------------------------------
    public PlaceType placeType;
    public int add_point;
    public int put_in_garbage_count;
    public String garbages;
    public int broom_use_count;
    public boolean broom_broken;

    public int nextStage;

    private String newFoundGarbages;

    public boolean garbage_can_broken;

    /**
     *
     * @param jsonString jsonString
     * @return
     */
    @Nullable
    public static GomipoiGameSaveParam objectFromJsonString(@Nullable String jsonString) {
        GomipoiGameSaveParam param = null;
        try {
            JSONObject obj = new JSONObject(jsonString);
            String place_str = obj.getString("place_type");
            String add_point_str = obj.getString("add_point");
            String put_in_garbage_str = obj.getString("put_in_garbage_count");
            String garbages = obj.getString("garbages");
            String broom_use_count_str = obj.getString("broom_use_count");
            String broom_broken_str = obj.getString("broom_broken");
            String newFoundGarbages = obj.getString("newFoundGarbages");
            String nextStage_str = obj.getString("nextStage");
            String garbage_can_broken_str = obj.getString("garbage_can_broken");

            int placeValue = Integer.parseInt(place_str);
            PlaceType type = PlaceType.valueOf(placeValue);
            int add_point = Integer.parseInt(add_point_str);
            int put_in_garbage_count = Integer.parseInt(put_in_garbage_str);
            int broom_use_count = Integer.parseInt(broom_use_count_str);

            boolean is_broken = Integer.parseInt(broom_broken_str) == 1;
            boolean is_garbage_can_broken = Integer.parseInt(garbage_can_broken_str) == 1;
            int nextStage = Integer.parseInt(nextStage_str);
            param = new GomipoiGameSaveParam(type,
                    add_point,
                    put_in_garbage_count,
                    garbages,
                    broom_use_count, is_broken, newFoundGarbages, nextStage, is_garbage_can_broken);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return param;
    }

    // ------------------------------
    // Constructor
    // ------------------------------
    public GomipoiGameSaveParam(PlaceType placeType, int add_point, int put_in_garbage_count, String garbages, int broom_use_count, boolean broom_broken, String newFoundGarbages, int nextStage, boolean isGarbageCanBroken) {
        this.placeType = placeType;
        this.add_point = add_point;
        this.broom_broken = broom_broken;
        this.broom_use_count = broom_use_count;
        this.garbages = garbages;
        this.put_in_garbage_count = put_in_garbage_count;
        this.newFoundGarbages = newFoundGarbages;

        this.nextStage = nextStage;
        this.garbage_can_broken = isGarbageCanBroken;
    }


    // ------------------------------
    // Override
    // ------------------------------
    public Object cloneParam() {
        return new GomipoiGameSaveParam(placeType, add_point, put_in_garbage_count, garbages, broom_use_count, broom_broken, newFoundGarbages, nextStage, garbage_can_broken);
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    public HashMap<String, String> makeParam() {
        HashMap<String, String> map = new HashMap<>();
        map.put("place_type", String.valueOf(placeType.getValue()));
        map.put("add_point", String.valueOf(add_point));
        map.put("put_in_garbage_count", String.valueOf(put_in_garbage_count));
        map.put("garbages", garbages);
        map.put("broom_use_count", String.valueOf(broom_use_count));
        map.put("broom_broken", String.valueOf((broom_broken ? 1 : 0)));
        map.put("garbage_can_broken", String.valueOf((garbage_can_broken ? 1 : 0)));
        return map;
    }

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("place_type", String.valueOf(placeType.getValue()));
        map.put("add_point", String.valueOf(add_point));
        map.put("put_in_garbage_count", String.valueOf(put_in_garbage_count));
        map.put("garbages", garbages);
        map.put("broom_use_count", String.valueOf(broom_use_count));
        map.put("broom_broken", String.valueOf((broom_broken ? 1 : 0)));
        map.put("newFoundGarbages", newFoundGarbages);
        map.put("nextStage", String.valueOf(nextStage));
        map.put("garbage_can_broken", String.valueOf(garbage_can_broken ? 1 : 0));
        return map;
    }

    public GomipoiGarbageFoundParam getGarbageFoundParam() {
        if (newFoundGarbages == null || newFoundGarbages.length() == 0) {
            return null;
        }

        String[] idList = newFoundGarbages.split(",");
        GomipoiGarbageFoundParam param = new GomipoiGarbageFoundParam(GomipoiGarbageFoundParam.Method.Cleaning);
        for (int i = 0; i < idList.length; i++) {
            param.addGarbage(new Garbage(GarbageId.valueOfWithCode(idList[i]), Garbage.FOUND_TYPE_CLEANING));
        }

        return param;
    }
}
