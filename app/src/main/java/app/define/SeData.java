package app.define;


import com.topmission.gomipoi.R;

import java.io.Serializable;

/**
 * 効果音データ
 */
public enum SeData implements Serializable {
    NONE(-1, 0),
    NO(0, R.raw.cancel2),
    YES(1, R.raw.suck1),
    PAGE(2, R.raw.page07),
    BROOM(3, R.raw.broom),
    LEVELUP(4, R.raw.levelup),
    GEM(5, R.raw.coin),
    ;

    // ------------------------------
    // Member
    // ------------------------------
    int mId;
    int mResId;

    // ------------------------------
    // Constructor
    // ------------------------------
    SeData(int mId, int mResId) {
        this.mId = mId;
        this.mResId = mResId;
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    /**
     * IDからSeDataのインスタンスを返す
     * @param id [int]
     */
    public static SeData valueOf(int id) {
        for (SeData se : values()) {
            if (se.mId == id) {
                return se;
            }
        }
        return NONE;
    }

    /**
     * 定義されているSEの数を返す
     */
    public static int size() {
        return values().length;
    }

    /**
     * IDを返す
     */
    public final int getSeId() {
        return mId;
    }

    /**
     * ResourceIDを返す
     */
    public final int getResourceId() {
        return mResId;
    }
}
