package app.define;

import java.io.Serializable;

/**
 * アニメーションID
 */
public enum AnimationId implements Serializable {
    UNKNOWN(0),
    ALPHAANIMATION(1),
    ;


    int mValue;

    AnimationId(int mValue) {
        this.mValue = mValue;
    }

    public static AnimationId valueOf(int value) {
        for (AnimationId item : values()) {
            if (item.getValue() == value) {
                return item;
            }
        }
        return UNKNOWN;
    }

    public int getValue() {
        return mValue;
    }

}
