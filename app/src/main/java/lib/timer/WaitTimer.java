package lib.timer;

import java.util.Timer;

/**
 * 待機タイマークラス
 * @author Yuya Hirayama (foot-loose)
 *
 */
public class WaitTimer {

	// ------------------------------
	// Define
	// ------------------------------
	private final long dInterval;
	private final OnWaitTimerListener dListener;
	
	// ------------------------------
	// Member
	// ------------------------------
	private Timer mTimer;
	
	// ==============================
	// Constructor
	// ==============================
	public WaitTimer(long interval, OnWaitTimerListener listener) {
		dInterval = interval;
		dListener = listener;
	}
	
	@Override
	protected void finalize() throws Throwable {
		stop();
		super.finalize();
	}
	
	// ==============================
	// Accesser
	// ==============================
	/**
	 * タイマーを開始する
	 */
	public void start() {
		stop();
		
		mTimer = new Timer();
		mTimer.schedule(new WaitTimerTask(dWaitTimerTaskListener), dInterval);
	}
	
	/**
	 * タイマーを停止する
	 */
	public void stop() {
		if (mTimer == null)
			return;
		
		mTimer.cancel();
		mTimer = null;
	}
	
	// ==============================
	// Event
	// ==============================
	/**
	 * タイマータスクのコールバック
	 */
	private final OnWaitTimerListener dWaitTimerTaskListener = new OnWaitTimerListener() {
		
		@Override
		public void onCompletedTimer(Object data) {
			stop();
			if (dListener != null)
				dListener.onCompletedTimer(data);
		}
	};
}
