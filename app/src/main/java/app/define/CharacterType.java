package app.define;

import java.io.Serializable;

/**
 * Created by Kurosuke on 2017/07/14.
 */

public enum CharacterType implements Serializable {
    UNKNOWN(-1),
    POIKO(0),
    OTON(31),
    KOTATSU(62),
    ;


    private int mValue;

    CharacterType(int value) {
        this.mValue = value;
    }

    public static CharacterType valueOf(int partsId) {
        for (CharacterType parts : values()) {
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
