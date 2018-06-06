package app.jni;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import app.application.GBApplication;
import app.data.http.GomipoiGameSaveParam;
import app.define.GarbageId;
import app.define.PartsEventId;
import app.define.SeData;
import app.service.SaveDataService;
import app.uuid.OnUUIDManagerListener;
import app.uuid.UUIDManager;
import lib.opengl.jni.GLJniBridgeBase;

/**
 * Jniの橋渡しクラス
 */
public class JniBridge extends GLJniBridgeBase {

    // ------------------------------
    // Member
    // ------------------------------

    // ------------------------------
    // Constructor
    // ------------------------------
    public JniBridge(@NonNull Context context) {
        super(context);
    }

    // ------------------------------
    // Native
    // ------------------------------
    private native void nativeForegroundApplication();
    private native void nativeBackgroundApplication();
    public static native void nativeSetInitData(double bonesStart, double addGarbageStart, double heroDrinkAppearTime, double zDrinkStartTime, double dropStartTime);
    public static native void nativeOnCheckTime(int isForegroundFirst);
    public static native int nativeGetGem();
    public static native int nativeGetPoint();
    public static native int nativeGetLevel();
    public static native double nativeGetFullness();
    public static native int nativeGetExperiencePoint();
    public static native int nativeGetCurrentLevelRequiredPoint();
    public static native int nativeGetNextLevelRequiredPoint();
    public static native void nativeAddGem(int addValue);
    public static native void nativeAddPoint(int addValue);
    public static native int nativeGetCurrentBroomType();
    public static native void nativeChangeCharacter(int type);
    public static native void nativeChangeBroomType(int type);
    public static native int nativeGetCurrentGarbageCanType();
    public static native void nativeChangeGarbageCanType(int type);
    public static native void nativeUseSeal();
    public static native void nativeUseTelephone();
    public static native void nativeOnReceivedUserAppsSelfResponse(String json);
    public static native void nativeOnReceivedGomipoiGarbageOwnResponse(String json);
    public static native void nativeOnReceivedGomipoiGameLoadResponse(String json);
    public static native void nativeOnReceivedGomipoiItemOwnResponse(String json);
    public static native int nativeGetItemOwnCount(String item_code);
    public static native boolean nativeIsItemUsing(String item_code);
    public static native boolean nativeIsLimitItemUsing(String item_code);
    public static native void nativeOnReceivedGomipoiBookOwnResponse(String json);
    public static native void nativeOnReceivedGomipoiGarbageSynthesesResponse(String json);
    public static native void nativeOnReceivedGomipoiGameSaveResponse(String json);
    public static native boolean nativeIsUnlockBook(String garbage_code);
    public static native boolean nativeIsNewBook(String garbage_code);
    public static native int[] nativeGetPageBonus();
    public static native void nativeOnReceivedBookReceiveBonusesResponse(String json);
    public static native void nativeOnStartGetData();
    public static native void nativeOnFinishedGetData();
    public static native int nativeGetGarbageBonus(int garbageId);
    public static native String nativeGetNewFoundGarbages();
    public static native void nativeLogout();
    public static native String nativeGetCurrentDate();
    public static native int[] nativeGetBookGarbages();
    public static native boolean nativeIsRareGarbage(String garbage_code);
    public static native void nativeChangeStage(int stage);
    public static native void nativeSaveData(int nextStage);
    public static native int nativeGetCurrentStage();
    public static native int nativeGetMaxLevel();

    public static native void nativeOnUsedZDrink();
    public static native void nativeOnUsedDrop();
    public static native void nativeOnUsedAutoBroom();
    public static native void nativeOnUsedBattery();
    public static native int nativeGetFriendCount();

    public static native boolean nativeIsUsedZDrink();
    public static native boolean nativeIsUsedDrop();
    public static native boolean nativeIsBonusTime();
    public static native double nativeGetCurrentTime();
    public static native boolean nativeIsGameActive();


