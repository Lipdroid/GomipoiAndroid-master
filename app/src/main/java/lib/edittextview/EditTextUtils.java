package lib.edittextview;

import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;

/**
 *
 */
public class EditTextUtils {

    // ------------------------------
    // Define
    // ------------------------------
    // MODE
    public static final int MODE_Alphanumeric = 1;
    public static final int MODE_NoPictogram = 2;
    public static final int MODE_MaxLength = 3;

    // OUT_OF_RANGE_RESULT
    public static final int RANGE_RESULT_NG = 0;
    public static final int RANGE_RESULT_OK = 1;
    public static final int RANGE_RESULT_SMALL = 2;
    public static final int RANGE_RESULT_LARGE = 3;

    // ------------------------------
    // Accesser
    // ------------------------------
    /**
     * EditTextにInputFileterを設定する
     */
    public static void setInputFilter(EditText editText, int... modes) {
        setInputFilter(Integer.MAX_VALUE, editText, modes);
    }

    /**
     * EditTextにInputFileterを設定する
     */
    public static void setInputFilter(final int max, final EditText editText, final int... modes) {
        if (editText == null) {
            return;
        }

        InputFilter inputFilter = new InputFilter() {

            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {

                for (int i = 0; i < modes.length; i++) {
                    int mode = modes[i];
                    switch (mode) {
                        case MODE_Alphanumeric: {
                            if (!(source.toString().matches("^[0-9a-zA-Z]+$"))) {
                                return "";
                            }
                            break;
                        }

                        case MODE_NoPictogram: {
                            if (isInclude_Pictogram(source.toString())) {
                                return "";
                            }
                            break;
                        }

                        case MODE_MaxLength: {
                            if (isOutOfTextSizeRange(editText, Integer.MIN_VALUE, 5) != RANGE_RESULT_OK) {
                                return "";
                            }
                            break;
                        }

                    }

                }

                return source;
            }

        };

        InputFilter[] filters = new InputFilter[] { inputFilter };
        editText.setFilters(filters);
    }

    /**
     * 入力値が空かどうかを返す
     */
    public static boolean isEmpty(EditText editText) {
        return (editText == null
                || editText.getText() == null
                || editText.getText().toString().replaceAll(" ", "").replaceAll("　", "").length() == 0);
    }

    /**
     * 絵文字が入力文字内に含まれているかを返す
     */
    public static boolean isIncludePictogram(EditText editText) {
        return isInclude_Pictogram(editText.getText().toString());
    }

    /**
     * 文字数が許容範囲外かを返す
     */
    public static int isOutOfTextSizeRange(EditText editText, int minLength, int maxLength) {
        if (editText == null
                || editText.getText() == null) {
            return RANGE_RESULT_NG;
        }

        if (editText.getText().toString().length() < minLength) {
            return RANGE_RESULT_SMALL;
        }

        if (editText.getText().toString().length() > maxLength) {
            return RANGE_RESULT_LARGE;
        }

        return RANGE_RESULT_OK;
    }

    // ------------------------------
    // Function
    // ------------------------------
    private static boolean isInclude_Pictogram(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }

        final char[] chars = str.toCharArray();

        int codePoint = 0;
        for (int i = 0; i < chars.length; i++) {
            if (Character.isLowSurrogate(chars[i])) {
                if (i > 0 && Character.isSurrogatePair(chars[i-1], chars[i])) {
                    // get the Unicode code point for the surrogate pair
                    codePoint = Character.toCodePoint(chars[i-1], chars[i]);
                }
            } else {
                // get the Unicode code point by simply casting the char to int
                codePoint = (int) chars[i];
            }

            if (i > 0 && Character.isSurrogatePair(chars[i - 1], chars[i]) && Character.isHighSurrogate(chars[i - 1])) {
                if (codePoint != 0 && Integer.decode("0x1d000") <= codePoint && codePoint <= Integer.decode("0x1f77f")) {
                    return true;
                }
            } else {
                if (('\u2100' <= chars[i] && chars[i] <= '\u27bf')) {
                    return true;
                }
            }

            if (codePoint != 0 && isPictogram(codePoint)) {
                return true;
            }
        }

