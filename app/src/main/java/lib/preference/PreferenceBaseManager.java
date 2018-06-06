package lib.preference;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreference管理クラス
 */
public class PreferenceBaseManager {

	// ------------------------------
	// Define
	// ------------------------------
	private final Context dContext;
	private final SharedPreferences dPreference;
	
	// ------------------------------
	// Constructor
	// ------------------------------
	public PreferenceBaseManager(Context context) {
		dContext = context;
		dPreference = dContext.getSharedPreferences(getName(), Context.MODE_PRIVATE);
	}
	
	// ------------------------------
	// Accesser
	// ------------------------------
	public void setValue(String key, Object value) {
		if (dPreference == null)
			return;
		
		SharedPreferences.Editor editor = dPreference.edit();
		if (value instanceof Integer) {
			editor.putInt(key, (Integer) value);
		} else if (value instanceof Float) {
			editor.putFloat(key, (Float) value);
		} else if (value instanceof Boolean) {
			editor.putBoolean(key, (Boolean) value);
		} else if (value instanceof String) {
			editor.putString(key, (String) value);
		} else if (value instanceof Long) {
			editor.putLong(key, (Long)value);
		} else if (value instanceof Double) {
			editor.putLong(key, Double.doubleToRawLongBits((Double) value));
		}
		editor.commit();
		
	}
	
	/**
	 * int型の値を取得する
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public int getInt(String key, int defaultValue) {
		if (dPreference == null)
			return defaultValue;
		
		return dPreference.getInt(key, defaultValue);
	}

	/**
	 * long型の値を取得する
	 * @param key
	 * @param defaultValue
	 */
	public long getLong(String key, long defaultValue) {
		if (dPreference == null)
			return defaultValue;

		return dPreference.getLong(key, defaultValue);
	}
	
	/**
	 * float型の値を取得する
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public float getFloat(String key, float defaultValue) {
		if (dPreference == null)
			return defaultValue;
		
		return dPreference.getFloat(key, defaultValue);
	}

	/**
	 * double型の値を取得する
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public double getDouble(String key, double defaultValue) {
		if (dPreference == null)
			return defaultValue;
		return Double.longBitsToDouble(dPreference.getLong(key, Double.doubleToLongBits(defaultValue)));
	}
	
	/**
	 * boolean型の値を取得する
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public boolean getBoolean(String key, boolean defaultValue) {
		if (dPreference == null)
			return defaultValue;
		
		return dPreference.getBoolean(key, defaultValue);
	}
	
	/**
	 * String型の値を取得する
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public String getString(String key, String defaultValue) {
		if (dPreference == null)
			return defaultValue;
		
		return dPreference.getString(key, defaultValue);
	}
	
	// ------------------------------
	// Function
	// ------------------------------
	/**
	 * 名前を返す
	 * @return
	 */
	protected String getName() {
		return dContext.getPackageName() + "--Preference";
	}
	
}
