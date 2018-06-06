package app.define;

import java.io.Serializable;

/**
 * Created by Herve on 2016/09/16.
 */
public enum StageType implements Serializable {
    UNKNOWN(-1),
    DefaultRoom(0),
    PoikoRoom(1),
    Garden(2),
    ;

    private int mValue;

    StageType(int value) {
        this.mValue = value;
    }

    public static StageType valueOf(int stageId) {
        for (StageType stage : values()) {
            if (stage.getValue() == stageId) {
                return stage;
            }
        }
        return UNKNOWN;
    }

    public int getValue() {
        return mValue;
    }
}
