package lib.resize;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;

import lib.convert.UnitUtils;

/**
 * レイアウトのリサイズの管理基幹クラス
 * @author Yuya Hirayama (foot-loose)
 *
 */
public abstract class ResizeLayoutManagerBase {

	// ------------------------------
	// Define
	// ------------------------------
	protected final Context dContext;
	protected  final float dDisplayWidth;
	protected final float dDisplayHeight;

	// ------------------------------
	// Member
	// ------------------------------
	protected float mLayoutRatio;
	
	// ------------------------------
	// Constructor
	// ------------------------------
	/**
	 * Constructor
	 * @param context
	 */
	public ResizeLayoutManagerBase(Context context) {
		dContext = context;
		dDisplayWidth = context.getResources().getDisplayMetrics().widthPixels;
		dDisplayHeight = context.getResources().getDisplayMetrics().heightPixels;
		mLayoutRatio = calcRatio();
	}

	/**
	 * Constructor
	 * @param context
	 * @param width
	 * @param height
	 */
	public ResizeLayoutManagerBase(Context context, int width, int height) {
		dContext = context;
		dDisplayWidth = width;
		dDisplayHeight = height;
		mLayoutRatio = calcRatio();
	}
	
	// ------------------------------
	// Abstract
	// ------------------------------
	/**
	 * 基準となる幅を返す
	 * @return
	 */
	public abstract float getStandardWidth();
	
	/**
	 * 基準となる高さを返す
	 * @return
	 */
	public abstract float getStandardHeight();
	
	// ------------------------------
	// Accesser
	// ------------------------------
	/**
	 * レイアウトの倍率を返す
	 * @return
	 */
	public float getLayoutRatio() {
		return mLayoutRatio;
	}
	
	/**
	 * 倍率を適応したスクリーンの幅を返す
	 * @return
	 */
	public float getScreenWidth() {
		return getStandardWidth() * mLayoutRatio;
	}
	
	/**
	 * 倍率を適応したスクリーンの高さを返す
	 * @return
	 */
	public float getScreenHeight() {
		return getStandardHeight() * mLayoutRatio;
	}

	/**
	 * リサイズのエントリーポイント
	 * @param view
	 */
	public void resize(View view) {
		if (view == null) {
			return;
		}
		
		if (view instanceof ViewGroup) {
			resizeViewGroup((ViewGroup) view);
			return;
		}
		resizeView(view);
	}
	
	/**
	 * FragmentLayout用のリサイズ処理
	 * @param view
	 */
	public void resizeFragmentContainer(View view) {
		if (view == null) {
			return;
		}
		
		MarginLayoutParams marginLayoutParams = null;
		LayoutParams param = view.getLayoutParams();
		if (param instanceof MarginLayoutParams) {
			marginLayoutParams = (MarginLayoutParams) param;
		} else {
			return;
		}
		
		int width = (int)Math.ceil(getScreenWidth());
		int height = (int)Math.ceil(getScreenHeight());
		marginLayoutParams.width = width;
		marginLayoutParams.height = height;
	}

	public final float calcValue(float dpValue) {
		return dpValue * mLayoutRatio;
	}
	
	// ------------------------------
	// Function
	// ------------------------------
	/**
	 * 比率を守って、画面サイズに内接させる為の倍率を算出する
	 */
	protected float calcRatio() {
		float width = getStandardWidth();
		float height = getStandardHeight();
		float widthRatio = dDisplayWidth / width;
		float heightRatio = dDisplayHeight / height;
		return Math.min(widthRatio, heightRatio);
	}
	
	/**
	 * ViewGroup自身と子Viewをリサイズする
	 * @param group
	 */
	private final void resizeViewGroup(ViewGroup group) {
		if (group == null) {
			return;
		}
		
		resizeView(group);
		for (int index = 0; index < group.getChildCount(); index++) {
			View child = group.getChildAt(index);
			if (child instanceof ViewGroup) {
				resizeViewGroup((ViewGroup)child);
			} else {
				resizeView(child);
			}
		}
	}
	
	/**
	 * Viewをリサイズする
	 * @param view
	 */
	private final void resizeView(View view) {
		if (view == null) {
			return;
		}
		
		MarginLayoutParams marginLayoutParams = null;
		LayoutParams param = view.getLayoutParams();
		if (param instanceof MarginLayoutParams) {
			marginLayoutParams = (MarginLayoutParams) param;
		} else {
			return;
		}
		
		int width = marginLayoutParams.width < 0 
				? marginLayoutParams.width 
						: (int)Math.ceil(UnitUtils.getDpFromPx(dContext, marginLayoutParams.width) * mLayoutRatio);
		int height = marginLayoutParams.height < 0 ? marginLayoutParams.height : (int)Math.ceil(UnitUtils.getDpFromPx(dContext, marginLayoutParams.height) * mLayoutRatio);
		marginLayoutParams.width = width;
		marginLayoutParams.height = height;
		
		int marginLeft = (int)Math.ceil(UnitUtils.getDpFromPx(dContext, marginLayoutParams.leftMargin) * mLayoutRatio);
		int marginRight = (int)Math.ceil(UnitUtils.getDpFromPx(dContext, marginLayoutParams.rightMargin) * mLayoutRatio);
		int marginTop = (int)Math.ceil(UnitUtils.getDpFromPx(dContext, marginLayoutParams.topMargin) * mLayoutRatio);
		int marginBottom = (int)Math.ceil(UnitUtils.getDpFromPx(dContext, marginLayoutParams.bottomMargin) * mLayoutRatio);
		marginLayoutParams.setMargins(marginLeft, marginTop, marginRight, marginBottom);
		
		int paddingLeft = (int)Math.ceil(UnitUtils.getDpFromPx(dContext, view.getPaddingLeft()) * mLayoutRatio);
		int paddingRight = (int)Math.ceil(UnitUtils.getDpFromPx(dContext, view.getPaddingRight()) * mLayoutRatio);
		int paddingTop = (int)Math.ceil(UnitUtils.getDpFromPx(dContext, view.getPaddingTop()) * mLayoutRatio);
		int paddingBottom = (int)Math.ceil(UnitUtils.getDpFromPx(dContext, view.getPaddingBottom()) * mLayoutRatio);
		
		view.setLayoutParams(marginLayoutParams);
		view.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
	}
}
