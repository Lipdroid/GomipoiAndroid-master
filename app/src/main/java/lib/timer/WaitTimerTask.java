package lib.timer;

import android.os.Handler;

import java.util.TimerTask;

/**
 * 待機タイマーのタスク処理クラス
 * @author Yuya Hirayama (foot-loose)
 *
 */
/*packaged*/ class WaitTimerTask extends TimerTask {

	// ------------------------------
	// Define
	// ------------------------------
	private final OnWaitTimerListener dListener;
	
	// ------------------------------
	// Member
	// ------------------------------
	private Handler mTaskHandler;
	
	// ------------------------------
	// Constructor
	// ------------------------------
	public WaitTimerTask(OnWaitTimerListener listener) {
		dListener = listener;
		mTaskHandler = new Handler();
	}
	
	@Override
	protected void finalize() throws Throwable {
		mTaskHandler = null;
		super.finalize();
	}
	
	// ------------------------------
	// Override
	// ------------------------------
	@Override
	public void run() {
		if (dListener == null)
			return;
		
		// コールバックを呼ぶ
		mTaskHandler.post(new Runnable() {
			@Override
			public void run() {
				dListener.onCompletedTimer(null);
				mTaskHandler.removeCallbacks(this);
				mTaskHandler = null;
			}
		});
	}
}
