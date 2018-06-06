package app.data.http;

import java.io.Serializable;
import java.util.HashMap;

import lib.json.JsonUtils;

/**
 */
public class NewsTopicCheckResponse implements Serializable {

    // ------------------------------
    // Enum
    // ------------------------------
    public static enum ResultCode implements Serializable {
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
    public static final String API = "news_topics/check";

    // ------------------------------
    // Member
    // ------------------------------
    public String news_url;

    // ------------------------------
    // Constructor
    // ------------------------------
    public NewsTopicCheckResponse(HashMap<String, Object> jsonData) {

        if (jsonData.get("news_url") != null) {
            this.news_url = JsonUtils.getStringElement(jsonData, "news_url");
        }
    }

    public NewsTopicCheckResponse() {
        news_url = null;
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    public String getTestLog() {
        return "news_url : " + news_url;
    }

}
