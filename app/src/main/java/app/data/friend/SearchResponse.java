package app.data.friend;

import java.io.Serializable;

import app.data.http.FriendsSearchByCodeResponse;
import app.define.FriendActionCode;

/**
 *
 */
public class SearchResponse implements Serializable {

    // ------------------------------
    // Member
    // ------------------------------
    public FriendActionCode responseCode;
    public String searchText;
    public FriendsSearchByCodeResponse requestFriendData;

    // ------------------------------
    // Constructor
    // ------------------------------
    public SearchResponse(String searchText) {
        this.responseCode = FriendActionCode.DATA_GET;
        this.searchText = searchText;
        this.requestFriendData = null;
    }

    public SearchResponse(String searchText, FriendsSearchByCodeResponse requestFriendData) {
        this.responseCode = FriendActionCode.APPLY;
        this.searchText = searchText;
        this.requestFriendData = requestFriendData;
    }

    public SearchResponse(FriendActionCode responseCode, String searchText, FriendsSearchByCodeResponse requestFriendData) {
        this.responseCode = responseCode;
        this.searchText = searchText;
        this.requestFriendData = requestFriendData;
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    public final String getSearchText() {
        if (searchText == null) {
            return "";
        }
        return searchText;
    }

}
