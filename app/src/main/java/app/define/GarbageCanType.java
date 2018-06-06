package app.define;

import java.io.Serializable;

/**
 * GarbageCanType
 * jni/app/parameter/app_parameter.hのPartsIDに合わせること！
 */
public enum GarbageCanType implements Serializable {
    UNKNOWN(0),
    Normal(1),
    Big(2),
    Huge(3),
    XL(4),
    ;

    private int mValue;

    GarbageCanType(int value) {
        this.mValue = value;
    }

    public static GarbageCanType valueOf(int partsId) {
        for (GarbageCanType parts : values()) {
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
