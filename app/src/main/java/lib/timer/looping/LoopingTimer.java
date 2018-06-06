package lib.timer.looping;

import android.os.Handler;
import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 一定間隔にコールバックを返すタイマー管理クラス
 * @author Yuya Hirayama (foot-loose)
 *
 */
public class LoopingTimer {

	// ------------------------------
	// Member
	// ------------------------------
	private OnLoopingTimerListener mOnLoopingTimerListener;
	private Timer mLoopingTimer;
	private Handler mHandler;
	
	// ------------------------------
	// Constructor
	// ------------------------------
	/**
	 * Constructor
	 */
	public LoopingTimer() {
	}
	
	/**
	 * Constructor
	 * @param listener
	 */
	public LoopingTimer(OnLoopingTimerListener listener) {
		mOnLoopingTimerListener = listener;
	}
	
	// ------------------------------
	// Accesser
	// ------------------------------
	/**
	 * コールバックリスナーをセットする
	 * @param listener
	 */
	public void setOnLoopingTimerListener(OnLoopingTimerListener listener) {
		mOnLoopingTimerListener = listener;
	}
	
	public void release() {
		mOnLoopingTimerListener = null;
		if (mLoopingTimer != null) {
			mLoopingTimer.cancel();
			mLoopingTimer = null;
		}
		mHandler = null;
	}
	
	/**
	 * タイマーを開始する
	 * @param offsetTime
	 * @param interval
	 */
	public void start(long offsetTime, long interval) {
		if (mLoopingTimer != null) {
			return;
		}
		
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				// UIスレッド用のコールバック
				if (mOnLoopingTimerListener != null) {
					mOnLoopingTimerListener.onRun(true, null);
				}
			}
		};
		
		getLoopingTimer().schedule(new TimerTask() {
			@Override
			public void run() {
				// Thread用のコールバック
				if (mOnLoopingTimerListener != null) {
					mOnLoopingTimerListener.onRun(false, null);
				}
				if (mHandler != null) {
					try {
						mHandler.sendEmptyMessage(0);
					} catch (Exception e) {
					}
				}
			}
		}, offsetTime, interval);
	}
	
	/**
	 * タイマーを止める
	 */
	public void stop() {
		if (mLoopingTimer != null) {
			mLoopingTimer.cancel();
			mLoopingTimer = null;
		}
		mHandler = null;
	}
	
	// ------------------------------
	// Function
	// ------------------------------
	/**
	 * タイマーのインスタンスを返す
	 * @return
	 */
	private Timer getLoopingTimer() {
		if (mLoopingTimer == null) {
			mLoopingTimer = new Timer();
		}
		return mLoopingTimer;
	}
	
}
