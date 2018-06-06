package app.manager;

import lib.timer.looping.LoopingTimer;
import lib.timer.looping.OnLoopingTimerListener;

/**
 */
public class GameManager {

    // ------------------------------
    // Member
    // ------------------------------
    private LoopingTimer mLoopingTimer;
    private OnLoopingTimerListener mTimerListener;

    // ------------------------------
    // Constructor
    // ------------------------------
    public GameManager(OnLoopingTimerListener timerListener) {
        mTimerListener = timerListener;
    }

    // ------------------------------
    // Override
    // ------------------------------
    @Override
    protected void finalize() throws Throwable {
        try {
            super.finalize();
        } finally {
            stopTimer();
            mTimerListener = null;
        }
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    public void onResume() {
        startTime();
    }

    public void onPause() {
        stopTimer();
    }

    // ------------------------------
    // Function
    // ------------------------------
    private void startTime() {
        mLoopingTimer = new LoopingTimer(mTimerListener);
        mLoopingTimer.start(0, 1000L);
    }

    private void stopTimer() {
        if (mLoopingTimer != null) {
            mLoopingTimer.setOnLoopingTimerListener(null);
            mLoopingTimer.stop();
            mLoopingTimer = null;
        }
    }

}
