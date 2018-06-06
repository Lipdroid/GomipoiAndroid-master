package lib.log;

import android.util.Log;

import com.topmission.gomipoi.BuildConfig;


/**
 * ログ出力クラス
 *
 */
public class DebugLog {

	// ------------------------------
	// Define
	// ------------------------------
	private static final String dDefaultTag = "DEBUG_LOG";
	
	// ------------------------------
	// Accesser
	// ------------------------------
	/**
	 * Infoログを出力
	 * @param msg [String]出力文字列
	 */
	public static void i(String msg) {
		if (!isWritable())
			return;
		
		if (msg == null)
			msg = "";
		
		Log.i(getTag(), msg);
	}
	
	/**
	 * Debugログを出力
	 * @param msg [String]出力文字列
	 */
	public static void d(String msg) {
		if (!isWritable())
			return;
		
		if (msg == null)
			msg = "";
		
		Log.d(getTag(), msg);
	}
	
	/**
	 * Errorログを出力
	 * @param msg [String]出力文字列
	 */
	public static void e(String msg) {
		if (!isWritable())
			return;
		
		if (msg == null)
			msg = "";
		
		Log.e(getTag(), msg);
	}
	
	/**
	 * Verboseログを出力
	 * @param msg [String]出力文字列
	 */
	public static void v(String msg) {
		if (!isWritable())
			return;
		
		if (msg == null)
			msg = "";
		
		Log.v(getTag(), msg);
	}
	
	/**
	 * Warnログを出力
	 * @param msg [String]出力文字列
	 */
	public static void w(String msg) {
		if (!isWritable())
			return;
		
		if (msg == null)
			msg = "";
		
		Log.w(getTag(), msg);
	}
	
	// ------------------------------
	// Function
	// ------------------------------
	/**
	 * 出力可能可能かを返す
	 * @return true:出力可能, false:出力不可
	 */
	protected static Boolean isWritable() {
		return BuildConfig.DEBUG;
	}
	
	/**
	 * タグ文字列を返す
	 */
	protected static String getTag() {
		return dDefaultTag;
	}
}
