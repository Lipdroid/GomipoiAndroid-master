package app.data.friend;

import java.io.Serializable;

import app.data.http.FriendsForPresentRequestResponse;
import app.define.FriendActionCode;

/**
 * FriendMessageDialogのレスポンス
 */
public class IconCanSelectDialogResponse implements Serializable {

    // ------------------------------
    // Member
    // ------------------------------
    public FriendActionCode responseCode;
    public FriendsForPresentRequestResponse targetData;

    // ------------------------------
    // Constructor
    // ------------------------------
    public IconCanSelectDialogResponse(FriendActionCode responseCode, FriendsForPresentRequestResponse targetData) {
        this.responseCode = responseCode;
        this.targetData = targetData;
    }

}
