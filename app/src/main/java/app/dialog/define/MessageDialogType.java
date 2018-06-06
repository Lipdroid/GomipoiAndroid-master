package app.dialog.define;

import java.io.Serializable;

/**
 * MessageDialogのタイプ
 */
public enum MessageDialogType implements Serializable {
    UNKNOWN(0),
    ONE_BUTTON(1),
    TWO_BUTTON(2),
    ;

    // ------------------------------
    // Member
    // ------------------------------
    int mValue;

    // ------------------------------
    // Constructor
    // ------------------------------
    MessageDialogType(int mValue) {
        this.mValue = mValue;
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    public static MessageDialogType valueOf(int value) {
        for (MessageDialogType item : values()) {
            if (item.getValue() == value) {
                return item;
            }
        }
        return UNKNOWN;
    }

    public int getValue() {
        return mValue;
    }

    public boolean isNeedVisibleRightButton() {
        return (mValue == TWO_BUTTON.getValue());
    }
}