        return false;
    }

    private static boolean isPictogram(int codePoint) {
        return
        // Digits and number sign on keys (actually defined in concatenated form)
        // codePoint == 0x0023 0x20E3 ||
        // codePoint == 0x0030 0x20E3 ||
        // codePoint == 0x0031 0x20E3 ||
        // codePoint == 0x0032 0x20E3 ||
        // codePoint == 0x0033 0x20E3 ||
        // codePoint == 0x0034 0x20E3 ||
        // codePoint == 0x0035 0x20E3 ||
        // codePoint == 0x0036 0x20E3 ||
        // codePoint == 0x0037 0x20E3 ||
        // codePoint == 0x0038 0x20E3 ||
        // codePoint == 0x0039 0x20E3 ||

        codePoint == 0x00A9 ||
        codePoint == 0x00AE ||
        codePoint == 0x2002 ||
        codePoint == 0x2003 ||
        codePoint == 0x2005 ||
        codePoint == 0x203C ||
        codePoint == 0x2049 ||
        codePoint == 0x2122 ||
        codePoint == 0x2139 ||
        codePoint == 0x2194 ||
        codePoint == 0x2195 ||
        codePoint == 0x2196 ||
        codePoint == 0x2197 ||
        codePoint == 0x2198 ||
        codePoint == 0x2199 ||
        codePoint == 0x21A9 ||
        codePoint == 0x21AA ||
        codePoint == 0x231A ||
        codePoint == 0x231B ||
        codePoint == 0x23E9 ||
        codePoint == 0x23EA ||
        codePoint == 0x23EB ||
        codePoint == 0x23EC ||
        codePoint == 0x23F0 ||
        codePoint == 0x23F3 ||
        codePoint == 0x24C2 ||
        codePoint == 0x25AA ||
        codePoint == 0x25AB ||
        codePoint == 0x25B6 ||
        codePoint == 0x25C0 ||
        codePoint == 0x25FB ||
        codePoint == 0x25FC ||
        codePoint == 0x25FD ||
        codePoint == 0x25FE ||
        codePoint == 0x2600 ||
        codePoint == 0x2601 ||
        codePoint == 0x260E ||
        codePoint == 0x2611 ||
        codePoint == 0x2614 ||
        codePoint == 0x2615 ||
        codePoint == 0x261D ||
        codePoint == 0x263A ||
        codePoint == 0x2648 ||
        codePoint == 0x2649 ||
        codePoint == 0x264A ||
        codePoint == 0x264B ||
        codePoint == 0x264C ||
        codePoint == 0x264D ||
        codePoint == 0x264E ||
        codePoint == 0x264F ||
        codePoint == 0x2650 ||
        codePoint == 0x2651 ||
        codePoint == 0x2652 ||
        codePoint == 0x2653 ||
        codePoint == 0x2660 ||
        codePoint == 0x2663 ||
        codePoint == 0x2665 ||
        codePoint == 0x2666 ||
        codePoint == 0x2668 ||
        codePoint == 0x267B ||
        codePoint == 0x267F ||
        codePoint == 0x2693 ||
        codePoint == 0x26A0 ||
        codePoint == 0x26A1 ||
        codePoint == 0x26AA ||
        codePoint == 0x26AB ||
        codePoint == 0x26BD ||
        codePoint == 0x26BE ||
        codePoint == 0x26C4 ||
        codePoint == 0x26C5 ||
        codePoint == 0x26CE ||
        codePoint == 0x26D4 ||
        codePoint == 0x26EA ||
        codePoint == 0x26F2 ||
        codePoint == 0x26F3 ||
        codePoint == 0x26F5 ||
        codePoint == 0x26FA ||
        codePoint == 0x26FD ||
        codePoint == 0x2702 ||
        codePoint == 0x2705 ||
        codePoint == 0x2708 ||
        codePoint == 0x2709 ||
        codePoint == 0x270A ||
        codePoint == 0x270B ||
        codePoint == 0x270C ||
        codePoint == 0x270F ||
        codePoint == 0x2712 ||
        codePoint == 0x2714 ||
        codePoint == 0x2716 ||
        codePoint == 0x2728 ||
        codePoint == 0x2733 ||
        codePoint == 0x2734 ||
        codePoint == 0x2744 ||
        codePoint == 0x2747 ||
        codePoint == 0x274C ||
        codePoint == 0x274E ||
        codePoint == 0x2753 ||
        codePoint == 0x2754 ||
        codePoint == 0x2755 ||
        codePoint == 0x2757 ||
        codePoint == 0x2764 ||
        codePoint == 0x2795 ||
        codePoint == 0x2796 ||
        codePoint == 0x2797 ||
        codePoint == 0x27A1 ||
        codePoint == 0x27B0 ||
        codePoint == 0x2934 ||
        codePoint == 0x2935 ||
        codePoint == 0x2B05 ||
        codePoint == 0x2B06 ||
        codePoint == 0x2B07 ||
        codePoint == 0x2B1B ||
        codePoint == 0x2B1C ||
        codePoint == 0x2B50 ||
        codePoint == 0x2B55 ||
        codePoint == 0x3030 ||
        codePoint == 0x303D ||
        codePoint == 0x3297 ||
        codePoint == 0x3299 ||
        codePoint == 0x1F004 ||
        codePoint == 0x1F0CF ||
        codePoint == 0x1F170 ||
        codePoint == 0x1F171 ||
        codePoint == 0x1F17E ||
        codePoint == 0x1F17F ||
        codePoint == 0x1F18E ||
        codePoint == 0x1F191 ||
        codePoint == 0x1F192 ||
        codePoint == 0x1F193 ||
        codePoint == 0x1F194 ||
        codePoint == 0x1F195 ||
        codePoint == 0x1F196 ||
        codePoint == 0x1F197 ||
        codePoint == 0x1F198 ||
        codePoint == 0x1F199 ||
        codePoint == 0x1F19A ||

        // Regional Indicator Symbols (actually defined in concatenated form)
        (codePoint == 0x1F1E8 || codePoint == 0x1F1F3) ||
        (codePoint == 0x1F1E9 || codePoint == 0x1F1EA) ||
        (codePoint == 0x1F1EA || codePoint == 0x1F1F8) ||
        (codePoint == 0x1F1EB || codePoint == 0x1F1F7) ||
        (codePoint == 0x1F1EC || codePoint == 0x1F1E7) ||
        (codePoint == 0x1F1EE || codePoint == 0x1F1F9) ||
        (codePoint == 0x1F1EF || codePoint == 0x1F1F5) ||
        (codePoint == 0x1F1F0 || codePoint == 0x1F1F7) ||
        (codePoint == 0x1F1F7 || codePoint == 0x1F1FA) ||
        (codePoint == 0x1F1FA || codePoint == 0x1F1F8) ||

        codePoint == 0x1F201 ||
        codePoint == 0x1F202 ||
        codePoint == 0x1F21A ||
        codePoint == 0x1F22F ||
        codePoint == 0x1F232 ||
        codePoint == 0x1F233 ||
        codePoint == 0x1F234 ||
        codePoint == 0x1F235 ||
        codePoint == 0x1F236 ||
        codePoint == 0x1F237 ||
        codePoint == 0x1F238 ||
        codePoint == 0x1F239 ||
        codePoint == 0x1F23A ||
        codePoint == 0x1F250 ||
        codePoint == 0x1F251 ||
        codePoint == 0x1F300 ||
        codePoint == 0x1F301 ||
        codePoint == 0x1F302 ||
        codePoint == 0x1F303 ||
        codePoint == 0x1F304 ||
        codePoint == 0x1F305 ||
        codePoint == 0x1F306 ||
        codePoint == 0x1F307 ||
        codePoint == 0x1F308 ||
        codePoint == 0x1F309 ||
        codePoint == 0x1F30A ||
        codePoint == 0x1F30B ||
        codePoint == 0x1F30C ||
        codePoint == 0x1F30F ||
        codePoint == 0x1F311 ||
        codePoint == 0x1F313 ||
        codePoint == 0x1F314 ||
        codePoint == 0x1F315 ||
        codePoint == 0x1F319 ||
        codePoint == 0x1F31B ||
        codePoint == 0x1F31F ||
        codePoint == 0x1F320 ||
        codePoint == 0x1F330 ||
        codePoint == 0x1F331 ||
        codePoint == 0x1F334 ||
        codePoint == 0x1F335 ||
        codePoint == 0x1F337 ||
        codePoint == 0x1F338 ||
        codePoint == 0x1F339 ||
        codePoint == 0x1F33A ||
        codePoint == 0x1F33B ||
        codePoint == 0x1F33C ||
        codePoint == 0x1F33D ||
        codePoint == 0x1F33E ||
        codePoint == 0x1F33F ||
        codePoint == 0x1F340 ||
        codePoint == 0x1F341 ||
        codePoint == 0x1F342 ||
        codePoint == 0x1F343 ||
        codePoint == 0x1F344 ||
        codePoint == 0x1F345 ||
        codePoint == 0x1F346 ||
        codePoint == 0x1F347 ||
        codePoint == 0x1F348 ||
        codePoint == 0x1F349 ||
        codePoint == 0x1F34A ||
        codePoint == 0x1F34C ||
        codePoint == 0x1F34D ||
        codePoint == 0x1F34E ||
        codePoint == 0x1F34F ||
        codePoint == 0x1F351 ||
        codePoint == 0x1F352 ||
        codePoint == 0x1F353 ||
        codePoint == 0x1F354 ||
        codePoint == 0x1F355 ||
        codePoint == 0x1F356 ||
        codePoint == 0x1F357 ||
        codePoint == 0x1F358 ||
        codePoint == 0x1F359 ||
        codePoint == 0x1F35A ||
        codePoint == 0x1F35B ||
        codePoint == 0x1F35C ||
        codePoint == 0x1F35D ||
        codePoint == 0x1F35E ||
        codePoint == 0x1F35F ||
        codePoint == 0x1F360 ||
        codePoint == 0x1F361 ||
        codePoint == 0x1F362 ||
        codePoint == 0x1F363 ||
        codePoint == 0x1F364 ||
        codePoint == 0x1F365 ||
        codePoint == 0x1F366 ||
        codePoint == 0x1F367 ||
        codePoint == 0x1F368 ||
        codePoint == 0x1F369 ||
        codePoint == 0x1F36A ||
        codePoint == 0x1F36B ||
        codePoint == 0x1F36C ||
        codePoint == 0x1F36D ||
        codePoint == 0x1F36E ||
        codePoint == 0x1F36F ||
        codePoint == 0x1F370 ||
        codePoint == 0x1F371 ||
        codePoint == 0x1F372 ||
        codePoint == 0x1F373 ||
        codePoint == 0x1F374 ||
        codePoint == 0x1F375 ||
        codePoint == 0x1F376 ||
        codePoint == 0x1F377 ||
        codePoint == 0x1F378 ||
        codePoint == 0x1F379 ||
        codePoint == 0x1F37A ||
        codePoint == 0x1F37B ||
        codePoint == 0x1F380 ||
        codePoint == 0x1F381 ||
        codePoint == 0x1F382 ||
        codePoint == 0x1F383 ||
        codePoint == 0x1F384 ||
        codePoint == 0x1F385 ||
        codePoint == 0x1F386 ||
        codePoint == 0x1F387 ||
        codePoint == 0x1F388 ||
        codePoint == 0x1F389 ||
        codePoint == 0x1F38A ||
        codePoint == 0x1F38B ||
        codePoint == 0x1F38C ||
        codePoint == 0x1F38D ||
        codePoint == 0x1F38E ||
        codePoint == 0x1F38F ||
        codePoint == 0x1F390 ||
        codePoint == 0x1F391 ||
        codePoint == 0x1F392 ||
        codePoint == 0x1F393 ||
        codePoint == 0x1F3A0 ||
        codePoint == 0x1F3A1 ||
        codePoint == 0x1F3A2 ||
        codePoint == 0x1F3A3 ||
        codePoint == 0x1F3A4 ||
        codePoint == 0x1F3A5 ||
        codePoint == 0x1F3A6 ||
        codePoint == 0x1F3A7 ||
        codePoint == 0x1F3A8 ||
        codePoint == 0x1F3A9 ||
        codePoint == 0x1F3AA ||
        codePoint == 0x1F3AB ||
        codePoint == 0x1F3AC ||
        codePoint == 0x1F3AD ||
        codePoint == 0x1F3AE ||
        codePoint == 0x1F3AF ||
        codePoint == 0x1F3B0 ||
        codePoint == 0x1F3B1 ||
        codePoint == 0x1F3B2 ||
        codePoint == 0x1F3B3 ||
        codePoint == 0x1F3B4 ||
        codePoint == 0x1F3B5 ||
        codePoint == 0x1F3B6 ||
        codePoint == 0x1F3B7 ||
        codePoint == 0x1F3B8 ||
        codePoint == 0x1F3B9 ||
        codePoint == 0x1F3BA ||
        codePoint == 0x1F3BB ||
        codePoint == 0x1F3BC ||
        codePoint == 0x1F3BD ||
        codePoint == 0x1F3BE ||
        codePoint == 0x1F3BF ||
        codePoint == 0x1F3C0 ||
        codePoint == 0x1F3C1 ||
        codePoint == 0x1F3C2 ||
        codePoint == 0x1F3C3 ||
        codePoint == 0x1F3C4 ||
        codePoint == 0x1F3C6 ||
        codePoint == 0x1F3C8 ||
        codePoint == 0x1F3CA ||
        codePoint == 0x1F3E0 ||
        codePoint == 0x1F3E1 ||
        codePoint == 0x1F3E2 ||
        codePoint == 0x1F3E3 ||
        codePoint == 0x1F3E5 ||
        codePoint == 0x1F3E6 ||
        codePoint == 0x1F3E7 ||
        codePoint == 0x1F3E8 ||
        codePoint == 0x1F3E9 ||
        codePoint == 0x1F3EA ||
        codePoint == 0x1F3EB ||
        codePoint == 0x1F3EC ||
        codePoint == 0x1F3ED ||
        codePoint == 0x1F3EE ||
        codePoint == 0x1F3EF ||
        codePoint == 0x1F3F0 ||
        codePoint == 0x1F40C ||
        codePoint == 0x1F40D ||
        codePoint == 0x1F40E ||
        codePoint == 0x1F411 ||
        codePoint == 0x1F412 ||
        codePoint == 0x1F414 ||
        codePoint == 0x1F417 ||
        codePoint == 0x1F418 ||
        codePoint == 0x1F419 ||
        codePoint == 0x1F41A ||
        codePoint == 0x1F41B ||
        codePoint == 0x1F41C ||
        codePoint == 0x1F41D ||
        codePoint == 0x1F41E ||
        codePoint == 0x1F41F ||
        codePoint == 0x1F420 ||
        codePoint == 0x1F421 ||
        codePoint == 0x1F422 ||
        codePoint == 0x1F423 ||
        codePoint == 0x1F424 ||
        codePoint == 0x1F425 ||
        codePoint == 0x1F426 ||
        codePoint == 0x1F427 ||
        codePoint == 0x1F428 ||
        codePoint == 0x1F429 ||
        codePoint == 0x1F42B ||
        codePoint == 0x1F42C ||
        codePoint == 0x1F42D ||
        codePoint == 0x1F42E ||
        codePoint == 0x1F42F ||
        codePoint == 0x1F430 ||
        codePoint == 0x1F431 ||
        codePoint == 0x1F432 ||
        codePoint == 0x1F433 ||
        codePoint == 0x1F434 ||
        codePoint == 0x1F435 ||
        codePoint == 0x1F436 ||
        codePoint == 0x1F437 ||
        codePoint == 0x1F438 ||
        codePoint == 0x1F439 ||
        codePoint == 0x1F43A ||
        codePoint == 0x1F43B ||
        codePoint == 0x1F43C ||
        codePoint == 0x1F43D ||
        codePoint == 0x1F43E ||
        codePoint == 0x1F440 ||
        codePoint == 0x1F442 ||
        codePoint == 0x1F443 ||
        codePoint == 0x1F444 ||
        codePoint == 0x1F445 ||
        codePoint == 0x1F446 ||
        codePoint == 0x1F447 ||
        codePoint == 0x1F448 ||
        codePoint == 0x1F449 ||
        codePoint == 0x1F44A ||
        codePoint == 0x1F44B ||
        codePoint == 0x1F44C ||
        codePoint == 0x1F44D ||
        codePoint == 0x1F44E ||
        codePoint == 0x1F44F ||
        codePoint == 0x1F450 ||
        codePoint == 0x1F451 ||
        codePoint == 0x1F452 ||
        codePoint == 0x1F453 ||
        codePoint == 0x1F454 ||
        codePoint == 0x1F455 ||
        codePoint == 0x1F456 ||
        codePoint == 0x1F457 ||
        codePoint == 0x1F458 ||
        codePoint == 0x1F459 ||
        codePoint == 0x1F45A ||
        codePoint == 0x1F45B ||
        codePoint == 0x1F45C ||
        codePoint == 0x1F45D ||
        codePoint == 0x1F45E ||
        codePoint == 0x1F45F ||
        codePoint == 0x1F460 ||
        codePoint == 0x1F461 ||
        codePoint == 0x1F462 ||
        codePoint == 0x1F463 ||
        codePoint == 0x1F464 ||
        codePoint == 0x1F466 ||
        codePoint == 0x1F467 ||
        codePoint == 0x1F468 ||
        codePoint == 0x1F469 ||
        codePoint == 0x1F46A ||
        codePoint == 0x1F46B ||
        codePoint == 0x1F46E ||
        codePoint == 0x1F46F ||
        codePoint == 0x1F470 ||
        codePoint == 0x1F471 ||
        codePoint == 0x1F472 ||
        codePoint == 0x1F473 ||
        codePoint == 0x1F474 ||
        codePoint == 0x1F475 ||
        codePoint == 0x1F476 ||
        codePoint == 0x1F477 ||
        codePoint == 0x1F478 ||
        codePoint == 0x1F479 ||
        codePoint == 0x1F47A ||
        codePoint == 0x1F47B ||
        codePoint == 0x1F47C ||
        codePoint == 0x1F47D ||
        codePoint == 0x1F47E ||
        codePoint == 0x1F47F ||
        codePoint == 0x1F480 ||
        codePoint == 0x1F481 ||
        codePoint == 0x1F482 ||
        codePoint == 0x1F483 ||
        codePoint == 0x1F484 ||
        codePoint == 0x1F485 ||
        codePoint == 0x1F486 ||
        codePoint == 0x1F487 ||
        codePoint == 0x1F488 ||
        codePoint == 0x1F489 ||
        codePoint == 0x1F48A ||
        codePoint == 0x1F48B ||
        codePoint == 0x1F48C ||
        codePoint == 0x1F48D ||
        codePoint == 0x1F48E ||
        codePoint == 0x1F48F ||
        codePoint == 0x1F490 ||
        codePoint == 0x1F491 ||
        codePoint == 0x1F492 ||
        codePoint == 0x1F493 ||
        codePoint == 0x1F494 ||
        codePoint == 0x1F495 ||
        codePoint == 0x1F496 ||
        codePoint == 0x1F497 ||
        codePoint == 0x1F498 ||
        codePoint == 0x1F499 ||
        codePoint == 0x1F49A ||
        codePoint == 0x1F49B ||
        codePoint == 0x1F49C ||
        codePoint == 0x1F49D ||
        codePoint == 0x1F49E ||
        codePoint == 0x1F49F ||
        codePoint == 0x1F4A0 ||
        codePoint == 0x1F4A1 ||
        codePoint == 0x1F4A2 ||
        codePoint == 0x1F4A3 ||
        codePoint == 0x1F4A4 ||
        codePoint == 0x1F4A5 ||
        codePoint == 0x1F4A6 ||
        codePoint == 0x1F4A7 ||
        codePoint == 0x1F4A8 ||
        codePoint == 0x1F4A9 ||
        codePoint == 0x1F4AA ||
        codePoint == 0x1F4AB ||
        codePoint == 0x1F4AC ||
        codePoint == 0x1F4AE ||
        codePoint == 0x1F4AF ||
        codePoint == 0x1F4B0 ||
        codePoint == 0x1F4B1 ||
        codePoint == 0x1F4B2 ||
        codePoint == 0x1F4B3 ||
        codePoint == 0x1F4B4 ||
        codePoint == 0x1F4B5 ||
        codePoint == 0x1F4B8 ||
        codePoint == 0x1F4B9 ||
        codePoint == 0x1F4BA ||
        codePoint == 0x1F4BB ||
        codePoint == 0x1F4BC ||
        codePoint == 0x1F4BD ||
        codePoint == 0x1F4BE ||
        codePoint == 0x1F4BF ||
        codePoint == 0x1F4C0 ||
        codePoint == 0x1F4C1 ||
        codePoint == 0x1F4C2 ||
        codePoint == 0x1F4C3 ||
        codePoint == 0x1F4C4 ||
        codePoint == 0x1F4C5 ||
        codePoint == 0x1F4C6 ||
        codePoint == 0x1F4C7 ||
        codePoint == 0x1F4C8 ||
        codePoint == 0x1F4C9 ||
        codePoint == 0x1F4CA ||
        codePoint == 0x1F4CB ||
        codePoint == 0x1F4CC ||
        codePoint == 0x1F4CD ||
        codePoint == 0x1F4CE ||
        codePoint == 0x1F4CF ||
        codePoint == 0x1F4D0 ||
        codePoint == 0x1F4D1 ||
        codePoint == 0x1F4D2 ||
        codePoint == 0x1F4D3 ||
        codePoint == 0x1F4D4 ||
        codePoint == 0x1F4D5 ||
        codePoint == 0x1F4D6 ||
        codePoint == 0x1F4D7 ||
        codePoint == 0x1F4D8 ||
        codePoint == 0x1F4D9 ||
        codePoint == 0x1F4DA ||
        codePoint == 0x1F4DB ||
        codePoint == 0x1F4DC ||
        codePoint == 0x1F4DD ||
        codePoint == 0x1F4DE ||
        codePoint == 0x1F4DF ||
        codePoint == 0x1F4E0 ||
        codePoint == 0x1F4E1 ||
        codePoint == 0x1F4E2 ||
        codePoint == 0x1F4E3 ||
        codePoint == 0x1F4E4 ||
        codePoint == 0x1F4E5 ||
        codePoint == 0x1F4E6 ||
        codePoint == 0x1F4E7 ||
        codePoint == 0x1F4E8 ||
        codePoint == 0x1F4E9 ||
        codePoint == 0x1F4EA ||
        codePoint == 0x1F4EB ||
        codePoint == 0x1F4EE ||
        codePoint == 0x1F4F0 ||
        codePoint == 0x1F4F1 ||
        codePoint == 0x1F4F2 ||
        codePoint == 0x1F4F3 ||
        codePoint == 0x1F4F4 ||
        codePoint == 0x1F4F6 ||
        codePoint == 0x1F4F7 ||
        codePoint == 0x1F4F9 ||
        codePoint == 0x1F4FA ||
        codePoint == 0x1F4FB ||
        codePoint == 0x1F4FC ||
        codePoint == 0x1F503 ||
        codePoint == 0x1F50A ||
        codePoint == 0x1F50B ||
        codePoint == 0x1F50C ||
        codePoint == 0x1F50D ||
        codePoint == 0x1F50E ||
        codePoint == 0x1F50F ||
        codePoint == 0x1F510 ||
        codePoint == 0x1F511 ||
        codePoint == 0x1F512 ||
        codePoint == 0x1F513 ||
        codePoint == 0x1F514 ||
        codePoint == 0x1F516 ||
        codePoint == 0x1F517 ||
        codePoint == 0x1F518 ||
        codePoint == 0x1F519 ||
        codePoint == 0x1F51A ||
        codePoint == 0x1F51B ||
        codePoint == 0x1F51C ||
        codePoint == 0x1F51D ||
        codePoint == 0x1F51E ||
        codePoint == 0x1F51F ||
        codePoint == 0x1F520 ||
        codePoint == 0x1F521 ||
        codePoint == 0x1F522 ||
        codePoint == 0x1F523 ||
        codePoint == 0x1F524 ||
        codePoint == 0x1F525 ||
        codePoint == 0x1F526 ||
        codePoint == 0x1F527 ||
        codePoint == 0x1F528 ||
        codePoint == 0x1F529 ||
        codePoint == 0x1F52A ||
        codePoint == 0x1F52B ||
        codePoint == 0x1F52E ||
        codePoint == 0x1F52F ||
        codePoint == 0x1F530 ||
        codePoint == 0x1F531 ||
        codePoint == 0x1F532 ||
        codePoint == 0x1F533 ||
        codePoint == 0x1F534 ||
        codePoint == 0x1F535 ||
        codePoint == 0x1F536 ||
        codePoint == 0x1F537 ||
        codePoint == 0x1F538 ||
        codePoint == 0x1F539 ||
        codePoint == 0x1F53A ||
        codePoint == 0x1F53B ||
        codePoint == 0x1F53C ||
        codePoint == 0x1F53D ||
        codePoint == 0x1F550 ||
        codePoint == 0x1F551 ||
        codePoint == 0x1F552 ||
        codePoint == 0x1F553 ||
        codePoint == 0x1F554 ||
        codePoint == 0x1F555 ||
        codePoint == 0x1F556 ||
        codePoint == 0x1F557 ||
        codePoint == 0x1F558 ||
        codePoint == 0x1F559 ||
        codePoint == 0x1F55A ||
        codePoint == 0x1F55B ||
        codePoint == 0x1F5FB ||
        codePoint == 0x1F5FC ||
        codePoint == 0x1F5FD ||
        codePoint == 0x1F5FE ||
        codePoint == 0x1F5FF ||
        codePoint == 0x1F601 ||
        codePoint == 0x1F602 ||
        codePoint == 0x1F603 ||
        codePoint == 0x1F604 ||
        codePoint == 0x1F605 ||
        codePoint == 0x1F606 ||
        codePoint == 0x1F609 ||
        codePoint == 0x1F60A ||
        codePoint == 0x1F60B ||
        codePoint == 0x1F60C ||
        codePoint == 0x1F60D ||
        codePoint == 0x1F60F ||
        codePoint == 0x1F612 ||
        codePoint == 0x1F613 ||
        codePoint == 0x1F614 ||
        codePoint == 0x1F616 ||
        codePoint == 0x1F618 ||
        codePoint == 0x1F61A ||
        codePoint == 0x1F61C ||
        codePoint == 0x1F61D ||
        codePoint == 0x1F61E ||
        codePoint == 0x1F620 ||
        codePoint == 0x1F621 ||
        codePoint == 0x1F622 ||
        codePoint == 0x1F623 ||
        codePoint == 0x1F624 ||
        codePoint == 0x1F625 ||
        codePoint == 0x1F628 ||
        codePoint == 0x1F629 ||
        codePoint == 0x1F62A ||
        codePoint == 0x1F62B ||
        codePoint == 0x1F62D ||
        codePoint == 0x1F630 ||
        codePoint == 0x1F631 ||
        codePoint == 0x1F632 ||
        codePoint == 0x1F633 ||
        codePoint == 0x1F635 ||
        codePoint == 0x1F637 ||
        codePoint == 0x1F638 ||
        codePoint == 0x1F639 ||
        codePoint == 0x1F63A ||
        codePoint == 0x1F63B ||
        codePoint == 0x1F63C ||
        codePoint == 0x1F63D ||
        codePoint == 0x1F63E ||
        codePoint == 0x1F63F ||
        codePoint == 0x1F640 ||
        codePoint == 0x1F645 ||
        codePoint == 0x1F646 ||
        codePoint == 0x1F647 ||
        codePoint == 0x1F648 ||
        codePoint == 0x1F649 ||
        codePoint == 0x1F64A ||
        codePoint == 0x1F64B ||
        codePoint == 0x1F64C ||
        codePoint == 0x1F64D ||
        codePoint == 0x1F64E ||
        codePoint == 0x1F64F ||
        codePoint == 0x1F680 ||
        codePoint == 0x1F683 ||
        codePoint == 0x1F684 ||
        codePoint == 0x1F685 ||
        codePoint == 0x1F687 ||
        codePoint == 0x1F689 ||
        codePoint == 0x1F68C ||
        codePoint == 0x1F68F ||
        codePoint == 0x1F691 ||
        codePoint == 0x1F692 ||
        codePoint == 0x1F693 ||
        codePoint == 0x1F695 ||
        codePoint == 0x1F697 ||
        codePoint == 0x1F699 ||
        codePoint == 0x1F69A ||
        codePoint == 0x1F6A2 ||
        codePoint == 0x1F6A4 ||
        codePoint == 0x1F6A5 ||
        codePoint == 0x1F6A7 ||
        codePoint == 0x1F6A8 ||
        codePoint == 0x1F6A9 ||
        codePoint == 0x1F6AA ||
        codePoint == 0x1F6AB ||
        codePoint == 0x1F6AC ||
        codePoint == 0x1F6AD ||
        codePoint == 0x1F6B2 ||
        codePoint == 0x1F6B6 ||
        codePoint == 0x1F6B9 ||
        codePoint == 0x1F6BA ||
        codePoint == 0x1F6BB ||
        codePoint == 0x1F6BC ||
        codePoint == 0x1F6BD ||
        codePoint == 0x1F6BE ||
        codePoint == 0x1F6C0;
    }

}
