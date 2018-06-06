package app.data.http;

import android.content.res.Resources;

import com.topmission.gomipoi.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.number.NumberUtils;
import lib.json.JsonUtils;

/**
 */
public class GomipoiFriendsResponse implements Serializable {

    // ------------------------------
    // Enum
    // ------------------------------
    public enum ResultCode implements Serializable {
        UNKNOWN(-1),
        OK(0),
        FAILED(1),
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
    public static final String API = "gomipoi_friends";
    public static final String API_DELETE = "friends/";

    // ------------------------------
    // Member
    // ------------------------------
    public String nickname;
    public int userId;
    public int totalPoint;
    public int level;
    public double gomiGauge;
    public int maxGomiGauge;
    public int canInvite;
    public int canPresent;
    public int canPresentRequest;

    private boolean mIsNotInstalled;

    // ------------------------------
    // Constructor
    // ------------------------------
    public GomipoiFriendsResponse(HashMap<String, Object> map) {
        if (map == null) {
            return;
        }
        this.userId = JsonUtils.getIntElement(map, "user_id");
        this.nickname = JsonUtils.getStringElement(map, "nickname");
        this.totalPoint = JsonUtils.getIntElement(map, "total_point");
        this.level = JsonUtils.getIntElement(map, "level");
        this.gomiGauge = JsonUtils.getDoubleElement(map, "gomi_gauge");
        this.maxGomiGauge = JsonUtils.getIntElement(map, "max_gomi_gauge");
        this.canInvite = JsonUtils.getIntElement(map, "can_invite");
        this.canPresent = JsonUtils.getIntElement(map, "can_present");
        this.canPresentRequest = JsonUtils.getIntElement(map, "can_present_request");

        mIsNotInstalled = (map.get("total_point") == null
                || map.get("total_point").toString().equals("null"));
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    /**
     * self_userのマップを返す
     */
    public static HashMap<String, Object> getSelfUserMap(HashMap<String, Object> jsonData) {
        if (jsonData == null) {
            return null;
        }

        if (jsonData.get("self_user") == null) {
            return null;
        }

        return (HashMap)jsonData.get("self_user");
    }

    /**
     * friendsのリストを返す
     */
    public static List<Object> getList(HashMap<String, Object> jsonData) {
        if (jsonData == null) {
            return new ArrayList<>();
        }
        return (List<Object>)jsonData.get("friends");
    }

    /**
     * 相手がアプリをインストールしていないかを返す
     */
    public final boolean isNeedShowInvite() {
        return mIsNotInstalled;
    }

    /**
     * 招待可かを返す
     */
    public final boolean canInvite() {
        return canInvite != 0;
    }

    /**
     * プレゼント可かを返す
     */
    public final boolean canPresent() {
        if (isNeedShowInvite()) {
            return false;
        }
        return canPresent != 0;
    }

    /**
     * おねだり可かを返す
     */
    public final boolean canPresentRequest() {
        if (isNeedShowInvite()) {
            return false;
        }
        return canPresentRequest != 0;
    }

    /**
     * ポイントの文字列を返す
     */
    public final String getTotalPointText() {
        return String.valueOf(NumberUtils.getNumberFormatText(totalPoint));
    }

    /**
     * あげるダイアログの文言を返す
     */
    public final String getGiveDialogText() {
        return nickname + "さんに\n「業者に電話」を\nあげますか？";
    }

    /**
     * おねだりダイアログの文言を返す
     */
    public final String getGimmeDialogText() {
        return nickname + "さんに\n「業者に電話」を\nおねだりしますか？";
    }

    /**
     * 招待ダイアログの文言を返す
     */
    public final String getInviteDialogText() {
        return nickname + "さんを\nこのアプリに招待\nしますか？";
    }

    /**
     * リストのレベル文言を返す
     */
    public final String getLevelText(Resources resources) {
        return resources.getString(R.string.friend_level, level);
    }

    public final String getTestLog() {
        return "nickname=" + nickname;
    }

    // ------------------------------
    // Function
    // ------------------------------
    /**
     * ゴミ箱ゲージの文字列を返す
     */
    private String getGauge() {
        if (maxGomiGauge == 0) {
            return "0％";
        }

        int percent = (int)(gomiGauge * 100.0 / maxGomiGauge);
        return String.valueOf(percent) + "％";
    }

}
