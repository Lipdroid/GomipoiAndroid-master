package app.define;

import java.io.Serializable;

/**
 * Activity遷移コード
 */
public enum ChangeActivityCode implements Serializable {
    UNKNOWN(0),
    Top(1),
    Game(2),
    Ranking(3),
    Exchange(4),
    Shop(5),
    Settings(6),
    Help(7),
    SNS(8),
    News(9),
    CaptureShare(10),
    Friend(11),
    InstallOther(12),
    Info(13),
    Contact(14),
    ReadQR(15),
    ShowQR(16),
    ;

    int mValue;

    ChangeActivityCode(int mValue) {
        this.mValue = mValue;
    }

    public static ChangeActivityCode valueOf(int value) {
        for (ChangeActivityCode code : values()) {
            if (code.getValue() == value) {
                return code;
            }
        }
        return UNKNOWN;
    }

    public final int getValue() {
        return mValue;
    }
}
