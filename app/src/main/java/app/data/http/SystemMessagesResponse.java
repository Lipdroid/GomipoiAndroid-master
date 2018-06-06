package app.data.http;

import android.content.res.Resources;

import com.topmission.gomipoi.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lib.json.JsonUtils;
import lib.log.DebugLog;

/**
 */
public class SystemMessagesResponse extends MessagesResponse implements Serializable {

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
    // Define
    // ------------------------------
    public static final String API = "system_messages";

    private static final int TYPE_ADD_FRIEND_BONUS = 1;
    private static final int TYPE_ADD_FRIEND_CHARA_BONUS = 2;
    private static final int TYPE_ADD_FRIEND_STAGE_BONUS = 3;
    private static final int TYPE_ADD_FRIEND_BONUS_TICKET = 4;

    // ------------------------------
    // Member
    // ------------------------------
    public int systemMessageId;
    public int messageType;
    public int bonusFriendCount;
    public int bonusJewelCount;
    public int bonusTicketCount;

    // ------------------------------
    // Constructor
    // ------------------------------
    public SystemMessagesResponse(HashMap<String, Object> map) {
        this.systemMessageId = JsonUtils.getIntElement(map, "system_message_id");
        this.messageType = JsonUtils.getIntElement(map, "message_type");
        this.bonusFriendCount = JsonUtils.getIntElement(map, "friendship_bonus_friend_count");
        this.bonusJewelCount = JsonUtils.getIntElement(map, "friendship_bonus_jewel_count");
        this.sentAt = JsonUtils.getStringElement(map, "sent_at");

        DebugLog.i("SystemMessagesResponse - Message Type: " + this.messageType);
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    /**
     * friend_messagesのリストを返す
     */
    public static List<Object> getList(HashMap<String, Object> jsonData) {
        if (jsonData == null) {
            return new ArrayList<>();
        }
        return (List<Object>)jsonData.get("system_messages");
    }

    /**
     * リストに表示する文字列を返す
     */
    public final String getMessage(Resources resources) {
        switch (messageType) {
            case TYPE_ADD_FRIEND_BONUS: {
                return resources.getString(R.string.friend_message, bonusFriendCount, bonusJewelCount);
            }

            case TYPE_ADD_FRIEND_CHARA_BONUS: {
                return resources.getString(R.string.friend_chara_bonus_message);
            }

            case TYPE_ADD_FRIEND_STAGE_BONUS: {
                return resources.getString(R.string.friend_stage_bonus_message);
            }

            case TYPE_ADD_FRIEND_BONUS_TICKET: {
                return resources.getString(R.string.friend_message_bonus_ticket, bonusFriendCount);
            }
        }
        return "";
    }

    /**
     * 招待のメッセージかを返す
     */
    public final boolean isAddFriendBonus() {
        return messageType == TYPE_ADD_FRIEND_BONUS;
    }

    public final boolean isBonusTicket() {
        return messageType == TYPE_ADD_FRIEND_BONUS_TICKET;
    }

    public final String getTestLog() {
        return "" + systemMessageId;
    }

}
