package app.application;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.topmission.gomipoi.SplashActivity;

import java.lang.ref.WeakReference;

import app.activity.TopActivity;
import app.activity.TutorialActivity;
import app.data.http.GomipoiGameSaveParam;
import app.data.http.RegisterDeviceTokenParam;
import app.define.FragmentEventCode;
import app.googleanalytics.JPTracker;
import app.http.ConnectionManager;
import app.jni.JniBridge;
import app.manager.GameManager;
import app.manager.PlayerManager;
import app.preference.GBPreferenceManager;
import app.resize.ResizeLayoutManager;
import app.service.OnSaveDataServiceManagerListener;
import app.service.SaveDataService;
import app.service.SaveDataServiceManager;
import app.sound.BgmManager;
import app.sound.SeManager;
import common.activity.GBActivityBase;
import lib.application.ApplicationBase;
import lib.log.DebugLog;
import lib.timer.looping.OnLoopingTimerListener;

/**
 * アプリケーションクラス
 */
public class GBApplication extends ApplicationBase {

    // ------------------------------
    // Define
    // ------------------------------
    private static final String LIBRARY_NAME = "AppModule";

    // ------------------------------
    // Native
    // ------------------------------
    static {
        // JNI のライブラリ (モジュール) をロードします。
        System.loadLibrary(LIBRARY_NAME);
    }

    // ------------------------------
    // Member
    // ------------------------------
    private JniBridge mJniBridge;
    private BgmManager mBgmManager;
    private SeManager mSeManager;
    private GBPreferenceManager mPreferenceManager;
    private JPTracker mAnalyticsTracker;
    private GameManager mGameManager;
    private PlayerManager mPlayerManager;
    private ResizeLayoutManager mResizeManager;
    private int mDeviceWidth, mDeviceHeight;


    private boolean mIsLogouting;
    private String mAccessToken;
    private String mFriendCode;
    private String mNickname;
    private boolean mIsGuest;

    private OnApplicationListener mApplicationListener;

    private SaveDataServiceManager mSaveDataServiceManager;

    private boolean mIsWaitingLoadServerData;
    private ConnectionManager.RetryData mWaitingShowRetryDialogData;

    private boolean mIsForegroundFirst;

