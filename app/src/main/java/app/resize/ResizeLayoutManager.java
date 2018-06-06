package app.resize;

import android.content.Context;
import android.support.annotation.NonNull;

import lib.resize.ResizeLayoutManagerBase;

/**
 * レイアウトの自動リサイズの管理クラス
 */
public class ResizeLayoutManager extends ResizeLayoutManagerBase {

	// ------------------------------
	// Define
	// ------------------------------
	protected final float STANDARD_WIDTH = 320; 	// 320dpを基準とする
//	protected final float STANDARD_HEIGHT = 548; 	// 548dpを基準とする
	protected final float STANDARD_HEIGHT = 568; 	// 568dpを基準とする

	// ------------------------------
	// Constructor
	// ------------------------------
	/**
	 * Constructor
	 * @param context
	 */
	public ResizeLayoutManager(@NonNull Context context, int width, int height) {
		super(context, width, height);
	}

	// ------------------------------
	// Override
	// ------------------------------
	@Override
	public float getStandardWidth() {
		return STANDARD_WIDTH;
	}
	
	@Override
	public float getStandardHeight() {
		return STANDARD_HEIGHT;
	}
	
}
