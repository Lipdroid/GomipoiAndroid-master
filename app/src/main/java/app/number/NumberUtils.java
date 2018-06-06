package app.number;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * 数字に関するUtilityクラス
 */
public class NumberUtils {

    // ------------------------------
    // Accesser
    // ------------------------------
    public static String getNumberFormatText(int value) {
        return NumberFormat.getInstance(Locale.getDefault()).format(value);
    }

    public static String getNumberFormatText(float value) {
        return NumberFormat.getInstance(Locale.getDefault()).format(value);
    }

    public static String getNumberFormatText(double value) {
        return NumberFormat.getInstance(Locale.getDefault()).format(value);
    }

    public static String getNumberFormatText(long value) {
        return NumberFormat.getInstance(Locale.getDefault()).format(value);
    }

}
