package app.data.http;

import java.io.Serializable;
import java.util.Date;

import lib.date.DateUtils;

/**
 */
public abstract class MessagesResponse implements Serializable {

    // ------------------------------
    // Enum
    // ------------------------------
    public enum ResultCode implements Serializable {
        UNKNOWN(-1),
        OK(0),
        ;

        private int mValue;

        ResultCode(int mValue) {
            this.mValue = mValue;
        }

        public int getValue() {
            return mValue;
        }

        public static ResultCode valueOf(int value) {
            for (ResultCode code : values()) {
                if (code.getValue() == value) {
                    return code;
                }
            }
            return UNKNOWN;
        }
    }

    // ------------------------------
    // Member
    // ------------------------------
    public String sentAt;

    // ------------------------------
    // Constructor
    // ------------------------------
    public MessagesResponse() {
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    public final Date getDate() {
        return DateUtils.valueOf(this.sentAt, "yyyy-MM-dd'T'kk:mm:ss'Z'");
    }

    /**
     * メッセージの日付文字列を返す
     * 〜1時間 : 1時間以内
     * 〜24時間 : ◯時間前
     * 24時間〜 : ◯日前
     */
    public final String getDateText(String serverDate) {
        Date current = DateUtils.valueOf(serverDate, "yyyy-MM-dd'T'kk:mm:ss");
        Date sent = DateUtils.valueOf(this.sentAt, "yyyy-MM-dd'T'kk:mm:ss'Z'");
        if (current == null || sent == null) {
            return "";
        }

        long currentTime = current.getTime();
        long sentTime = sent.getTime();
        long diff = currentTime - sentTime;

        long diffDay = diff / (1000L * 60L * 60L * 24L );
        if (diffDay <= 0L) {
            long diffHour = diff / (1000L * 60L * 60L);

            // 〜1時間 : 1時間以内
            if (diffHour <= 0L) {
                return "1時間以内";
            }

            // 〜24時間 : ◯時間前
            return String.valueOf(diffHour) + "時間前";
        }

        return String.valueOf(diffDay) + "日前";
    }

    public abstract String getTestLog();

}
