package lib.json;

import java.util.HashMap;

import lib.log.DebugLog;

/**
 */
public class JsonUtils {

    // ------------------------------
    // Accesser
    // ------------------------------
    public static boolean getBooleanElement(HashMap<String, Object> map, String key) {
        if (!map.containsKey(key)) {
            DebugLog.e("notFoundKey:" + key);
            return false;
        }

        if (!(map.get(key) instanceof Integer)) {
            DebugLog.e("notInteger:" + key);
            return false;
        }

        return ((int)map.get(key) != 0);
    }

    public static int getIntElement(HashMap<String, Object> map, String key) {
        if (!map.containsKey(key)) {
            DebugLog.e("notFoundKey:" + key);
            return 0;
        }

        if (!(map.get(key) instanceof Integer)) {
            // Check if number is instance of double
            DebugLog.e("JsonUtils - notInteger Key: " + key + " Value: " + map.get(key));
            return getDoubleElement(map, key).intValue();
        }

        return (int)map.get(key);
    }

    public static Double getDoubleElement(HashMap<String, Object> map, String key) {
        if (!map.containsKey(key)) {
            DebugLog.e("notFoundKey:" + key);
            return 0.0;
        }

        if (!(map.get(key) instanceof Double) && !(map.get(key) instanceof Integer)) {
            DebugLog.e("notInteger:" + key);
            return 0.0;
        }

        if (map.get(key) instanceof Integer) {
            return (double)((int)map.get(key));
        }

        return (double)map.get(key);
    }

    public static String getStringElement(HashMap<String, Object> map, String key) {
        if (!map.containsKey(key)) {
            DebugLog.e("notFoundKey:" + key);
            return "";
        }

        String value = map.get(key).toString();
        if (value.equals("null")) {
            return "";
        }

        return value;
    }


}
