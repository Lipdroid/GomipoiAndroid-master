package lib.event;

/**
 * イベント管理クラス
 * (Activity / Fragment / Viewのイベントロックは共通なので、このクラスで管理する)
 */
public class EventManager {

    // ------------------------------
    // Member
    // ------------------------------
    private boolean mIsLockedEvent;

    // ------------------------------
    // Constructor
    // ------------------------------
    public EventManager() {
        mIsLockedEvent = false;
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    /**
     * イベントロック中かを返す
     */
    public final boolean isLockedEvent() {
        return mIsLockedEvent;
    }

    /**
     * イベントをロックする
     */
    public final void lockEvent() {
        mIsLockedEvent = true;
    }

    /**
     * イベントロックを解除する
     */
    public final void unlockEvent() {
        mIsLockedEvent = false;
    }

}
