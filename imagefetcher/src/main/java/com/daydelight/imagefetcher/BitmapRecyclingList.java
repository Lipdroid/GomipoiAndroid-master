package com.daydelight.imagefetcher;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.lang.ref.SoftReference;
import java.util.ArrayList;

/**
 * Created by Herve on 2016/03/16.
 */
public class BitmapRecyclingList {

    //================================================
    // Fields
    //================================================
    private static final int DEFAULT_MAX_SIZE = 15;
    private int mMaxSize;
    private ArrayList<SoftReference<Bitmap>> mRecycleList;

    public BitmapRecyclingList() {
        mMaxSize        = DEFAULT_MAX_SIZE;
        mRecycleList    = new ArrayList<>(mMaxSize + 1);
    }

    //================================================
    // Recycling
    //================================================
    public synchronized void addToRecycleList(@NonNull Bitmap bitmap) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }

        SoftReference<Bitmap> reference = new SoftReference<>(bitmap);
        mRecycleList.add(reference);

        if (mRecycleList.size() >= mMaxSize) {
            clean();
            if (mRecycleList.size() >= mMaxSize) {
                mRecycleList.remove(0);
            }
        }
    }

    @Nullable
    public synchronized Bitmap findRecycleBitmap(BitmapFactory.Options options) {
        int byteAllocationNeeded = options.outWidth * options.outHeight * 4;

        int i = 0;
        while(i < mRecycleList.size()) {
            boolean clean = false;

            SoftReference<Bitmap> reference = mRecycleList.get(i);
            if (reference.get() != null) {
                Bitmap bitmap = reference.get();

                if (!bitmap.isRecycled()) {
                    if (bitmap.isMutable()) {
                        int byteAllocation;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                            byteAllocation = bitmap.getAllocationByteCount();
                            if (byteAllocation >= byteAllocationNeeded) {
                                mRecycleList.remove(i);
                                return bitmap;
                            }
                        }
                        else {
                            if (bitmap.getWidth() == options.outWidth && bitmap.getHeight() == options.outHeight && options.inSampleSize == 1) {
                                mRecycleList.remove(i);
                                return bitmap;
                            }
                        }
                    }
                    else clean = true;
                }
                else clean = true;
            }
            else clean = true;

            if (clean) {
                mRecycleList.remove(i);
            }
            else i++;
        }

        return null;
    }

    private void clean() {
        int i = 0;
        while(i < mRecycleList.size()) {
            SoftReference<Bitmap> reference = mRecycleList.get(i);
            Bitmap bitmap = reference.get();
            if (bitmap == null || bitmap.isRecycled() || !bitmap.isMutable()) {
                mRecycleList.remove(i);
            }
            else i++;
        }
    }

    public synchronized void clearList() {
        for (SoftReference<Bitmap> reference : mRecycleList) {
            Bitmap bitmap = reference.get();
            if (bitmap != null && !bitmap.isRecycled())
                bitmap.recycle();
        }

        mRecycleList.clear();
    }
}
