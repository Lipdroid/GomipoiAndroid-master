package app.define;

import java.io.Serializable;

/**
 *
 */
public enum SettingsType implements Serializable {
    UNKNOWN(0),
    WITH_DESCRIPT(1),
    WITH_SWITCH(2),
    ONLY_TITLE(3),
    ;

    private int value;

    SettingsType(int value) {
        this.value = value;
    }

    public static SettingsType valueOf(int value) {
        for (SettingsType type : values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        return UNKNOWN;
    }

    public final int getValue() {
        return value;
    }

}
