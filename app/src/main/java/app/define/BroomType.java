package app.define;

import java.io.Serializable;

/**
 * BroomType
 * jni/app/parameter/app_parameter.hのPartsIDに合わせること！
 */
public enum BroomType implements Serializable {
    UNKNOWN(0),
    Normal(1),
    Silver(2),
    Gold(3),
    ;

    private int mValue;

    BroomType(int value) {
        this.mValue = value;
    }

    public static BroomType valueOf(int partsId) {
        for (BroomType parts : values()) {
            if (parts.getValue() == partsId) {
                return parts;
            }
        }
        return UNKNOWN;
    }

    public int getValue() {
        return mValue;
    }
}
