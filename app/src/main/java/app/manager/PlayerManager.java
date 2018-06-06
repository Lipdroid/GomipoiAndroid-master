package app.manager;

import app.data.http.NewsTopicCheckResponse;

/**
 */
public class PlayerManager {

    // ------------------------------
    // Member
    // ------------------------------
    private NewsTopicCheckResponse mNewsTopicCheckResponse;
    private boolean mIsShownNewsDialog;
    private boolean mIsShownNotice1Dialog = false;
    private boolean mIsShownNotice2Dialog = false;
    private boolean mIsShownNotice3Dialog = false;

    // ------------------------------
    // Accesser
    // ------------------------------
    //
    // NewsTopicCheckResponse
    // ------------------------------
    public final void setNewsTopicCheckResponse(NewsTopicCheckResponse response) {
        mNewsTopicCheckResponse = response;
    }

    public final void shownNewsDialog() {
        mIsShownNewsDialog = true;
    }

    public final boolean isNeedShowNewsDialog() {
        if (mNewsTopicCheckResponse == null) {
            return false;
        }
        return (getNewsUrl() != null && !mIsShownNewsDialog);
    }

    public final String getNewsUrl() {
        if (mNewsTopicCheckResponse == null) {
            return null;
        }
        return mNewsTopicCheckResponse.news_url;
    }

    public final void shownNotice1Dialog() {
        mIsShownNotice1Dialog = true;
    }
    public final boolean isNeedShowNotice1Dialog() {
        return !mIsShownNotice1Dialog;
    }

    public final void shownNotice2Dialog() {
        mIsShownNotice2Dialog = true;
    }
    public final boolean isNeedShowNotice2Dialog() {
        return !mIsShownNotice2Dialog;
    }

    public final void shownNotice3Dialog() {
        mIsShownNotice3Dialog = true;
    }

    public final boolean isNeedShowNotice3Dialog() {
        return !mIsShownNotice3Dialog; // uncomment this to show the notice3 dialog.
        // return mIsShownNotice3Dialog;
    }
}
