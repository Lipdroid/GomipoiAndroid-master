package lib.timer;

/**
 * 待機タイマー関連のコールバック
 * @author Yuya Hirayama (foot-loose)
 *
 */
public interface OnWaitTimerListener {
	
	// ------------------------------
	// Abstract
	// ------------------------------
	/**
	 * タイマー終了時のコールバック
	 * @param data
	 */
	public void onCompletedTimer(Object data);
	
}
