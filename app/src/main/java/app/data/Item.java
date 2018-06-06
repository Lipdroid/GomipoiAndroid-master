package app.data;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import lib.json.JsonUtils;
import lib.log.DebugLog;

/**
 */
public class Item implements Serializable {

    // ------------------------------
    // Member
    // ------------------------------
    public String item_code;
    public int own_count;
    public boolean item_using;
    public Date used_at;

    // ------------------------------
    // Constructor
    // ------------------------------
    public Item(HashMap<String, Object> map) {
        if (map == null) {
            return;
        }

        if (map.get("item_code") != null) {
            item_code = JsonUtils.getStringElement(map, "item_code");
        }

        if (map.get("own_count") != null) {
            own_count = JsonUtils.getIntElement(map, "own_count");
        }

        if (map.get("item_using") != null) {
            item_using = JsonUtils.getBooleanElement(map, "item_using");
        }

        if (map.get("used_at") != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss'Z'");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            String serverDateText = JsonUtils.getStringElement(map, "used_at");
            try {
                used_at = dateFormat.parse(serverDateText);
            } catch (ParseException e) {
                DebugLog.e(e.toString());
            }
        }

    }

}
