package app.wheel.lib.number;

import android.content.Context;
import android.util.AttributeSet;

import app.wheel.lib.WheelView;

/**
 */
public class NumberWheelView extends WheelView {

    // ------------------------------
    // Member
    // ------------------------------
    private int mDigit;

    // ------------------------------
    // Constructor
    // ------------------------------
    public NumberWheelView(Context context) {
        super(context);
    }

    public NumberWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NumberWheelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    /**
     * 桁数をセットする
     */
    public void setDigit(int digit) {
        mDigit = digit;
    }

    /**
     * 桁数を返す
     */
    public int getDigit() {
        return mDigit;
    }

}
