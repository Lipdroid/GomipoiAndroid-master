package lib.fragment;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * フラグメントをスタック管理するクラス
 * @author Yuya Hirayama (foot-loose)
 *
 */
public class FragmentStackManager {
	
	// ------------------------------
	// Member
	// ------------------------------
	private ArrayList<StackData> mStackDataList;

	// ------------------------------
	// Constructor
	// ------------------------------
	/**
	 * Constructor
	 */
	public FragmentStackManager() {
	}

	// ------------------------------
	// Accesser
	// ------------------------------
	/**
	 * 変数を破棄する
	 */
	public void destroy() {
		if (mStackDataList != null) {
			mStackDataList.clear();
			mStackDataList = null;
		}
	}
	
	/**
	 * スタック数を返す
	 * @return
	 */
	public int getStackSize() {
		return getStackDataList().size();
	}
	
	/**
	 * 最初のスタックをセットする
	 * @param data
	 */
	public void setFirstStack(StackData data) {
		if (getStackSize() == 1) {
			return;
		}
		
		getStackDataList().clear();
		getStackDataList().add(data);
	}
	
	/**
	 * スタックする
	 * @param data
	 */
	public void stack(StackData data) {
		getStackDataList().add(data);
	}
	
	/**
	 * スタックを1つ戻す
	 * @return
	 */
	public void back() {
		if (!isBackable()) {
			return;
		}
		getStackDataList().remove(getStackSize() - 1);
	}
	
	/**
	 * スタックの最後のデータを返す
	 * @return
	 */
	public StackData getLastStack() {
		return getStackDataList().get(getStackSize() - 1);
	}
	
	/**
	 * Back可能かどうかを返す
	 * @return
	 */
	public boolean isBackable() {
		return getStackSize() > 1;
	}

	// ------------------------------
	// Function
	// ------------------------------
	/**
	 * StackDataListを返す
	 * @return
	 */
	protected ArrayList<StackData> getStackDataList() {
		if (mStackDataList == null) {
			mStackDataList = new ArrayList<StackData>();
		}
		return mStackDataList;
	}
	
	// ------------------------------
	// Inner-Class
	// ------------------------------
	/**
	 * スタックデータ
	 * @author Yuya Hirayama (foot-loose)
	 *
	 */
	public static class StackData implements Serializable {
		
		// ------------------------------
		// Define
		// ------------------------------
		private static final long serialVersionUID = 1L;
		
		// ------------------------------
		// Member
		// ------------------------------
		public FragmentBase fragment;
		public String name;
		
		// ------------------------------
		// Constructor
		// ------------------------------
		/**
		 * Constructor
		 * @param fragment
		 * @param name
		 */
		public StackData(FragmentBase fragment, String name) {
			this.fragment 	= fragment;
			this.name 		= name;
		}
	}
}
