package lib.adapter;

/**
 * Adapterのコールバックインターフェース
 * @author Yuya Hirayama (foot-loose)
 *
 */
public interface OnAdapterListener {

	// ------------------------------
	// Abstract
	// ------------------------------
	/**
	 * イベント発生時のコールバック
	 * @param eventCode
	 * @param data
	 */
	public void onEvent(int eventCode, Object data);
	
}
