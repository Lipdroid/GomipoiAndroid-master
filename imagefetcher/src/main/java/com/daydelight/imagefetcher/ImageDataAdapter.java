package com.daydelight.imagefetcher;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v4.content.ContextCompat;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Herve on 2016/03/15.
 */
public abstract class ImageDataAdapter extends ArrayAdapter<ImageData> {

    //================================================
    // Fields
    //================================================
    private List<ImageData>         mDatas;
    private BitmapAsyncLoadHelper   mLoader;

    //================================================
    // Constructors
    //================================================
    public ImageDataAdapter(Context context, int resource, @NonNull BitmapAsyncLoadHelper loader) {
        super(context, resource);
        init(loader);
    }

    public ImageDataAdapter(Context context, int resource, ImageData[] objects, @NonNull BitmapAsyncLoadHelper loader) {
        super(context, resource, objects);

        mDatas = new ArrayList<>();
        for (ImageData data : objects)
            mDatas.add(data);
        init(loader);
    }

    public ImageDataAdapter(Context context, int resource, List<ImageData> objects, @NonNull BitmapAsyncLoadHelper loader) {
        super(context, resource, objects);

        mDatas = objects;
        init(loader);
    }

    private void init(BitmapAsyncLoadHelper loader) {
        if (loader == null)
            throw new NullPointerException("loader must not be null.");

        mLoader = loader;
    }

    //================================================
    // Getters. setters
    //================================================
    /**
     * Set the data used by this adapter.
     * @param datas The data to use.
     */
    public final void setImageDatas(@Nullable List<ImageData> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    @RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public final void deleteImageData(int position) {
        if (ContextCompat.checkSelfPermission(
                    getContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            throw new SecurityException("WRITE_EXTERNAL_STORAGE permission is not granted.");

        ImageData data = mDatas.get(position);
        mDatas.remove(position);

        String fileName = data.getFileName();

        if (fileName != null) {
            File file = new File(fileName);
            file.delete();
        }
        MediaScannerConnection.scanFile(getContext(), new String[]{fileName}, null, null);

        notifyDataSetChanged();
    }

    public final void remove(ImageData image) {
        if (!mDatas.contains(image)) {
            return;
        }

        mDatas.remove(image);
    }

    //================================================
    // Adapter methods
    //================================================
    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public ImageData getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long) position;
    }

    //================================================
    // ImageLoading
    //================================================
    protected final void loadAsyncImage(ImageView view, Resources res, String fileName, String exifFileName, int width, int height, Bitmap loadingBitmap) {
        Drawable drawable = view.getDrawable();
        if (drawable != null) {
            if (drawable instanceof AsyncBitmapDrawable) {
                AsyncBitmapDrawable oldDrawable = (AsyncBitmapDrawable) drawable;
                oldDrawable.cancel();
            }
            else if (drawable instanceof BitmapDrawable) {
                BitmapDrawable oldDrawable = (BitmapDrawable) drawable;
                Bitmap bitmap = oldDrawable.getBitmap();
                if (bitmap != null && bitmap.isMutable() && !bitmap.isRecycled()) {
                    mLoader.getBitmapRecyclingList().addToRecycleList(bitmap);
                }
            }
        }

        AsyncBitmapDrawable asyncDrawable = new AsyncBitmapDrawable();
        asyncDrawable.setLoadingBitmap(res, loadingBitmap);
        view.setImageDrawable(asyncDrawable);
        asyncDrawable.displayBitmapFromFile(view, fileName, exifFileName, width, height, mLoader);
    }
}
