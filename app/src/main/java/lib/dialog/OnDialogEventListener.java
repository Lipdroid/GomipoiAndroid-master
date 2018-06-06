package lib.dialog;

/**
 * ダイアログのイベントリスナー
 * @author Yuya Hirayama (foot-loose)
 *
 */
public interface OnDialogEventListener {

	// ------------------------------
	// Abstract
	// ------------------------------
	/**
	 * イベント発生時
	 * @param dialogCode
	 * @param resultCode
	 * @param data
	 */
	public void onEvent(DialogBase dialog, int resultCode, Object data);
	
}
