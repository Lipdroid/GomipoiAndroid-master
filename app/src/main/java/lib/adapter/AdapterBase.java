package lib.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Adapter基幹クラス
 * @author Yuya Hirayama (foot-loose)
 *
 */
public class AdapterBase<T> extends ArrayAdapter<T> {

	// ------------------------------
	// Define
	// ------------------------------
	protected final LayoutInflater dInflater;
	protected final OnAdapterListener dAdapterListener;
	
	// ------------------------------
	// Member
	// ------------------------------
	private boolean mIsLockedEvent;
	
	// ------------------------------
	// Constructor
	// ------------------------------
	public AdapterBase(Context context, List<T> objects, OnAdapterListener listener) {
		super(context, 0, 0, objects);
		dInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		dAdapterListener = listener;
		mIsLockedEvent = false;
	}

	// ------------------------------
	// Accesser
	// ------------------------------
	/**
	 * リストを更新する
	 * @param dataList
	 */
	public void refresh(List<T> dataList) {
		this.clear();
        this.addAll(dataList);
		this.notifyDataSetChanged();
	}

	// ------------------------------
	// Function
	// ------------------------------
	/**
	 * LayoutInflaterのインスタンスを返す
	 * @return
	 */
	protected LayoutInflater getLayoutInflater() {
		return dInflater;
	}
	
	/**
	 * イベントロックが掛かっているかを返す
	 * @return
	 */
	protected boolean isLockedEvent() {
		return mIsLockedEvent;
	}
	
	/**
	 * イベントロックを掛ける
	 */
	protected void lockEvent() {
		mIsLockedEvent = true;
	}
	
	/**
	 * イベントロックを解除する
	 */
	protected void unlockEvent() {
		mIsLockedEvent = false;
	}
}
