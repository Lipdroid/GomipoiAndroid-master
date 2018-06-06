package lib.application;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;

/**
 * アプリ情報関連ユーティリティクラス
 *
 */
public class ApplicationUtils {

	// ==============================
	// Define
	// ==============================
	public static final String TYPE_PDF = "application/pdf";
	
	// ==============================
	// Accesser
	// ==============================
	/**
	 * パッケージ名を返す
	 * @param context
	 * @return
	 */
	public static String getPackageName(Context context) {
		if (context == null)
			return null;
		
		return context.getPackageName();
	}
	
	/**
	 * アプリケーションのバージョンを返す
	 * @param context
	 * @return
	 */
	public static String getApplicationVersion(Context context) {
		if (context == null)
			return null;
		
		String value = null;
		PackageManager manager = context.getPackageManager();
		try {
			PackageInfo info = manager.getPackageInfo(getPackageName(context), PackageManager.GET_ACTIVITIES);
			value = info.versionName;
		} catch (NameNotFoundException exp) {
		}
		return value;
	}
	
	/**
	 * GooglePlayアプリに遷移するためのIntentを作成する
	 * @param context
	 * @return
	 */
	public static Intent makeThisApplicationGooglePlayIntent(Context context) {
		if (context == null)
			return null;
		
		String packageName = getPackageName(context);
		if (packageName == null)
			return null;
		
		Uri uri = Uri.parse(String.format("market://details?id=%s", packageName));
		if (uri == null)
			return null;
		
		return new Intent(Intent.ACTION_VIEW, uri);
	}
	
	/**
	 * WEBページに遷移するIntentを作成する
	 * @param context
	 * @param pageUrl
	 * @return
	 */
	public static Intent makeWebPageIntent(Context context, String pageUrl) {
		if (context == null)
			return null;
		
		if (pageUrl == null)
			return null;
		
		Uri uri = Uri.parse(pageUrl);
		if (uri == null)
			return null;

		return new Intent(Intent.ACTION_VIEW, uri);
	}
	
	/**
	 * アプリケーション一覧へのIntentを作成する
	 * @param context
	 * @param type
	 * @param objectPath
	 * @return
	 */
	public static Intent makeApplicationListIntent(Context context, String type, String objectPath) {
		if (context == null)
			return null;
		
		if (type == null)
			return null;
		
		if (objectPath == null)
			return null;
		
		Uri uri = Uri.parse(objectPath);
		if (uri == null)
			return null;
		
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(uri, type);
		return intent;
	}
}
