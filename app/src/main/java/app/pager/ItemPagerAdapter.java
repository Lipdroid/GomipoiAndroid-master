package app.pager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import app.data.ListItemData;

/**
 */
public class ItemPagerAdapter extends PagerAdapter implements View.OnClickListener {

    // ------------------------------
    // Define
    // ------------------------------

    // ------------------------------
    // Member
    // ------------------------------
    protected Context mContext;
    protected List<ListItemData> mItemList;
    protected OnItemPagerAdapterListener mListener;

    // ------------------------------
    // Constructor
    // ------------------------------
    public ItemPagerAdapter(Context context, List<ListItemData> itemList, OnItemPagerAdapterListener mListener) {
        this.mContext = context;
        this.mItemList = itemList;
        this.mListener = mListener;
    }

    // ------------------------------
    // Override
    // ------------------------------
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mItemList == null || mItemList.size() == 0 ? 0 : ((mItemList.size() - 1) / 3) + 1;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (View) object;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    // ------------------------------
    // View.OnClickListener
    // ------------------------------
    @Override
    public void onClick(View v) {
    }
}
