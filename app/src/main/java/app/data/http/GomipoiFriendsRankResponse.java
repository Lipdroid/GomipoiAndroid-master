package app.data.http;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lib.json.JsonUtils;

/**
 * Created by jerro on 08/02/2018.
 */

public class GomipoiFriendsRankResponse implements Serializable, Comparable<GomipoiFriendsRankResponse> {

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
    public static String API = "NA";

    // ------------------------------
    // Member
    // ------------------------------
    public int id;
    public String nickname;
    public int ranking;
    public int point;
    private String mExtraText;

    // ------------------------------
    // Constructor
    // ------------------------------
    public GomipoiFriendsRankResponse(HashMap<String, Object> map) {
        if (map == null) {
            return;
        }
        this.id = JsonUtils.getIntElement(map, "id");
        this.nickname = JsonUtils.getStringElement(map, "nickname");
        this.ranking = JsonUtils.getIntElement(map, "ranking");
        this.point = JsonUtils.getIntElement(map, "point");
    }

    /**
     * date
     */
    public static HashMap<String, Object> getRankingDate(HashMap<String, Object> jsonData) {
        if (jsonData == null) {
            return null;
        }

        if (jsonData.get("ranking") == null) {
            return null;
        }

        return (HashMap)jsonData.get("ranking");
    }

    /**
     * self_rank
     */
    public static HashMap<String, Object> getSelfRank(HashMap<String, Object> jsonData) {
        if (jsonData == null) {
            return null;
        }

        if (jsonData.get("self_user") == null) {
            return null;
        }

        return (HashMap)jsonData.get("self_user");
    }

    /**
     * list of users
     */
    public static List<Object> getList(HashMap<String, Object> jsonData) {
        if (jsonData == null) {
            return new ArrayList<>();
        }
        return (List<Object>)jsonData.get("users");
    }

    public int getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public int getRanking() {
        return ranking;
    }

    public int getPoint() {
        return point;
    }

    public void setExtraText(String text) {
        this.mExtraText = text;
    }

    public String getExtraText() {
        return this.mExtraText;
    }

    @Override
    public int compareTo(@NonNull GomipoiFriendsRankResponse o) {
        if (this.ranking > o.ranking) {
            return 1;
        } else if (this.ranking < o.ranking) {
            return -1;
        } else {
            return 0;
        }
        // return 0;
    }
}
