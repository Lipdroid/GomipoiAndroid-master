package lib.fps;

import java.io.Serializable;

/**
 * FpsManagerのレスポンスデータ
 */
public class FpsResponse implements Serializable {

    // ------------------------------
    // Member
    // ------------------------------
    private long prevTime;
    private long currentTime;
    private double maxFPS;

    // ------------------------------
    // Constructor
    // ------------------------------
    /**
     * Constructor
     * @param maxFPS [double]
     * @param prevTime [long]
     */
    public FpsResponse(double maxFPS, long prevTime) {
        this.maxFPS = maxFPS;
        this.prevTime = prevTime;
        this.currentTime = System.currentTimeMillis();
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    /**
     * チェックした時間を返す
     */
    public final long getCheckTime() {
        return currentTime;
    }

    /**
     * 経過時間を返す
     */
    public final long getDifTime() {
        long dif = 0L;
        if (prevTime > 0) {
            dif = currentTime - prevTime;
        }
        return dif;
    }

    /**
     * 処理を行うかを返す
     */
    public final boolean isNeedDoMethod() {
        double fps = getFps();
        return (fps == 0 || fps <= maxFPS * 3.0f / 2.0f); // 閾値を(maxFPS/2)としてみる
    }

    /**
     * 確認ログ用の文字列を返す
     */
    public final String getTestLog() {
        long dif = getDifTime();
        double fps = getFps();
        return "経過時間 = " + dif + ", FPS = " + fps;
    }

    // ------------------------------
    // Function
    // ------------------------------
    /**
     * FPSを返す
     */
    private double getFps() {
        double fps = 0.0f;
        long dif = getDifTime();
        if (dif > 0L) {
            fps = 1000.0f / (double)dif;
        }
        return fps;
    }

}
