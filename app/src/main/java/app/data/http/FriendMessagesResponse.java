package app.data.http;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import lib.date.DateUtils;
import lib.json.JsonUtils;

/**
 */
public class FriendMessagesResponse extends MessagesResponse implements Serializable {

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
    public static final String API = "friend_messages";

    private static final int TYPE_INVITE = 1;
    private static final int TYPE_RECEIVE = 2;
    private static final int TYPE_SEND = 3;

    // ------------------------------
    // Member
    // ------------------------------
    public int friendMessageId;
    public int messageType;
    public int inviteAppId;
    public int fromUserId;
    public String fromUserNickname;

    // ------------------------------
    // Constructor
    // ------------------------------
    public FriendMessagesResponse(HashMap<String, Object> map) {
        this.friendMessageId = JsonUtils.getIntElement(map, "friend_message_id");
        this.messageType = JsonUtils.getIntElement(map, "message_type");
        this.inviteAppId = JsonUtils.getIntElement(map, "invite_app_id");
        this.sentAt = JsonUtils.getStringElement(map, "sent_at");
        this.fromUserId = JsonUtils.getIntElement(map, "from_user_id");
        this.fromUserNickname = JsonUtils.getStringElement(map, "from_user_nickname");
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
        return (List<Object>)jsonData.get("friend_messages");
    }

    /**
     * リストに表示する文言を返す
     */
    public final String getMessage() {
        switch (messageType) {
            case TYPE_INVITE: {
                return fromUserNickname + "さんから" + getAppName() + "の招待が届きました";
            }

            case TYPE_RECEIVE: {
                return fromUserNickname + "さんから「業者に電話」が届きました";
            }

            case TYPE_SEND: {
                return fromUserNickname + "さんから「業者に電話」のおねだりが届きました";
            }
        }
        return "";
    }

    /**
     * 招待かどうかを返す
     */
    public final boolean isInvite() {
        return messageType == TYPE_INVITE;
    }

    /**
     * 受け取るかどうかを返す
     */
    public final boolean isReceived() {
        return messageType == TYPE_RECEIVE;
    }

    /**
     * あげるかどうかを返す
     */
    public final boolean isSend() {
        return messageType == TYPE_SEND;
    }

    public final String getTestLog() {
        return "nickname=" + fromUserNickname + "\nmessage_type=" + String.valueOf(messageType);
    }

    // ------------------------------
    // Function
    // ------------------------------
    /**
     * アプリ名を返す
     */
    private String getAppName() {
        switch(inviteAppId) {
            case 1001: {
                return "じゃんPOIアプリ";
            }

            case 1002: {
                return "ゴミ箱にPOIアプリ";
            }
        }
        return "";
    }

}
