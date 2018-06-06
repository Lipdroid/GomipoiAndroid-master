package app.wheel.lib;

import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

import app.wheel.lib.number.NumberWheelView;

/**
 */
public abstract class WheelManagerBase implements OnWheelChangedListener, OnWheelScrollListener {

    // ------------------------------
    // Member
    // ------------------------------
    private Context mContext;
    private Handler mHandler;

    private List<Object> mScrollStackQueue;
    private List<Integer> mScrollCompletedList;
    private List<WheelView> mWheelList;

    private boolean wheelScrolled = false;

    private OnWheelManagerListener mWheelManagerListener;

    // ------------------------------
    // Constructor
    // ------------------------------
    public WheelManagerBase(Context mContext) {
        this.mContext = mContext;
        mHandler = new Handler();
    }

    // ------------------------------
    // OnWheelChangedListener
    // ------------------------------
    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
    }

    // ------------------------------
    // OnWheelScrollListener
    // ------------------------------
    @Override
    public void onScrollingFinished(WheelView wheel) {
        if (wheel == null || mScrollCompletedList == null) {
            return;
        }
        mScrollCompletedList.add(wheel.getId());

        if (mScrollCompletedList.size() >= getWheelList().size()) {
            mScrollCompletedList.clear();
            mScrollCompletedList = null;

            if (getScrollStackQueue().size() > 0 && mHandler != null) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mHandler.removeCallbacks(this);
                        Object value = getScrollStackQueue().get(0);
                        getScrollStackQueue().remove(0);
                        forcedChangeValue(value);
                    }

                });
                return;
            }

            wheelScrolled = false;

            OnWheelManagerListener listener = getManagerListener();
            if (listener != null) {
                listener.WheelManager_OnEndedScroll();
            }
        }
    }

    @Override
    public void onScrollingStarted(WheelView wheel) {
        if (wheel == null) {
            return;
        }

        if (mScrollCompletedList == null) {
            wheelScrolled = true;
            mScrollCompletedList = new ArrayList<>();

            OnWheelManagerListener listener = getManagerListener();
            if (listener != null) {
                listener.WheelManager_OnStartedScroll();
            }
        }
    }

    // ------------------------------
    // Abstract
    // ------------------------------
    /**
     * WheelViewに初期データをセットする
     */
    protected abstract void setWheelInitData(Object initData, WheelView wheelView);

    /**
     * スタックキューを無視して、強制的に値を変更する場合の処理
     */
    protected abstract void forcedChangeValue(Object object);

    // ------------------------------
    // Accesser
    // ------------------------------
    /**
     * OnWheelManagerListenerをセットする
     */
    public final void setOnWheelManagerListener(OnWheelManagerListener listener) {
        mWheelManagerListener = listener;
    }

    /**
     * WheelViewを追加する
     */
    public void addWheelView(WheelView wheelView) {
        getWheelList().add(wheelView);
    }

    /**
     * onCreate時に呼ぶ
     */
    public void onCreate(Object initData) {
        for (int i = 0; i < getWheelList().size(); i++) {
            WheelView wheelView = getWheelList().get(i);
            if (wheelView == null || !(wheelView instanceof NumberWheelView)) {
                continue;
            }

            wheelView.addChangingListener(this);
            wheelView.addScrollingListener(this);
            wheelView.setCyclic(true);
            wheelView.setEnabled(false);

            setWheelInitData(initData, wheelView);
        }
    }

    /**
     * onDestroy時に呼ぶ
     */
    public void onDestroy() {
        if (mWheelList != null) {
            for (int i = 0; i < mWheelList.size(); i++) {
                WheelView wheelView = mWheelList.get(i);
                if (wheelView == null) {
                    continue;
                }
                wheelView.removeChangingListener(this);
                wheelView.removeScrollingListener(this);
            }
            mWheelList.clear();
            mWheelList = null;
        }

        if (mScrollCompletedList != null) {
            mScrollCompletedList.clear();
            mScrollCompletedList = null;
        }

        if (mScrollStackQueue != null) {
            mScrollStackQueue.clear();
            mScrollStackQueue = null;
        }

        mContext = null;
        mHandler = null;
        mWheelManagerListener = null;
    }

    /**
     * 値変更時の処理
     */
    public boolean changeValue(Object newValue) {
        // スクロール中の要求はスタックキューに溜めておく
        if (wheelScrolled) {
            getScrollStackQueue().add(newValue);
            return true;
        }

        forcedChangeValue(newValue);
        return true;
    }

    // ------------------------------
    // Function
    // ------------------------------
    /**
     * Contextのインスタンスを返す
     */
    protected final Context getContext() {
        return mContext;
    }

    /**
     * OnWheelManagerListenerのインスタンスを返す
     */
    protected final OnWheelManagerListener getManagerListener() {
        return mWheelManagerListener;
    }

    /**
     * WheelViewのリストを返す
     */
    protected final List<WheelView> getWheelList() {
        if (mWheelList == null) {
            mWheelList = new ArrayList<>();
        }
        return mWheelList;
    }

    /**
     * スタックキューを返す
     */
    private List<Object> getScrollStackQueue() {
        if (mScrollStackQueue == null) {
            mScrollStackQueue = new ArrayList<>();
        }
        return mScrollStackQueue;
    }

}
