package com.daydelight.imagefetcher;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * Created by Herve on 2016/03/16.
 */
public class AsyncBitmapDrawable extends Drawable {

    //================================================
    // Fields
    //================================================
    private BitmapAsyncLoadHelper.BitmapDecodeListener mListener;

    private Drawable                    mNullDrawable;
    private WeakReference<ImageView>    mViewReference;

    //================================================
    // Constructor
    //================================================
    public AsyncBitmapDrawable() {
        mNullDrawable   = new ColorDrawable(Color.TRANSPARENT);
    }

    //================================================
    // Getters, setters
    //================================================
    public void setLoadingBitmap(@NonNull Resources resources, Bitmap bitmap) {
        if (bitmap == null) {
            mNullDrawable = new ColorDrawable(Color.TRANSPARENT);
        }
        else {
            mNullDrawable = new BitmapDrawable(resources, bitmap);
        }
    }

    public void cancel() {
        if (mListener != null) {
            mListener.cancel();
        }
    }

    //================================================
    // Loading
    //================================================
    public void displayBitmapFromFile(@NonNull ImageView view, @NonNull String fileName, @NonNull String exifFileName, int width, int height, @NonNull BitmapAsyncLoadHelper loader) {
        if (loader.isShutdown())
            return;

        mViewReference = new WeakReference<>(view);
        mListener = new BitmapAsyncLoadHelper.BitmapDecodeListener() {

            @Override
            public void onDecode(@Nullable Bitmap bitmap) {
                if (isCancelled())
                    return;

                if (bitmap != null) {
                    ImageView view = mViewReference.get();

                    if (view != null) {
                        view.setImageBitmap(bitmap);
                    }
                }
            }
        };
        loader.loadBitmapFromFile(fileName, exifFileName, width, height, mListener);
    }

    @Override
    public void draw(Canvas canvas) {
        mNullDrawable.draw(canvas);
    }

    @Override
    public void setAlpha(int alpha) {
        mNullDrawable.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mNullDrawable.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return mNullDrawable.getOpacity();
    }
}
