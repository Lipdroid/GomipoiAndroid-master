package app.define;

import java.io.Serializable;

/**
 * PartsのID
 * jni/app/parameter/app_parameter.hのPartsIDに合わせること！
 */
public enum PartsId implements Serializable {
    UNKNOWN(0),
    ;

    private int mPartsId;

    PartsId(int mPartsId) {
        this.mPartsId = mPartsId;
    }

    public static PartsId valueOf(int partsId) {
        for (PartsId parts : values()) {
            if (parts.getValue() == partsId) {
                return parts;
            }
        }
        return UNKNOWN;
    }

    public int getValue() {
        return mPartsId;
    }
}
