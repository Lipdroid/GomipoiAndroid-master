package lib.timer.looping;

/**
 * ループタイマーのコールバックインターフェース
 * @author Yuya Hirayama (foot-loose)
 *
 */
public interface OnLoopingTimerListener {

	/**
	 * Interval分待機後のコールバック
	 * @param isUIThread
	 * @param data
	 */
	public void onRun(boolean isUIThread, Object data);
	
}
