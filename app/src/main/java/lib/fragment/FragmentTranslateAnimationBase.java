package lib.fragment;

/**
 * フラグメントの移動アニメーションクラス
 * @author Yuya Hirayama (foot-loose)
 *
 */
public class FragmentTranslateAnimationBase {
	
	// ------------------------------
	// Member
	// ------------------------------
	private boolean isNeedAnimation;
	
	// ------------------------------
	// Accesser
	// ------------------------------
	public void setNeedAnimation(boolean needAnimation) {
		isNeedAnimation = needAnimation;
	}
	
	/**
	 * アニメーションが必要かを返す
	 * @return
	 */
	public boolean isNeed() {
		return isNeedAnimation;
	}
	
	/**
	 * アニメーションを返す
	 * @return
	 */
	public int getRightEnter() {
		return 0;
	}
	
	/**
	 * アニメーションを返す
	 * @return
	 */
	public int getLeftExit() {
		return 0;
	}
	
	/**
	 * アニメーションを返す
	 * @return
	 */
	public int getLeftEnter() {
		return 0;
	}
	
	/**
	 * アニメーションを返す
	 * @return
	 */
	public int getRightExit() {
		return 0;
	}
}
