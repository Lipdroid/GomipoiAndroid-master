package lib.convert;

import android.content.Context;

/**
 * 単位関連ユーティリティクラス
 * @author Yuya Hirayama (foot-loose)
 *
 */
public class UnitUtils {

	/**
	 * dpの値をpixelの値に変換し、返す
	 * @param context
	 * @param dpValue
	 * @return
	 */
	public static final float getPxFromDp(Context context, float dpValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return dpValue * scale + 0.5f;
	}

	/**
	 * spの値をpixelの値に変換し、返す
	 * @param context
	 * @param spValue
	 * @return
	 */
	public static final float getPxFromSp(Context context, float spValue) {
		float scale = context.getResources().getDisplayMetrics().scaledDensity;
		return spValue * scale + 0.5f;
	}
	
	/**
	 * pxの値をdpの値に変換し、返す
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static final float getDpFromPx(Context context, float pxValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (pxValue - 0.5f) / scale;
	}

	/**
	 * pxの値をspの値に変換し、返す
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static final float getSpFromPx(Context context, float pxValue) {
		float scale = context.getResources().getDisplayMetrics().scaledDensity;
		return (pxValue - 0.5f) / scale;
	}
	
}
