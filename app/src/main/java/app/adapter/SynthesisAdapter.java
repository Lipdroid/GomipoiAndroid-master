package app.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.topmission.gomipoi.R;

import java.util.ArrayList;
import java.util.List;

import app.application.GBApplication;
import app.data.BookGarbageData;
import app.define.DialogCode;
import lib.adapter.OnAdapterListener;

public class SynthesisAdapter extends PagerAdapter implements View.OnClickListener {

    // ------------------------------
    // Define
    // ------------------------------
    public static final int PAGE_ITEM_COUNT = 12;// 1ページのアイテム数

    // ------------------------------
    // Member
    // ------------------------------
    private Context mContext = null;
    private List<BookGarbageData> mGarbageDataList = null;
    private OnAdapterListener mListener = null;
    private List<BookGarbageData> mSelectItemList = null;// 選択したアイテムのリスト

    // ------------------------------
    // Constructor
    // ------------------------------
    public SynthesisAdapter(Context context, List<BookGarbageData> garbageDataList, OnAdapterListener listener) {
        mContext = context;
        mGarbageDataList = garbageDataList;
        mListener = listener;
        mSelectItemList = new ArrayList<>();
    }
    // ------------------------------
    // Override
    // ------------------------------
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_item_synthesis_pager, container, false);
        view.setTag(position);

        if (mContext == null) {
            return view;
        }
        for (int i = 1; i <= PAGE_ITEM_COUNT; i++) {
            // アイテムのNoを計算
            int itemNo = position * PAGE_ITEM_COUNT + i;
            // アイテムの番号に対応した画像をセットする
            String imageString = "layoutItem" + i;
            int imageId = mContext.getResources().getIdentifier(imageString, "id", mContext.getPackageName());
            ImageButton itemImageButton = (ImageButton) view.findViewById(imageId);

            // No64までしか画像は表示しない
            if (itemNo - 1 >= mGarbageDataList.size()) {
                if (itemImageButton != null) {
                    itemImageButton.setVisibility(View.GONE);
                }
                continue;
            }

            BookGarbageData garbageData = mGarbageDataList.get(itemNo - 1);
            if (garbageData == null) {
                continue;
            }
            // アイテムの番号に対応した画像をセットする
            if (itemImageButton != null && garbageData != null) {
                // アイテムがアンロックされているかどうかを確認する
                Drawable drawable = null;
                if (garbageData.isLocked()) {
                    // アイテムがロックされているとき
                    drawable = getImageDrawable(R.drawable.panel_unopened);
                } else {
                    int resourceId = garbageData.getGarbageId().getSynthesisResourceId();
                    drawable = getImageDrawable(resourceId);
                }
                if (drawable == null) {
                    continue;
                } else {
                    itemImageButton.setImageDrawable(drawable);
                }
                // 選択したアイテムのときは背景画像を黄色のものにする
                if (mSelectItemList != null) {
                    itemImageButton.setSelected(mSelectItemList.contains(garbageData));
                }
                // ボタンにアイテムをセットしておく
                itemImageButton.setTag(garbageData);
                itemImageButton.setOnClickListener(this);
            }
        }
        container.addView(view);

        ((GBApplication) mContext).getResizeManager().resize(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        if (mGarbageDataList == null)
            return 0;
        else return (mGarbageDataList.size() - 1) / PAGE_ITEM_COUNT + 1;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    //----------------------------------------
    // function
    //----------------------------------------
    /**
     * drawableをAPILevelでわけて取得する
     * @param resourceId
     * @return
     */
    @SuppressWarnings("deprecation")
    private Drawable getImageDrawable(int resourceId) {
        if (mContext == null || resourceId == 0) {
            return null;
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            // LOLLIPOP以上のとき
            return mContext.getDrawable(resourceId);
        } else {
            return mContext.getResources().getDrawable(resourceId);
        }
    }

    /**
     * 選択アイテムリストをセットする
     * @param selectItmeList
     */
    public void setItemSelectList(List<BookGarbageData> selectItmeList) {
        mSelectItemList = selectItmeList;
    }

    //----------------------------------------
    // View.OnClickListener
    //----------------------------------------
    @Override
    public void onClick(View view) {
        if (mListener != null) {
            mListener.onEvent(DialogCode.Synthesis.getValue(), view.getTag());
        }
    }
}
