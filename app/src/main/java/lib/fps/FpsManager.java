package lib.fps;

import lib.log.DebugLog;

/**
 * Fps管理クラス
 */
public class FpsManager {

    // ------------------------------
    // Define
    // ------------------------------
    private final double FPS = 60.0f;

    // ------------------------------
    // Member
    // ------------------------------
    private long mCheckedTime;
    private long mSumTime;
    private long mSumCount;

    // ------------------------------
    // Constructor
    // ------------------------------
    /**
     * Constructor
     */
    public FpsManager() {
        mCheckedTime = 0L;
        mSumTime = 0L;
        mSumCount = 0L;
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    /**
     * FPSをチェックする
     */
    public final void checkFPS() {
        FpsResponse fpsResponse = new FpsResponse(FPS, mCheckedTime);
        mCheckedTime = fpsResponse.getCheckTime();

        if (Long.MAX_VALUE - mSumTime <= fpsResponse.getDifTime()) {
            mSumTime = 0L;
            mSumCount = 0L;
        }

        mSumTime += fpsResponse.getDifTime();
        mSumCount += 1;

        DebugLog.i(fpsResponse.getTestLog());
        DebugLog.i(String.valueOf(mSumCount) + ".平均経過時間 = " + (double) mSumTime / (double) mSumCount);
    }

}
