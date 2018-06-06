package app.data.friend;

import java.io.Serializable;

import app.data.http.FriendMessagesResponse;
import app.data.http.MessagesResponse;
import app.define.FriendActionCode;

/**
 * FriendMessageDialogのレスポンス
 */
public class FriendMessageDialogResponse implements Serializable {

    // ------------------------------
    // Member
    // ------------------------------
    public FriendActionCode responseCode;
    public MessagesResponse targetData;

    // ------------------------------
    // Constructor
    // ------------------------------
    public FriendMessageDialogResponse(FriendActionCode responseCode, MessagesResponse targetData) {
        this.responseCode = responseCode;
        this.targetData = targetData;
    }

}
