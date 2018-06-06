package lib.appversion;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import lib.log.DebugLog;


/**
 * アプリバージョン関連Utilityクラス
 */
public class AppVersionUtils {
	
	// ------------------------------
	// Accesser
	// ------------------------------
	/**
	 * アップデートの必要があるかを返す
	 */
	public static boolean isNeedUpdate(Context context, String serverVersion) {
		return isOlder(getCurrentVersion(context), serverVersion);
	}
	
	/**
	 * 現在のアプリのバージョン番号を返す
	 */
	public static String getCurrentVersion(Context context) {
		String retValue 		= null;
		PackageInfo packageInfo = null;
		
		try {
	        packageInfo = context.getPackageManager().getPackageInfo(
	        		context.getPackageName(), 
	        		PackageManager.GET_META_DATA);
		} catch (NameNotFoundException e) {
			DebugLog.d(e.getMessage());
		}
		
		if (packageInfo != null) {
			retValue = packageInfo.versionName;
		}
		
		return retValue;
	}

	
	// ------------------------------
	// Function
	// ------------------------------
	/**
	 * 比較処理
	 */
	private static boolean isOlder(String appVersion, String serverVersion) {
		if (appVersion == null
				|| appVersion.length() <= 0) {
			return false;
		}

		if (serverVersion == null
				|| serverVersion.length() <= 0) {
			return false;
		}
		
		// 「.」で文字列を分割
		int[] columnsOfCurVersions = convertStringArrToIntArr(appVersion.split("\\."));
		int[] columnsOfMinVersions = convertStringArrToIntArr(serverVersion.split("\\."));
		if (columnsOfCurVersions == null
				|| columnsOfCurVersions.length <= 0
				|| columnsOfMinVersions == null
				|| columnsOfMinVersions.length <= 0) {
			return false;
		}
		
		boolean retValue = false;
		for (int i = 0; i < Math.max(columnsOfCurVersions.length, columnsOfMinVersions.length); i++) {
			int curValue = 0;
			if (i < columnsOfCurVersions.length) {
				curValue = columnsOfCurVersions[i];
			}
			
			int minValue = 0;
			if (i < columnsOfMinVersions.length) {
				minValue = columnsOfMinVersions[i];
			}
			
			if (curValue < minValue) {
				retValue = true;
				break;
			} else if (curValue > minValue) {
				retValue = false;
				break;
			}
		}
		columnsOfCurVersions = null;
		columnsOfMinVersions = null;
		return retValue;
	}
	
	/**
	 * ピリオド区切り文字列を数値の配列に変換する
	 */
	private static int[] convertStringArrToIntArr(String[] datas) {
		if (datas == null
				|| datas.length <= 0) {
			return null;
		}
		int[] retDatas = new int[datas.length];
		for (int i = 0; i < datas.length; i++) {
			try {
				retDatas[i] = Integer.parseInt(datas[i].replaceAll("[^0-9]",""));
			} catch (NumberFormatException e) {
				retDatas[i] = 0;
			}
		}
		return retDatas;
	}

}
