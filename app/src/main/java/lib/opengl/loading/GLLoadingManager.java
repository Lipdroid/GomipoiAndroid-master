package lib.opengl.loading;

import lib.timer.OnWaitTimerListener;
import lib.timer.WaitTimer;

/**
 *
 */
public class GLLoadingManager {

    // ------------------------------
    // Define
    // ------------------------------
//    private final long MIN_INTERVAL = 1000L;
    private final long MIN_INTERVAL = 1L;

    // ------------------------------
    // Member
    // ------------------------------
    private boolean mIsLoading;
    private boolean mIsFinishedTimer;
    private boolean mIsFinishedOpenGL;
    private boolean mIsFinishedConnection;

    private WaitTimer mLoadingWaitTime;
    private OnGLLoadingManagerListener mLoadingManagerListener;

    // ------------------------------
    // Accesser
    // ------------------------------
    /**
     * OnLoadingManagerListenerをセットする
     * @param listener [OnGLLoadingManagerListener]
     */
    public final void setOnLoadingManagerListener(OnGLLoadingManagerListener listener) {
        mLoadingManagerListener = listener;
    }

    /**
     * Loading中かを返す
     */
    public final boolean isLoading() {
        return mIsLoading;
    }

    /**
     * 対象画面のonPause時に呼ぶ
     */
    public final void onPause() {
        stopTimer();
    }

    /**
     * ローディングの開始時に呼ぶ
     */
    public final void start() {
        mIsLoading = true;

        mIsFinishedTimer = false;
        mIsFinishedOpenGL = false;
        mIsFinishedConnection = true; // TODO 実際に通信の実装をするまではtrueで

        stopTimer();
        startTimer();
    }

    /**
     * OpenGLのLoading終了時に呼ぶ
     */
    public final void finishedGlLoading() {
        mIsFinishedOpenGL = true;
        if (!mIsFinishedTimer || !mIsFinishedConnection) {
            return;
        }

        finishedLoading();
    }

    /**
     * 通信のLoading終了時に呼ぶ
     */
    public final void finishedConnectionLoading() {
        mIsFinishedConnection = true;

        if (!mIsFinishedTimer || !mIsFinishedOpenGL) {
            return;
        }

        finishedLoading();
    }


    // ------------------------------
    // Function
    // ------------------------------
    /**
     * タイマーを開始する
     */
    private void startTimer() {
        mLoadingWaitTime = new WaitTimer(
                MIN_INTERVAL,
                new OnWaitTimerListener() {

                    @Override
                    public void onCompletedTimer(Object data) {
                        mIsFinishedTimer = true;

                        if (!mIsFinishedConnection || !mIsFinishedOpenGL) {
                            return;
                        }

                        finishedLoading();
                    }

                });
        mLoadingWaitTime.start();
    }

    /**
     * タイマーを停止する
     */
    private void stopTimer() {
        if (mLoadingWaitTime == null) {
            return;
        }

        mLoadingWaitTime.stop();
        mLoadingWaitTime = null;
    }

    /**
     * Loading完了時の処理
     */
    private void finishedLoading() {
        if (mLoadingManagerListener != null) {
            mLoadingManagerListener.onFinishedLoading();
        }
        mIsLoading = false;
    }

}
