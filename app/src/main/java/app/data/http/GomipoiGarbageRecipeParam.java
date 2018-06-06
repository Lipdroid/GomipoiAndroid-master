package app.data.http;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.data.RecipeData;
import lib.json.JsonUtils;

/**
 */
public class GomipoiGarbageRecipeParam implements Serializable {

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
    public static final String API = "gomipoi_garbages/recipes";

    // ------------------------------
    // Member
    // ------------------------------
    public List<RecipeData> mRecipeList;

    // ------------------------------
    // Constructor
    // ------------------------------
    public GomipoiGarbageRecipeParam() {
        mRecipeList = new ArrayList<>();
    }

    public GomipoiGarbageRecipeParam(HashMap<String, Object> jsonData) {
        mRecipeList = new ArrayList<>();

        if (jsonData.get("garbages") != null) {
            List<Object> list = (List)jsonData.get("garbages");
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    HashMap<String, Object> map = (HashMap)list.get(i);
                    if (map != null) {
                        String itemCode = null;
                        String garbageCode1 = null;
                        boolean foundGarbage1 = false;
                        String garbageCode2 = null;
                        boolean foundGarbage2 = false;
                        String garbageCode3 = null;
                        boolean foundGarbage3 = false;

                        if (map.get("item_code") != null) {
                            itemCode = JsonUtils.getStringElement(map, "item_code");
                        }

                        if (map.get("garbage_code_1") != null) {
                            garbageCode1 = JsonUtils.getStringElement(map, "garbage_code_1");
                        }
                        if (map.get("found_garbage_1") != null) {
                            foundGarbage1 = JsonUtils.getBooleanElement(map, "found_garbage_1");
                        }

                        if (map.get("garbage_code_2") != null) {
                            garbageCode2 = JsonUtils.getStringElement(map, "garbage_code_2");
                        }
                        if (map.get("found_garbage_2") != null) {
                            foundGarbage2 = JsonUtils.getBooleanElement(map, "found_garbage_2");
                        }

                        if (map.get("garbage_code_3") != null) {
                            garbageCode3 = JsonUtils.getStringElement(map, "garbage_code_3");
                        }
                        if (map.get("found_garbage_3") != null) {
                            foundGarbage3 = JsonUtils.getBooleanElement(map, "found_garbage_3");
                        }

                        mRecipeList.add(new RecipeData(
                                itemCode,
                                garbageCode1,
                                foundGarbage1,
                                garbageCode2,
                                foundGarbage2,
                                garbageCode3,
                                foundGarbage3));
                    }
                }
            }
        }
    }

    // ------------------------------
    // Override
    // ------------------------------
    public Object cloneParam() {
        return new GomipoiGarbageRecipeParam();
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    public HashMap<String, String> makeParam() {
        return null;
    }

}
