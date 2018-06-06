package app.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.topmission.gomipoi.R;

import java.util.List;

import app.animation.BookQuestionAnimationManager;
import app.application.GBApplication;
import app.data.BookGarbageData;
import app.jni.JniBridge;
import app.number.NumberUtils;
import app.view.OutlineTextView;
import lib.text.TextUtils;

public class BookSpAdapter extends PagerAdapter implements View.OnClickListener {

    // ------------------------------
    // Define
    // ------------------------------
    public static final int PAGE_ITEM_COUNT = 6;// 1ページのアイテム数

    // ------------------------------
    // Member
    // ------------------------------
    private Context mContext = null;
    private List<BookGarbageData> mSpDataList = null;
    private BookAdapter.OnBookAdapterListener mOnBookAdapterListener = null;

    // ------------------------------
    // Constructor
    // ------------------------------
    public BookSpAdapter(Context context, List<BookGarbageData> spDataList, BookAdapter.OnBookAdapterListener listener) {
        mContext = context;
        mSpDataList = spDataList;
        mOnBookAdapterListener = listener;
    }
    // ------------------------------
    // Override
    // ------------------------------
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_item_book_sp_pager, container, false);
        view.setTag(position);

        if (mContext == null) {
            return view;
        }
        for (int i = 1; i <= PAGE_ITEM_COUNT; i++) {
            // アイテムのNoを計算
            int itemNo = position * PAGE_ITEM_COUNT + i;
            // No64までしか画像は表示しない
            if (itemNo - 1 >= mSpDataList.size()) {
                String frameString = "layoutItem" + i;
                int frameId = mContext.getResources().getIdentifier(frameString, "id", mContext.getPackageName());
                FrameLayout layout = (FrameLayout) view.findViewById(frameId);
                if (layout != null) {
                    layout.setVisibility(View.GONE);
                }
                continue;
            }

            BookGarbageData garbageData = mSpDataList.get(itemNo - 1);
            if (garbageData == null) {
                continue;
            }

            String outlineTextString = "outlineTextViewPoint" + i;
            int outlineTextId = mContext.getResources().getIdentifier(outlineTextString, "id", mContext.getPackageName());
            OutlineTextView outlineTextView = (OutlineTextView) view.findViewById(outlineTextId);
            if (outlineTextView != null) {
                outlineTextView.setVisibility(View.GONE);
            }

            String imageViewQuestionString = "imageViewQuestion" + i;
            int imageViewQuestionId = mContext.getResources().getIdentifier(imageViewQuestionString, "id", mContext.getPackageName());
            ImageView imageViewQuestion = (ImageView) view.findViewById(imageViewQuestionId);
            if (imageViewQuestion != null) {
                imageViewQuestion.clearAnimation();
                imageViewQuestion.setVisibility(View.GONE);
            }

            // アイテムの番号に対応した画像をセットする
            String imageString = "imageViewItem" + i;
            int imageId = mContext.getResources().getIdentifier(imageString, "id", mContext.getPackageName());
            ImageView itemImageView = (ImageView) view.findViewById(imageId);
            if (itemImageView != null) {
                itemImageView.setTag(garbageData);
                // アイテムがアンロックされているかどうかを確認する
                int resourceId = 0;
                if (garbageData.isLocked()) {
                    // アイテムがロックされているとき
                    resourceId = garbageData.getGarbageId().getSpBookResource(false);
                    itemImageView.setOnClickListener(this);

                    if (outlineTextView != null) {
                        outlineTextView.setText(NumberUtils.getNumberFormatText(JniBridge.nativeGetGarbageBonus(garbageData.getGarbageId().getValue())) + "P");
                        outlineTextView.setVisibility(View.VISIBLE);
                    }

                    if (imageViewQuestion != null) {
                        new BookQuestionAnimationManager(0, imageViewQuestion, null).start();
                        imageViewQuestion.setVisibility(View.VISIBLE);
                    }

                } else {
                    resourceId = garbageData.getGarbageId().getSpBookResource(true);

                    itemImageView.setOnClickListener(this);
                }

                if (resourceId == 0) {
                    continue;
                } else {
                    itemImageView.setImageResource(resourceId);
                }
            }
            if (garbageData.isLocked()) {
                // アイテムがロックされているときはNEWの判定をしない。
                continue;
            }
            // NEWをつけるかどうかの判定
            String newImageString = "imageViewNew" + i;
            int newImageId = mContext.getResources().getIdentifier(newImageString, "id", mContext.getPackageName());
            ImageView newImageView = (ImageView) view.findViewById(newImageId);
            if (newImageView != null) {
                newImageView.setVisibility(garbageData.isNew() ? View.VISIBLE : View.GONE);
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
        if (mSpDataList == null)
            return 0;
        else return (mSpDataList.size() - 1) / PAGE_ITEM_COUNT + 1;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (View) object;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    //----------------------------------------
    // View.OnClickListener
    //----------------------------------------
    @Override
    public void onClick(View v) {
        if (v.getTag() == null || !(v.getTag() instanceof BookGarbageData)) {
            return;
        }

        if (mOnBookAdapterListener != null) {
            mOnBookAdapterListener.onClickedCell((BookGarbageData) v.getTag());
        }
    }

}
