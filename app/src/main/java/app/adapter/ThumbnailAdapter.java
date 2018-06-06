package app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.topmission.gomipoi.R;
import com.daydelight.imagefetcher.BitmapAsyncLoadHelper;
import com.daydelight.imagefetcher.ImageData;
import com.daydelight.imagefetcher.SimpleImageDataAdapter;

import java.util.ArrayList;
import java.util.List;

import app.application.GBApplication;

/**
 */
public class ThumbnailAdapter extends SimpleImageDataAdapter implements View.OnClickListener {

    // ------------------------------
    // Member
    // ------------------------------
    private boolean mIsLockedEvent;
    private OnThumbnailAdapterListener mThumbnailAdapterListener;

    // ------------------------------
    // Constructor
    // ------------------------------
    public ThumbnailAdapter(Context context, List<ImageData> objects, BitmapAsyncLoadHelper loader) {
        super(
                context,
                R.layout.cell_picture_poi,
                R.id.imageViewThumbnail,
                0,
                context.getResources().getDisplayMetrics().widthPixels / 8,
                context.getResources().getDisplayMetrics().heightPixels / 8,
//                context.getResources().getDisplayMetrics().widthPixels / 4,
//                context.getResources().getDisplayMetrics().heightPixels / 4,
                objects,
                loader);
    }

    // ------------------------------
    // Override
    // ------------------------------
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        boolean isNeedResize = (convertView == null);
        View view = super.getView(position, convertView, parent);
        if (view == null) {
            return null;
        }

        ImageData item = getItem(position);

        ImageView imageViewCheck = (ImageView) view.findViewById(R.id.imageViewCheck);
        if (imageViewCheck != null) {
            imageViewCheck.setSelected(mThumbnailAdapterListener != null && mThumbnailAdapterListener.ThumbnailAdapter_isSelected(item));
            imageViewCheck.setTag(item);
        }

        if (isNeedResize) {
            view.setOnClickListener(this);
            ((GBApplication) getContext()).getResizeManager().resize(view);
        }
        return view;
    }

    // ------------------------------
    // View.OnClickListener
    // ------------------------------
    @Override
    public void onClick(View v) {
        if (mIsLockedEvent) {
            return;
        }
        mIsLockedEvent = true;

        ImageView imageViewCheck = (ImageView) v.findViewById(R.id.imageViewCheck);
        if (imageViewCheck != null) {
            ImageData item = (ImageData)imageViewCheck.getTag();
            if (mThumbnailAdapterListener != null) {
                mThumbnailAdapterListener.ThumbnailAdapter_onChangeSelected(item);
            }
            this.notifyDataSetChanged();
        }

        mIsLockedEvent = false;
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    public void setOnThumbnailAdapterListener(OnThumbnailAdapterListener listener) {
        mThumbnailAdapterListener = listener;
    }

    public void onCompletedDelete() {
        this.notifyDataSetChanged();
    }

    public final void lockEvent() {
        mIsLockedEvent = true;
    }

    public final void unlockEvent() {
        mIsLockedEvent = false;
    }

    // ------------------------------
    // Interface
    // ------------------------------
    public interface OnThumbnailAdapterListener {
        boolean ThumbnailAdapter_isSelected(ImageData item);
        void ThumbnailAdapter_onChangeSelected(ImageData item);
    }

}
