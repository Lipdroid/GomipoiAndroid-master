package com.daydelight.imagefetcher;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * This class contains data relative to image files. It also includes thumbnail data.
 */
public class ImageData {

    //================================================
    // Fields
    //================================================
    private int     mMediaStoreId;
    private String  mFileName;
    private int     mThumbId;
    private String  mThumbFileName;

    //================================================
    // Constructors
    //================================================
    /**
     * ImageData constructor.
     * @param mediaStoreId The Media Store ID of the picture.
     * @param fileName
     */
    public ImageData(int mediaStoreId, @NonNull String fileName) {
        if (fileName == null)
            throw new NullPointerException("fileName must not be null.");

        mMediaStoreId   = mediaStoreId;
        mFileName       = fileName;
    }

    //================================================
    // Basic getters, setters
    //================================================
    public int getMediaStoreId() {
        return mMediaStoreId;
    }

    public String getFileName() {
        return mFileName;
    }

    public int getThumbId() {
        return mThumbId;
    }

    public void setThumbId(int thumbId) {
        mThumbId = thumbId;
    }

    public String getThumbFileName() {
        return mThumbFileName;
    }

    public void setThumbFileName(String thumbFileName) {
        mThumbFileName = thumbFileName;
    }

    //================================================
    // Advanced getters, setters
    //================================================
    /**
     * Get the corresponding {@link android.graphics.Bitmap} rotating it if necessary, base on Exif information.
     * @param options the options to use when decoding the Bitmap.
     * @return the rotated bitmap, or null if an error occurred.
     */
    @Nullable
    public Bitmap getImageBitmap(@Nullable BitmapFactory.Options options) {
        return BitmapMaker.makeBitmapFromFile(mFileName, mFileName, 0, 0, null);
    }

    /**
     * Get the corresponding thumbnail {@link android.graphics.Bitmap} rotating it if necessary, base on Exif information.
     * @return the rotated bitmap, or null if an error occurred.
     */
    @Nullable
    public Bitmap getThumbnailBitmap() {
        if (mThumbFileName == null) {
            return BitmapMaker.makeBitmapFromFile(mFileName, mFileName, BitmapMaker.THUMBNAIL_WIDTH, BitmapMaker.THUMBNAIL_HEIGHT, null);
        }
        else {
            return BitmapMaker.makeBitmapFromFile(mThumbFileName, mFileName, 0, 0, null);
        }
    }

    //================================================
    // Comparison
    //================================================
    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof ImageData) {
            ImageData data = (ImageData) o;
            if (mMediaStoreId == data.mMediaStoreId)
                return true;
            else return false;
        }
        else return false;
    }
}
