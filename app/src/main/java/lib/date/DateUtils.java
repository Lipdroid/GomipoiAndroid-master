package lib.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 */
public class DateUtils {

    // ------------------------------
    // Accesser
    // ------------------------------
    /**
     * UTC時間の文字列をDateのインスタンスに変換して返す
     */
    public static Date valueOf(String utcDateString, String format) {
        SimpleDateFormat utcFormat = new SimpleDateFormat(format);
        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return utcFormat.parse(utcDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
