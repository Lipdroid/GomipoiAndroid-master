package com.daydelight.imagefetcher;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.util.List;

/**
 * Created by Herve on 2016/03/15.
 */
public class SimpleImageDataAdapter extends ImageDataAdapter {

    //================================================
    // Fields
    //================================================
    private int                 mLayoutRes;
    private int                 mImageViewRes;
    private int                 mDeleteViewRes;

    private int                 mThumbnailWidth = BitmapMaker.THUMBNAIL_WIDTH;
    private int                 mThumbnailHeight = BitmapMaker.THUMBNAIL_HEIGHT;

    private Bitmap              mLoadingBitmap;
    private OnDeleteListener    mListener;

    //================================================
    // Constructors
    //================================================
    public SimpleImageDataAdapter(Context context, @LayoutRes int resource, @IdRes int imageViewRes,
                                  @IdRes int deleteViewRes, BitmapAsyncLoadHelper loader) {
        super(context, resource, loader);

        init(resource, imageViewRes, deleteViewRes);
    }

    public SimpleImageDataAdapter(Context context, @LayoutRes int resource, @IdRes int imageViewRes,
                                  @IdRes int deleteViewRes, ImageData[] objects, BitmapAsyncLoadHelper loader) {
        super(context, resource, objects, loader);

        init(resource, imageViewRes, deleteViewRes);
    }

    public SimpleImageDataAdapter(Context context, @LayoutRes int resource, @IdRes int imageViewRes,
                                  @IdRes int deleteViewRes, List<ImageData> objects, BitmapAsyncLoadHelper loader) {
        super(context, resource, objects, loader);

        init(resource, imageViewRes, deleteViewRes);
    }

    public SimpleImageDataAdapter(Context context, @LayoutRes int resource, @IdRes int imageViewRes,
                                  @IdRes int deleteViewRes, int thumbnailWidth, int thumbnailHeight, List<ImageData> objects, BitmapAsyncLoadHelper loader) {
        super(context, resource, objects, loader);

        mThumbnailWidth = thumbnailWidth;
        mThumbnailHeight = thumbnailHeight;

        init(resource, imageViewRes, deleteViewRes);
    }

    private void init(@LayoutRes int resource, @IdRes int imageViewRes, @IdRes int deleteViewRes) {
        mLayoutRes      = resource;
        mImageViewRes   = imageViewRes;
        mDeleteViewRes  = deleteViewRes;
        mLoadingBitmap  = null;
    }

    //================================================
    // Getters, setters
    //================================================
    public void setOnDeleteListener(OnDeleteListener listener) {
        mListener = listener;
    }

    public void setLoadingBitmap(Bitmap bitmap) {
        mLoadingBitmap = bitmap;
    }

    //================================================
    // Adapter methods
    //================================================
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder;
        View view       = convertView;
        Resources res   = getContext().getResources();

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(mLayoutRes, parent, false);

            holder = new Holder();
            View imageView = view.findViewById(mImageViewRes);
            if (!(imageView instanceof ImageView)) {
                throw new IllegalArgumentException("imageViewRes defined in constructor must point to an ImageView.");
            }

            holder.bitmapView = (ImageView) imageView;
            holder.deleteView = view.findViewById(mDeleteViewRes);

            view.setTag(holder);
        }
        else {
            holder = (Holder) view.getTag();
        }

        final ImageData data = getItem(position);

        if (!data.equals(holder.data)) {
            boolean isNeedGetOriginal = true;
            String fileName = null;
            int width = 0, height = 0;
            if (data.getThumbFileName() != null) {
                File tmpFile = new File(data.getThumbFileName());
                if (tmpFile.exists()) {
                    isNeedGetOriginal = false;
                    fileName    = data.getThumbFileName();
                    width       = 0;
                    height      = 0;
                }
            }

            if (isNeedGetOriginal) {
                fileName    = data.getFileName();
                width       = mThumbnailWidth;
                height      = mThumbnailHeight;
            }

//            String fileName;
//            int width, height;
//
//            if (data.getThumbFileName() == null) {
//                fileName    = data.getFileName();
////                width       = BitmapMaker.THUMBNAIL_WIDTH;
////                height      = BitmapMaker.THUMBNAIL_HEIGHT;
//                width       = mThumbnailWidth;
//                height      = mThumbnailHeight;
//            }
//            else {
//                fileName    = data.getThumbFileName();
//                width       = 0;
//                height      = 0;
//            }

            loadAsyncImage(holder.bitmapView, res, fileName, data.getFileName(), width, height, mLoadingBitmap);
            holder.data = data;
        }

        if (holder.deleteView != null) {
            holder.deleteView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.shouldDelete(position, data, SimpleImageDataAdapter.this);
                    }
                }
            });
        }

        return view;
    }

    //================================================
    // Holder
    //================================================
    private class Holder {
        public ImageView    bitmapView;
        public View         deleteView;
        public ImageData    data;
    }

    //================================================
    // OnDeleteListener
    //================================================
    public interface OnDeleteListener {
        void shouldDelete(int position, ImageData data, SimpleImageDataAdapter adapter);
    }
}
