package app.define;

import android.content.Context;

import com.topmission.gomipoi.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import app.application.GBApplication;
import app.jni.JniBridge;

/**
 */
public enum CombinationId implements Serializable {
    UNKNOWN(0),
    Recipe1(1),
    Recipe2(2),
    Recipe3(3),
    Recipe4(4),
    Recipe5(5),
    Recipe6(6),
    Recipe7(7),
    Recipe8(8),
    Recipe9(9),
    Recipe10(10),
    Recipe11(11),
    Recipe12(12),
    Recipe13(13),
    Recipe14(14),
    ;

    private final int[] dTitleResourceArray = {
            0,
            R.drawable.text_secret_book_1, R.drawable.text_secret_book_1, R.drawable.text_secret_book_1, R.drawable.text_secret_book_1,
            R.drawable.text_secret_book_1, R.drawable.text_secret_book_1, R.drawable.text_secret_book_1, R.drawable.text_secret_book_1,
            R.drawable.text_secret_book_2, R.drawable.text_secret_book_2, R.drawable.text_secret_book_2, R.drawable.text_secret_book_2,
            R.drawable.text_secret_book_3, R.drawable.text_secret_book_3,
    };

    private int mValue;

    CombinationId(int mValue) {
        this.mValue = mValue;
    }

    public final int getValue() {
        return mValue;
    }

    public static CombinationId valueOf(int value) {
        for (CombinationId id : values()) {
            if (id.getValue() == value) {
                return id;
            }
        }
        return UNKNOWN;
    }

    public final int getResourceId() {
        return R.drawable.recipe_frame;
    }

    public final int getTextResourceId() {
        return dTitleResourceArray[mValue];
    }

    public static List<CombinationId> getPagerList(Context context) {
        List<CombinationId> list = new ArrayList<>();

        if (JniBridge.nativeGetItemOwnCount(ItemCode.BookOfSecrets1.getValue()) >= 1) {
            list.add(CombinationId.Recipe1);
            list.add(CombinationId.Recipe2);
            list.add(CombinationId.Recipe3);
            list.add(CombinationId.Recipe4);
            list.add(CombinationId.Recipe5);
            list.add(CombinationId.Recipe6);
            list.add(CombinationId.Recipe7);
            list.add(CombinationId.Recipe8);
        }

        if (JniBridge.nativeGetItemOwnCount(ItemCode.BookOfSecrets2.getValue()) >= 1) {
            list.add(CombinationId.Recipe9);
            list.add(CombinationId.Recipe10);
            list.add(CombinationId.Recipe11);
            list.add(CombinationId.Recipe12);
        }

        if (JniBridge.nativeGetItemOwnCount(ItemCode.BookOfSecrets3.getValue()) >= 1) {
            list.add(CombinationId.Recipe13);
            list.add(CombinationId.Recipe14);
        }

        return list;
    }

}
