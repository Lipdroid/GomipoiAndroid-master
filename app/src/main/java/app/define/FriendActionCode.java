package app.define;

import java.io.Serializable;

/**
 * フレンドアクションコード
 */
public enum FriendActionCode implements Serializable {
    UNKNOWN(0),
    GIVE(1),
    GIMME(2),
    INVITE(3),
    APPLY(4),
    SEND(5),
    RECEIVE(6),
    DATA_GET(7),
    USE_ITEM(8),
    DELETE(9),
    ;

    // ------------------------------
    // Member
    // ------------------------------
    private int code;

    // ------------------------------
    // Constructor
    // ------------------------------
    FriendActionCode(int code) {
        this.code = code;
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    /**
     * コードのInt値からコードを返す
     */
    public static FriendActionCode valueOf(int value) {
        for (FriendActionCode code : values()) {
            if (code.getValue() == value) {
                return code;
            }
        }
        return UNKNOWN;
    }

    /**
     * コードのInt値を返す
     */
    public final int getValue() {
        return code;
    }

}
