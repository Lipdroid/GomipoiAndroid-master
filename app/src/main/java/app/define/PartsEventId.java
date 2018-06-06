package app.define;

import java.io.Serializable;

/**
 * PartsのイベントID
 * jni/app/parameter/app_parameter.hに合わせること!
 */
public enum PartsEventId implements Serializable {
    UNKNOWN(0),
    PlayBroomSe(1),
    FullGarbages(2),
    LevelUp(3),
    ;

    private int mId;

    PartsEventId(int mId) {
        this.mId = mId;
    }

    public static PartsEventId valueOf(int id) {
        for (PartsEventId eventId : values()) {
            if (eventId.getValue() == id) {
                return eventId;
            }
        }
        return UNKNOWN;
    }

    public final int getValue() {
        return mId;
    }
}