    // ------------------------------
    // JNICallback
    // ------------------------------
    /**
     * パーツのクリック時に呼ばれる
     * @param partsId [int]
     */
    @Override
    public final void onPartsClicked(final int partsId) {
        if (!isActive()) {
            return;
        }

        if (isLockedEvent()) {
            return;
        }
        lockEvent();

        // UIスレッドに処理を移す
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mHandler.removeCallbacks(this);
                // TODO
            }
        });
    }

    /**
     * パーツのイベント時に呼ばれる
     * @param eventId [int]
     */
    @Override
    public final void onPartsEvent(final int eventId) {
        if (!isActive()) {
            return;
        }

        // UIスレッドに処理を移す
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mHandler.removeCallbacks(this);

                switch (PartsEventId.valueOf(eventId)) {
                    case PlayBroomSe: {
                        if (getApp() != null) {
                            getApp().getSeManager().playSe(SeData.BROOM);
                        }
                        break;
                    }

                    case FullGarbages: {
                        // TODO SEか何かを流すならココ!
//                        DebugLog.i("FullGarbages!");
                        break;
                    }

                    case LevelUp: {
                        if (getApp() != null) {
                            getApp().getSeManager().playSe(SeData.LEVELUP);
                        }
                        break;
                    }
                }
            }
        });
    }

    public final void onSaveOtherInfo(double bonusStartTime,
            double addGarbageStartTime, double heroDrinkAppearTime, double zDrinkStartTime, double dropStartTime) {
        getApp().getPreferenceManager().setBonusStartTime(bonusStartTime);
        getApp().getPreferenceManager().setAddGarbageStartTime(addGarbageStartTime);
        getApp().getPreferenceManager().setHeroDrinkAppearTime(heroDrinkAppearTime);
        getApp().getPreferenceManager().setZDrinkStartTime(zDrinkStartTime);
        getApp().getPreferenceManager().setDropStartTime(dropStartTime);
    }

    public final void onSaveGameData(final int place_type, final int add_point, final int put_in_garbage_count, final String garbages, final int broom_use_count, final int broom_broken, final int nextStage, final int isGarbageCanBroken) {
        // UIスレッドに処理を移す
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mHandler.removeCallbacks(this);
                if (getJniBridgeListener() != null) {
                    getJniBridgeListener().onRequestSaveGame(add_point, put_in_garbage_count, garbages, broom_use_count, broom_broken);
                }

                getApp().getSaveDataServiceManager().getService().saveData(
                        new GomipoiGameSaveParam(
                                GomipoiGameSaveParam.PlaceType.valueOf(place_type + 1),
                                add_point,
                                put_in_garbage_count,
                                garbages,
                                broom_use_count,
                                broom_broken == 1,
                                nativeGetNewFoundGarbages(),
                                nextStage,
                                isGarbageCanBroken == 1),
                        UUIDManager.getUUID(context, new OnUUIDManagerListener() {
                            @Override
                            public void UUIDManager_OnPermissionError() {
                            }

                            @Override
                            public void UUIDManager_OnStorageError() {
                            }
                        }), new SaveDataService.RequestSaveDataCallback() {
                            @Override
                            public void onFinish() {
                                if (getJniBridgeListener() != null) {
                                    getJniBridgeListener().onFinishSaveGame();
                                }
                            }
                        });
            }
        });

    }

    /**
     * 宝石数に変更があった場合に呼ばれる
     */
    public final void onChangedGem(final int gem) {
        if (mHandler != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mHandler.removeCallbacks(this);
                    if (getJniBridgeListener() != null) {
                        getJniBridgeListener().onChangedGem(gem);
                    }
                }
            });
        }
    }

    /**
     * ジロキチの隠れ家の宝石を全てとった場合に呼ばれる
     */
    public final void onUndergroundGemGot(final int gem) {
        if (mHandler != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mHandler.removeCallbacks(this);

                    if (getJniBridgeListener() != null) {
                        getJniBridgeListener().onUndergroundGemGot(gem);
                    }
                }
            });
        }
    }

    /**
     * 部屋のひみつをクリアした場合に呼ばれる
     */
    public final void onClearMission(final int missionId) {
        if (mHandler != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mHandler.removeCallbacks(this);
                    if (getJniBridgeListener() != null) {
                        getJniBridgeListener().onClearMission(missionId);
                    }
                }
            });
        }
    }

    public final void onChangedPoint(final int point) {
        if (mHandler != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mHandler.removeCallbacks(this);
                    if (getJniBridgeListener() != null) {
                        getJniBridgeListener().onChangedPoint(point);
                    }
                }
            });
        }
    }

    public final void onChangedFullness(final double fullness) {
        if (mHandler != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mHandler.removeCallbacks(this);
                    if (getJniBridgeListener() != null) {
                        getJniBridgeListener().onChangedFullness(fullness);
                    }
                }
            });
        }
    }

    public final void onChangedLevel(final int level) {
        if (mHandler != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mHandler.removeCallbacks(this);
                    if (getJniBridgeListener() != null) {
                        getJniBridgeListener().onChangedLevel(level);
                    }
                }
            });
        }
    }

    public final void onBrokenBroom(final int broomType) {
        if (mHandler != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mHandler.removeCallbacks(this);
                    if (getJniBridgeListener() != null) {
                        getJniBridgeListener().onBrokenBroom(broomType);
                    }
                }
            });
        }
    }

    public final void onBrokenGarbageCanXl() {
        if (mHandler != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mHandler.removeCallbacks(this);
                    if (getJniBridgeListener() != null) {
                        getJniBridgeListener().onBrokenGarbageCan();
                    }
                }
            });
        }
    }

    public final void onFoundGarbage(final String idList) {
        if (mHandler != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mHandler.removeCallbacks(this);

                    if (getJniBridgeListener() != null) {
                        getJniBridgeListener().onRequestFoundGarbage(idList);
                    }
                }
            });
        }
    }

    public final void onGetPoint(final int point) {
        if (mHandler != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mHandler.removeCallbacks(this);
                    if (getJniBridgeListener() != null) {
                        getJniBridgeListener().onShowPoint(point);
                    }
                }
            });
        }
    }

    public final void onGetGem(final int gem) {
        if (mHandler != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mHandler.removeCallbacks(this);
                    if (getJniBridgeListener() != null) {
                        getJniBridgeListener().onShowGem(gem);
                    }
                }
            });
        }
    }

    public final void onGetComboBonus(final int comboCount, final int point) {
        if (mHandler != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mHandler.removeCallbacks(this);
                    if (getJniBridgeListener() != null) {
                        getJniBridgeListener().onShowComboBonus(comboCount, point);
                    }
                }
            });
        }
    }

    public final void onSucceededSyntheses(final int garbageId) {
        if (mHandler != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mHandler.removeCallbacks(this);
                    if (getJniBridgeListener() != null) {
                        getJniBridgeListener().onShowSucceededSyntheses(GarbageId.valueOf(garbageId));
                    }
                }
            });
        }
    }

    public final void onEnterUnderground(final boolean enterJirokichi)
    {
        if (mHandler != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mHandler.removeCallbacks(this);
                    if (getJniBridgeListener() != null) {
                        getJniBridgeListener().onEnterUnderground(enterJirokichi);
                    }
                }
            });
        }
    }

    public final void onChangeStage(final int stageId)
    {
        if (mHandler != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mHandler.removeCallbacks(this);
                    if (getJniBridgeListener() != null) {
                        getJniBridgeListener().onChangeStage(stageId);
                    }
                }
            });
        }
    }

    public final void onRemainingBonusTime(final int remainingSeconds)
    {
        if (mHandler != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mHandler.removeCallbacks(this);
                    if (getJniBridgeListener() != null) {
                        getJniBridgeListener().onRemainingBonusTime(remainingSeconds);
                    }
                }
            });
        }
    }

    // ------------------------------
    // Override
    // ------------------------------
    @Override
    public boolean isLocked() {
        return getApp().isLockedEvent();
    }

    @Override
    protected void lockEvent() {
        getApp().lockEvent();
    }

    @Override
    protected void unlockEvent() {
        getApp().unlockEvent();
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    public void onForegroundApplication() {
        nativeForegroundApplication();

        // TODO 端末に保存するデータをセットする
        nativeSetInitData(
            getApp().getPreferenceManager().getBonusStartTime(),
            getApp().getPreferenceManager().getAddGarbageStartTime(),
            getApp().getPreferenceManager().getHeroDrinkAppearTime(),
            getApp().getPreferenceManager().getZDrinkStartTime(),
            getApp().getPreferenceManager().getDropStartTime()
        );
    }

    public void onBackgroundApplication() {
        nativeBackgroundApplication();
    }

    // ------------------------------
    // Function
    // ------------------------------
    /**
     * JPApplicationのインスタンスを返す
     */
    private GBApplication getApp() {
        return (GBApplication)context;
    }

    /**
     * OnGLJniBridgeListenerのインスタンスを返す
     */
    private OnGLJniBridgeListener getJniBridgeListener() {
        if (mJniListener == null) {
            return null;
        }
        return (OnGLJniBridgeListener)mJniListener;
    }

}
