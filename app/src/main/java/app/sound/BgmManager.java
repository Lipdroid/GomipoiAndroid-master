package app.sound;

import android.content.Context;
import android.media.MediaPlayer;

import com.topmission.gomipoi.R;

import app.application.GBApplication;


/**
 */
public class BgmManager {

    // ------------------------------
    // Enum
    // ------------------------------
    public enum BgmType {
        NORMAL,
        JIROKICHI
    }

    // ------------------------------
    // Member
    // ------------------------------
    private Context     mContext;
    private MediaPlayer mMediaPlayer;
    private int 		mOwnerCount;
    private BgmType     mBgmType;

    // ------------------------------
    // Constructor
    // ------------------------------
    public BgmManager(Context mContext) {
        this.mContext   = mContext;
        mOwnerCount 	= 0;
        mBgmType        = BgmType.NORMAL;
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    public void playAndRetain() {
        if (mContext == null) {
            return;
        }

        if (!((GBApplication) mContext).getPreferenceManager().isSoundEnabled()) {
            return;
        }

        mOwnerCount++;

        play();
    }

    public void stopAndRelease() {
        if (!((GBApplication) mContext).getPreferenceManager().isSoundEnabled()) {
            return;
        }

        mOwnerCount--;

        if (mOwnerCount == 0) {
            stop();
        }
    }

    /**
     * 設定のでのOn/Offの切り替えでBGMを止める用
     */
    public void stopAndDecreaseCount() {
        mOwnerCount--;
        stop();
    }

    // ------------------------------
    // Function
    // ------------------------------
    public void changeBgm(BgmType type) {
        if (mBgmType.equals(type)) {
            return;
        }

        mBgmType = type;
        if (mMediaPlayer != null) {
            stop();
            play();
        }
    }

    private void play() {
        if (mMediaPlayer == null) {
            int resourceId = 0;
            switch (mBgmType) {
                case NORMAL:
                    resourceId = R.raw.gomi_bgm;
                    break;

                case JIROKICHI:
                    resourceId = R.raw.jirokichi_bgm;
                    break;
            }
            mMediaPlayer = MediaPlayer.create(mContext, resourceId);
            mMediaPlayer.start();
            mMediaPlayer.setLooping(true);
        }
    }

    private void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}