    // ------------------------------
    // Override
    // ------------------------------
    @Override
    public void onCreate() {
        super.onCreate();
        
        Intent intent = new Intent(getApplicationContext(), SaveDataService.class);
        bindService(
                intent,
                getSaveDataServiceManager().getServiceConnection(),
                Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onCreate(WeakReference<Activity> activityRef) {
        super.onCreate(activityRef);

        getSeManager();
        getAnalyticsTracker().onCreate();

        mIsForegroundFirst = true;
        mGameManager = new GameManager(new OnLoopingTimerListener() {

            @Override
            public void onRun(boolean isUIThread, Object data) {
                if (!isUIThread) {
                    return;
                }

                int isForegroundFirst = mIsForegroundFirst ? 1 : 0;
//                Log.i("DEBUG_LOG", "仕様変更対応 - 11 - isForegroundFirst:" + isForegroundFirst);
                JniBridge.nativeOnCheckTime(isForegroundFirst);
                if (JniBridge.nativeIsGameActive()) {
//                    Log.i("DEBUG_LOG", "仕様変更対応 - 12");
                    mIsForegroundFirst = false;
                }
            }

        });
    }

    public void setForegroundFirst(boolean isForegroundFirst) {
//        Log.i("DEBUG_LOG", "仕様変更対応 - 10 - isForegroundFirst:" + isForegroundFirst);
        mIsForegroundFirst = isForegroundFirst;
    }

    @Override
    protected void onBackground(WeakReference<Activity> activityRef) {
        getJniBridge().onBackgroundApplication();

        if (mSaveDataServiceManager != null) {
            mSaveDataServiceManager.setServiceManagerListener(null);
        }

        if (mGameManager != null) {
            mGameManager.onPause();
        }

        super.onBackground(activityRef);
    }

    @Override
    protected void onForeground(WeakReference<Activity> activityRef) {
        super.onForeground(activityRef);
        getJniBridge().onForegroundApplication();

        getSaveDataServiceManager().setServiceManagerListener(new OnSaveDataServiceManagerListener() {
            @Override
            public void onShowRetryDialog(ConnectionManager.RetryData retryData) {
                if(mApplicationListener != null) {
                    mApplicationListener.onShowRetryDialog(retryData);
                } else {
                    mWaitingShowRetryDialogData = retryData;
                }
            }

            @Override
            public void onSucceed(GomipoiGameSaveParam gameSaveParam) {
                if(mApplicationListener != null) {
                    mApplicationListener.onSendSaveData(gameSaveParam);
                }
            }

            @Override
            public void onShowAuthorizeErrorDialog() {
                if (mApplicationListener != null) {
                    mApplicationListener.onShowAuthorizeErrorDialog();
                }
            }

            @Override
            public void onAlreadyMaxCapacity(GomipoiGameSaveParam newGameSaveParam) {
                if (mApplicationListener != null) {
                    mApplicationListener.onAlreadyMaxCapacity(newGameSaveParam);
                }
            }
        });

        Activity activity = activityRef.get();
        // TopActivityはonResumeで最新情報をとるので、通知しなくても勝手に最新情報をとってくれる
        if (!(activity instanceof TopActivity)
                && !(activity instanceof SplashActivity)
                && !(activity instanceof TutorialActivity))
                //&& !(activityRef.get() instanceof LoginActivity))
                 {
            if (mApplicationListener != null) {
                mApplicationListener.onLoadServerData();
            } else {
                mIsWaitingLoadServerData = true;
            }
        }

        if (activity != null && activity instanceof GBActivityBase) {
            if (getAccessToken() != null && TopActivity.sShouldSendDeviceToken) {
                String token = FirebaseInstanceId.getInstance().getToken();
                if (token != null) {
                    ((GBActivityBase) activity).onEvent(FragmentEventCode.RegisterDeviceToken.getValue(), new RegisterDeviceTokenParam(token));
                }
            }
        }

        if (mGameManager != null) {
            mGameManager.onResume();
        }
    }

    @Override
    protected void onDestroy(WeakReference<Activity> activityRef) {
        mJniBridge = null;
        if (mBgmManager != null) {
            mBgmManager.stopAndRelease();
            mBgmManager = null;
        }
        mSeManager = null;
        mPreferenceManager = null;
        mResizeManager = null;
        mGameManager = null;
        mAccessToken = null;
        mFriendCode = null;
        mApplicationListener = null;

        super.onDestroy(activityRef);
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    /**
     * GLJniBridgeのインスタンスを返す
     */
    public final JniBridge getJniBridge() {
        if (mJniBridge == null) {
            mJniBridge = new JniBridge(getApplicationContext());
        }
        return mJniBridge;
    }

    public final BgmManager getBgmManager() {
        if (mBgmManager == null) {
            mBgmManager = new BgmManager(getApplicationContext());
        }
        return mBgmManager;
    }

    /**
     * SeManagerのインスタンスを返す
     */
    public final SeManager getSeManager() {
        if (mSeManager == null) {
            mSeManager = new SeManager(getApplicationContext());
        }
        return mSeManager;
    }

    /**
     * PreferenceManagerのインスタンスを返す
     */
    public final GBPreferenceManager getPreferenceManager() {
        if (mPreferenceManager == null) {
            mPreferenceManager = new GBPreferenceManager(getApplicationContext());
        }
        return mPreferenceManager;
    }

    /**
     * JPTrackerのインスタンスを返す
     */
    public final JPTracker getAnalyticsTracker() {
        if (mAnalyticsTracker == null) {
            mAnalyticsTracker = new JPTracker(getApplicationContext());
        }
        return mAnalyticsTracker;
    }

    /**
     * ResizeLayoutManagerのインスタンスを返す
     */
    public final ResizeLayoutManager getResizeManager() {
        if (mResizeManager == null) {
            if (mDeviceWidth == 0 || mDeviceHeight == 0) {
                return new ResizeLayoutManager(
                        getApplicationContext(),
                        mDeviceWidth,
                        mDeviceHeight);
            }

            mResizeManager = new ResizeLayoutManager(
                    getApplicationContext(),
                    mDeviceWidth,
                    mDeviceHeight);
        }
        return mResizeManager;
    }

    /**
     * 端末画面のサイズをセットする
     * @param width [int]
     * @param height [int]
     */
    public final void setDeviceSize(int width, int height) {
        mDeviceWidth = width;
        mDeviceHeight = height;
    }

    public final boolean isLogouting() {
        return mIsLogouting;
    }

    public final void logoutStart() {
        mIsLogouting = true;
    }

    public final void logoutEnd() {
        mIsLogouting = false;
    }

    public final String getAccessToken() {
        return mAccessToken;
    }

    public final void setAccessToken(String accessToken) {
        mAccessToken = accessToken;
    }

    public final String getFriendCode() {
        return mFriendCode;
    }

    public final void setFriendCode(String friendCode) {
        mFriendCode = friendCode;
    }

    public final String getNickname() { return mNickname; }

    public final void setNickname(String value) { mNickname = value; }

    public final boolean getIsGuest() { return mIsGuest; }

    public final void setIsGuest(boolean value) { mIsGuest = value; }

    public final void setApplicationListener(OnApplicationListener listener) {
        mApplicationListener = listener;

        if (mApplicationListener != null) {
            if (mIsWaitingLoadServerData) {
                mApplicationListener.onLoadServerData();
                mIsWaitingLoadServerData = false;
            }

            if (mWaitingShowRetryDialogData != null) {
                mApplicationListener.onShowRetryDialog(mWaitingShowRetryDialogData);
                mWaitingShowRetryDialogData = null;
            }
        }
    }

    public final PlayerManager getPlayerManager() {
        if (mPlayerManager == null) {
            mPlayerManager = new PlayerManager();
        }
        return mPlayerManager;
    }

    public final SaveDataServiceManager getSaveDataServiceManager() {
        if (mSaveDataServiceManager == null) {
            mSaveDataServiceManager = new SaveDataServiceManager();
        }
        return mSaveDataServiceManager;
    }

    public final boolean isNeedRelunchar() {
        return mDeviceWidth == 0 || mDeviceHeight == 0;
    }
}
