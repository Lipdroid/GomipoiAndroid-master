package com.daydelight.imagefetcher;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A helper that loads Bitmap asynchronously.
 */
public class BitmapAsyncLoadHelper {

    //================================================
    // Fields
    //================================================
    private ExecutorService     mExecutor;
    private Handler             mHandler;
    private BitmapRecyclingList mRecyclingList;

    //================================================
    // Constructors
    //================================================
    public BitmapAsyncLoadHelper() {
        mExecutor       = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        mHandler        = new Handler(Looper.getMainLooper());
        mRecyclingList  = new BitmapRecyclingList();
    }

    //================================================
    // Shutdown
    //================================================
    /**
     * This must be called when done with the loading. Usually this will be called in {@link Activity#onDestroy()}.
     */
    public void shutdown() {
        mExecutor.shutdown();
        mRecyclingList = null;
    }

    public boolean isShutdown() {
        return mExecutor.isShutdown();
    }

    //================================================
    // Recycling
    //================================================
    public BitmapRecyclingList getBitmapRecyclingList() {
        return mRecyclingList;
    }

    //================================================
    // Loading
    //================================================
    /**
     * Loads asynchronously bitmap.
     * @param fileName The file name of the bitmap to decode.
     * @param exifFileName The file name of the exif data to use.
     * @param width The minimum width of the bitmap. If this is <=0, the original size will be used.
     * @param height The minimum height of the bitmap. If this is <=0, the original size will be used.
     * @param listener The listener that will return the bitmap.
     */
    public void loadBitmapFromFile(@NonNull final String fileName, @NonNull final String exifFileName, final int width, final int height, @NonNull final BitmapDecodeListener listener) {
        if (fileName == null)
            throw new NullPointerException("fileName must not be null");

        if (listener == null)
            throw new NullPointerException("listener must not be null");

        mExecutor.execute(new Runnable() {

            @Override
            public void run() {
                if (listener.isCancelled())
                    return;

                final Bitmap bitmap = BitmapMaker.makeBitmapFromFile(fileName, exifFileName, width, height, mRecyclingList);
                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        listener.onDecode(bitmap);
                    }
                });
            }
        });
    }

    //================================================
    // BitmapDecodeListener
    //================================================
    public abstract static class BitmapDecodeListener {
        private boolean mCancel;

        public BitmapDecodeListener() {
            mCancel = false;
        }

        /**
         * Cancel the decoding process.
         */
        public synchronized void cancel() {
            mCancel = true;
        }

        /**
         * @return Whether the decoding process has been cancelled.
         */
        public synchronized boolean isCancelled() {
            return mCancel;
        }

        /**
         * Called when the bitmap has been decoded. Implementations should check whether the decoding has been cancelled
         * with {@link BitmapDecodeListener#isCancelled()}, and whether the {@link Bitmap} is null.<br/>
         * This will be called in the UI thread.
         * @param bitmap The decoded bitmap, or null if an error occurred.
         */
        @UiThread
        public abstract void onDecode(@Nullable Bitmap bitmap);
    }
}
