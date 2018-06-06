package lib.opengl.jni;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Handler;
import android.support.annotation.NonNull;

import lib.opengl.loading.GLLoadingManager;
import lib.opengl.loading.OnGLLoadingManagerListener;

/**
 *
 */
public abstract class GLJniBridgeBase {

    // ------------------------------
    // Define
    // ------------------------------
    protected final Context context;

    // ------------------------------
    // Member
    // ------------------------------
    protected Handler mHandler;
    protected OnGLJniBridgeListenerBase mJniListener;
    protected GLLoadingManager mLoadingManager;

    // ------------------------------
    // Constructor
    // ------------------------------
    public GLJniBridgeBase(@NonNull Context context) {
        this.context = context;
        mHandler = new Handler();

        mLoadingManager = new GLLoadingManager();
        mLoadingManager.setOnLoadingManagerListener(new OnGLLoadingManagerListener() {

            @Override
            public void onFinishedLoading() {
                if (mJniListener != null) {
                    mJniListener.onCompletedLoading();
                }
            }

        });
    }

    // ------------------------------
    // Override
    // ------------------------------
    @Override
    protected void finalize() throws Throwable {
        try {
            super.finalize();
        } finally {
            mHandler = null;
            mJniListener = null;

            if (mLoadingManager != null) {
                mLoadingManager.setOnLoadingManagerListener(null);
                mLoadingManager = null;
            }
        }
    }

    // ------------------------------
    // Native
    // ------------------------------
    protected native void nativeOnParentCreated();
    protected native void nativeOnParentDestroy();
    protected native void nativeOnSurfaceCreated();
    protected native void nativeOnSurfaceChanged(int width, int height);
    protected native void nativeOnDrawFrame();
    protected native void nativeOnTouchDown(float ptX, float ptY);
    protected native void nativeOnTouchMove(float ptX, float ptY);
    protected native void nativeOnTouchUp();
    protected native boolean nativeIsPausing();
    protected native void nativePauseEnd();
    protected native void nativePause();
    protected native boolean nativeIsSuspending();
    protected native void nativeForeground();
    protected native void nativeBackground();

    // ------------------------------
    // Abstract
    // ------------------------------
    public abstract boolean isLocked();
    protected abstract void lockEvent();
    protected abstract void unlockEvent();

    // ------------------------------
    // Accesser
    // ------------------------------
    /**
     * GLJniBridgeのコールバックリスナーをセットする
     * @param listener [OnGLJniBridgeListenerBase]
     */
    public void setOnGLJniBridgeListener(OnGLJniBridgeListenerBase listener) {
        mJniListener = listener;
    }

    /**
     * [JNI Callback兼用]
     * AssetManagerのインスタンスを返す
     */
    public final AssetManager getAssetManager() {
        return context.getResources().getAssets();
    }

    /**
     * [JNI Callback兼用]
     * イベントロック中かを返す
     */
    public final boolean isLockedEvent() {
        return isLocked();
    }

    /**
     * [JNI Callback兼用]
     * Loadingの終了時に呼ばれる
     */
    public final void onLoadingEnd() {
        // UIスレッドに処理を移す
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mHandler.removeCallbacks(this);
                mLoadingManager.finishedGlLoading();
            }
        });
    }

    /**
     * [JNI Callback兼用]
     * パーツのクリック時に呼ばれる
     * @param partsId [int]
     */
    public void onPartsClicked(final int partsId) {
    }

    /**
     * [JNI Callback兼用]
     * パーツのイベント時に呼ばれる
     * @param eventId [int]
     */
    public void onPartsEvent(final int eventId) {
    }

    //
    // Render関連
    // ------------------------------
    /**
     * 管理者がCreateされた時に呼ぶ
     */
    public void onParentCreated() {
        nativeOnParentCreated();
    }

    /**
     * 管理者がDestroyされる時に呼ぶ
     */
    public void onParentDestroy() {
        nativeOnParentDestroy();
    }

    /**
     * SurfaceViewがCreateされた時に呼ぶ
     */
    public void onSurfaceCreated() {
        // UIスレッドに処理を移す
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mHandler.removeCallbacks(this);
                startLoading();
            }
        });
        nativeOnSurfaceCreated();
    }

    /**
     * SurfaceViewの状態が変わった時に呼ぶ
     * @param width [int]
     * @param height [int]
     */
    public void onSurfaceChanged(int width, int height) {
        nativeOnSurfaceChanged(width, height);
    }

    /**
     * 描画フレーム毎に呼ぶ
     */
    public void onDrawFrame() {
        nativeOnDrawFrame();
    }

    //
    // Event関連
    // ------------------------------
    /**
     * タッチダウンイベント時に呼ぶ
     * @param ptX [float]
     * @param ptY [float]
     */
    public void onTouchDown(float ptX, float ptY) {
        nativeOnTouchDown(ptX, ptY);
    }

    /**
     * タッチムーブイベント時に呼ぶ
     * @param ptX [float]
     * @param ptY [float]
     */
    public void onTouchMove(float ptX, float ptY) {
        nativeOnTouchMove(ptX, ptY);
    }

    /**
     * タッチアップイベント時に呼ぶ
     */
    public void onTouchUp() {
        nativeOnTouchUp();
    }

    //
    // Loading関連
    // ------------------------------
    /**
     * [JNI Callback兼用]
     * ローディング中かを返す
     */
    public final boolean isLoading() {
        return mLoadingManager.isLoading();
    }

    /**
     * ローディングのPause時に呼ぶ
     */
    public void loadingPause() {
        mLoadingManager.onPause();
    }

    /**
     * ローディング開始時に呼ぶ
     */
    public void startLoading() {
        mLoadingManager.start();
    }

    //
    // Pause関連
    // ------------------------------
    /**
     * 一時停止中かを返す
     */
    public boolean isPausing() {
        return nativeIsPausing();
    }

    /**
     * 一時停止処理
     */
    public void pause() {
        if (isPausing()) {
            return;
        }

        nativePause();
    }

    /**
     * 再開処理
     */
    public void pauseEnd() {
        if (!isPausing()) {
            return;
        }

        nativePauseEnd();
    }

    //
    // Suspend関連
    // ------------------------------
    /**
     * サスペンド中かを返す
     */
    public boolean isSuspending() {
        return nativeIsSuspending();
    }

    /**
     * サスペンド処理
     */
    public void background() {
        if (isSuspending()) {
            return;
        }

        nativeBackground();
    }

    /**
     * サスペンド復帰処理
     */
    public void foreground() {
        if (!isSuspending()) {
            return;
        }

        nativeForeground();
    }

    // ------------------------------
    // Function
    // ------------------------------
    /**
     * Activeかどうかを返す
     */
    protected final boolean isActive() {
        return (!isLoading() && !isPausing() && !isSuspending());
    }

}
