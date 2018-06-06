package app.data.http;

import java.io.Serializable;
import java.util.HashMap;

/**
 */
public class FriendsSearchByCodeParam implements Serializable {

    // ------------------------------
    // Define
    // ------------------------------
    public static final String API = "friends/search_by_code";

    // ------------------------------
    // Member
    // ------------------------------
    public String friendCode;

    // ------------------------------
    // Constructor
    // ------------------------------
    public FriendsSearchByCodeParam(String friendCode) {
        this.friendCode = friendCode;
    }

    // ------------------------------
    // Override
    // ------------------------------
    public Object cloneParam() {
        return new FriendsSearchByCodeParam(friendCode);
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    public HashMap<String, String> makeParam() {
        HashMap<String, String> map = new HashMap<>();
        map.put("friend_code", friendCode);
        return map;
    }

}
