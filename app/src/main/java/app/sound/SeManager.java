package app.sound;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

import app.application.GBApplication;
import app.define.SeData;

/**
 * 効果音再生の管理クラス
 */
public class SeManager {

    // ------------------------------
    // Member
    // ------------------------------
    private Context mContext;
    private SoundPool mSoundPool;
    private List<Integer> mSoundList;

    // ------------------------------
    // Constructor
    // ------------------------------
    public SeManager(Context context) {
        mContext = context;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes attr = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            mSoundPool = new SoundPool.Builder()
                    .setAudioAttributes(attr)
                    .setMaxStreams(SeData.size())
                    .build();
        } else {
            mSoundPool = new SoundPool(SeData.size(), AudioManager.STREAM_MUSIC, 0);
        }

        mSoundList = new ArrayList<>();
        for (SeData se : SeData.values()) {
            if (se.getResourceId() == 0)
            {
                continue;
            }

            mSoundList.add(se.getSeId(), mSoundPool.load(context, se.getResourceId(), 1));
        }
    }

    // ------------------------------
    // Destructor
    // ------------------------------
    @Override
    protected void finalize() throws Throwable {
        try {
            super.finalize();
        } finally {
            mSoundPool.release();
            mSoundPool = null;

            mSoundList.clear();
            mSoundList = null;
        }
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    /**
     * SEを再生する
     * @param se [SeData]
     */
    public final boolean playSe(SeData se) {
        if (!((GBApplication) mContext).getPreferenceManager().isSeEnabled()) {
            return false;
        }

        if (mSoundPool == null) {
            return false;
        }

        if (mSoundList == null || se.getSeId() < 0 || se.getSeId() >= mSoundList.size()) {
            return false;
        }

        mSoundPool.play(mSoundList.get(se.getSeId()), 1.0f, 1.0f, 0, 0, 1.0f);
        return true;
    }

}
