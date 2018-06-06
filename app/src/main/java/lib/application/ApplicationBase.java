package lib.application;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.lang.ref.WeakReference;

import lib.event.EventManager;

/**
 * Applicationの基幹クラス
 */
public class ApplicationBase extends Application {

    // ------------------------------
    // Member
    // ------------------------------
    private EventManager mEventManager;

    // ------------------------------
    // Override
    // ------------------------------
    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(new LifeCycleCallbacks());
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    /**
     * イベントロック中かを返す
     */
    public final boolean isLockedEvent() {
        return getEventManager().isLockedEvent();
    }

    /**
     * イベントをロックする
     */
    public final void lockEvent() {
        getEventManager().lockEvent();
    }

    /**
     * イベントロックを解除する
     */
    public final void unlockEvent() {
        getEventManager().unlockEvent();
    }

    // ------------------------------
    // Function
    // ------------------------------
    protected void onCreate(WeakReference<Activity> activityRef) {
        mEventManager = new EventManager();
    }

    /**
     * ActivityがonResumeになった時に呼ばれる
     */
    protected void onActivityResume(WeakReference<Activity> activityRef) {
    }

    /**
     * アプリ終了時に呼ばれる
     */
    protected void onDestroy(WeakReference<Activity> activityRef) {
        mEventManager = null;
        System.gc();
    }

    /**
     * バックグラウンドに移動した時に呼ばれる
     */
    protected void onBackground(WeakReference<Activity> activityRef) {
        System.gc();
    }

    /**
     * フォアグラウンドに移動した時に呼ばれる
     */
    protected void onForeground(WeakReference<Activity> activityRef) {
    }

    /**
     * EventManagerのインスタンスを返す
     */
    protected final EventManager getEventManager() {
        if (mEventManager == null) {
            mEventManager = new EventManager();
        }
        return mEventManager;
    }


    // ------------------------------
    // Inner-Class
    // ------------------------------
    /**
     * LifeCycleCallbacks
     */
    private final class LifeCycleCallbacks implements ActivityLifecycleCallbacks {

        private int mRunningCount = 0;

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            if(mRunningCount == 0) {
                onCreate(new WeakReference<Activity>(activity));
            }
        }

        @Override
        public void onActivityStarted(Activity activity) {
            if (++mRunningCount == 1) {
                // 復帰時か起動時
                onForeground(new WeakReference<Activity>(activity));
            }
        }

        @Override
        public void onActivityResumed(Activity activity) {
            onActivityResume(new WeakReference<Activity>(activity));
        }

        @Override
        public void onActivityPaused(Activity activity) {
        }

        @Override
        public void onActivityStopped(Activity activity) {
            if (--mRunningCount == 0) {
                // バックグラウンドに回った
                onBackground(new WeakReference<Activity>(activity));
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            if(mRunningCount == 0) {
                // アプリの終了
                onDestroy(new WeakReference<Activity>(activity));
            }
        }
    }

}
