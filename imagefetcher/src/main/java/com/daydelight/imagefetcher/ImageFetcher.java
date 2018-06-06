package com.daydelight.imagefetcher;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.annotation.UiThread;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

/**
 * This is a helper class that fetched images from the Media Store.
 */
public class ImageFetcher {

    private static void getThumbnailData(Context context, ArrayList<ImageData> imageDatas) {
        if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            throw new SecurityException("READ_EXTERNAL_STORAGE permission is not granted.");

        ContentResolver resolver = context.getContentResolver();
        Uri uri = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI;
        Cursor cursor = null;
        try {
            cursor = MediaStore.Images.Thumbnails.queryMiniThumbnails(resolver, uri, MediaStore.Images.Thumbnails.MINI_KIND,
                    new String[]{MediaStore.Images.Thumbnails._ID, MediaStore.Images.Thumbnails.IMAGE_ID, MediaStore.Images.Thumbnails.DATA});
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int idColumn        = cursor.getColumnIndex(MediaStore.Images.Thumbnails._ID);
                int imageIdColumn   = cursor.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID);
                int dataColumn      = cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA);

                if (idColumn != -1 && imageIdColumn != -1 && dataColumn != -1) {
                    do {
                        try {
                            int id      = cursor.getInt(idColumn);
                            int imageId = cursor.getInt(imageIdColumn);
                            String data = cursor.getString(dataColumn);

                            for (ImageData imageData : imageDatas) {
                                if (imageData.getMediaStoreId() == imageId) {
                                    imageData.setThumbId(id);
                                    imageData.setThumbFileName(data);
                                    break;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            break;
                        }
                    } while (cursor.moveToNext());
                }
            }

            cursor.close();
        }
    }

    /**
     * Fetch asynchronously all images in the Media Store.
     * @param context The context to be used. Must not be null.
     * @param listener The listener that will return the data.
     */
    @Nullable
    @RequiresPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public static void getImageData(@NonNull final Context context, final OnImageDataListener listener) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                if (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    throw new SecurityException("READ_EXTERNAL_STORAGE permission is not granted.");

                ArrayList<ImageData> imageDatas = new ArrayList<>();

                Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                Cursor cursor = null;
                try {
                    cursor = context.getContentResolver().query(uri,
                            new String[]{MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA}, null, null, "_id DESC");
                } catch (SQLiteException e) {
                    e.printStackTrace();
                }

                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        int idColumn = cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                        int dataColumn = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);

                        if (idColumn == -1 || dataColumn == -1) {
                            imageDatas = null;
                        }
                        else {
                            do {
                                ImageData imageData = null;
                                try {
                                    int id = cursor.getInt(idColumn);
                                    String data = cursor.getString(dataColumn);

                                    if (id >= 0 && data != null)
                                        imageData = new ImageData(id, data);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    break;
                                }

                                if (imageData != null) {
                                    imageDatas.add(imageData);
                                }
                            } while (cursor.moveToNext());
                        }
                    }

                    cursor.close();
                }

                if (imageDatas != null) {
                    getThumbnailData(context, imageDatas);
                }

                final ArrayList<ImageData> finalDatas = imageDatas;
                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {
                        if (listener != null) {
                            listener.onImageDataGot(finalDatas);
                        }
                    }
                });
            }
        });
    }

    public interface OnImageDataListener {
        /**
         * Called when Media Store images are fetched. Called from the UI thread.
         * @param imageDatas A list of {@link com.daydelight.imagefetcher.ImageData}, or null if an error occurred.
         */
        @UiThread
        void onImageDataGot(ArrayList<ImageData> imageDatas);
    }
}
