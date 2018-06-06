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
 *
 */
public class UsingItem implements Serializable {

    // ------------------------------
    // Member
    // ------------------------------
    public String item_code;
    public int use_count;
    public Date used_at;

    // ------------------------------
    // Constructor
    // ------------------------------
    public UsingItem(HashMap<String, Object> map) {
        if (map == null) {
            return;
        }

        if (map.get("item_code") != null) {
            this.item_code = JsonUtils.getStringElement(map, "item_code");
        }

        if (map.get("use_count") != null) {
            this.use_count = JsonUtils.getIntElement(map, "use_count");
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
