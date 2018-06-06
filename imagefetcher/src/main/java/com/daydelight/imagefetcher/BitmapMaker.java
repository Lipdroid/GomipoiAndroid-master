package com.daydelight.imagefetcher;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;

/**
 * Created by Herve on 2016/03/15.
 */
public class BitmapMaker {

    //================================================
    // Constants
    //================================================
    public static final int THUMBNAIL_WIDTH     = 512;
    public static final int THUMBNAIL_HEIGHT    = 384;

    //================================================
    // Bitmap maker
    //================================================
    public static Bitmap makeBitmapFromFile(String fileName, String exifFileName, int reqWidth, int reqHeight, @Nullable BitmapRecyclingList recyclingList) {
        Bitmap bitmap = null;
        try {
            bitmap = decodeSampledBitmapFromFile(fileName, reqWidth, reqHeight, recyclingList);
        } catch (OutOfMemoryError e) {
            if (recyclingList != null)
                recyclingList.clearList();
            e.printStackTrace();
        }

        bitmap = BitmapMaker.rotateBitmap(bitmap, exifFileName, recyclingList);

        return bitmap;
    }

    //================================================
    // Bitmap decoding
    //================================================
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        // 写真の回転に対応
        if (height > reqHeight || width > reqWidth) {
            if (width >= height) {
                if (reqWidth < reqHeight) {
                    int tmp = reqWidth;
                    reqWidth = reqHeight;
                    reqHeight = tmp;
                }
            } else {
                if (reqWidth > reqHeight) {
                    int tmp = reqWidth;
                    reqWidth = reqHeight;
                    reqHeight = tmp;
                }
            }

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight, @Nullable BitmapRecyclingList recyclingList) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        if (reqWidth > 0 && reqHeight > 0) {
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        }
        else options.inSampleSize = 1;

        if (recyclingList != null) {
            Bitmap recycle = recyclingList.findRecycleBitmap(options);

            if (recycle != null) {
                options.inBitmap = recycle;
            }
        }

        // Decode bitmap with inSampleSize set
        options.inMutable = true;
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap decodeSampledBitmapFromFile(String fileName, int reqWidth, int reqHeight, @Nullable BitmapRecyclingList recyclingList) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fileName, options);

        // Calculate inSampleSize
        if (reqWidth > 0 && reqHeight > 0) {
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        }
        else options.inSampleSize = 1;

        if (recyclingList != null) {
            Bitmap recycle = recyclingList.findRecycleBitmap(options);

            if (recycle != null) {
                options.inBitmap = recycle;
            }
        }

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        options.inMutable = true;
        return BitmapFactory.decodeFile(fileName, options);
    }

    //================================================
    // Bitmap rotation
    //================================================
    private static boolean isJpeg(@NonNull String fileName) {
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".JPG") || fileName.endsWith(".JPEG");
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, String fileName, @Nullable BitmapRecyclingList recyclingList) {
        if (bitmap == null)
            return null;

        if (!isJpeg(fileName))
            return bitmap;

        int orientation = ExifInterface.ORIENTATION_UNDEFINED;
        try {
            ExifInterface exif  = new ExifInterface(fileName);
            orientation            = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }

        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            if (recyclingList != null)
                recyclingList.addToRecycleList(bitmap);
            else bitmap.recycle();
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            if (recyclingList != null)
                recyclingList.clearList();
            e.printStackTrace();
            return null;
        }
    }
}
