package common.fragment;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import app.ad.banner.GBAdBannerManager;
import app.application.GBApplication;
import app.define.ChangeActivityCode;
import app.define.DialogCode;
import app.define.FragmentEventCode;
import lib.fragment.FragmentBase;
import lib.fragment.OnFragmentListener;

/**
 *
 */
public class GBFragmentBase extends FragmentBase {

    // ------------------------------
    // Member
    // ------------------------------
    private Handler mHandler;

    // ------------------------------
    // Override
    // ------------------------------
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        GBApplication app = getApp();
        if (app != null) {
            app.getResizeManager().resize(getView());
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        showAd();
    }

    @Override
    public void onDestroy() {
        mHandler = null;
        super.onDestroy();
    }

    // ------------------------------
    // Function
    // ------------------------------
    /**
     * Adを格納するViewのResourceIDを返す
     */
    protected int getAdContainerId() {
        return 0;
    }

    /**
     * JPApplicationのインスタンスを返す
     */
    protected final GBApplication getApp() {
        Application application = getActivity().getApplication();
        if (application == null || !(application instanceof GBApplication)) {
            return null;
        }
        return (GBApplication)application;
    }

    /**
     * ApplicationContextを返す
     */
    protected final Context getApplicationContext() {
        return getActivity().getApplicationContext();
    }

    /**
     * メインスレッドのHandlerを返す
     */
    protected final Handler getMainHandler() {
        return mHandler;
    }

    /**
     * Adを表示する
     */
    private void showAd() {
        if (getAdContainerId() == 0 || getView() == null) {
            return;
        }

        ViewGroup group = (ViewGroup) getView().findViewById(getAdContainerId());
        if (group != null) {
            View ad = GBAdBannerManager.getBanner(getActivity());
            group.addView(ad);
        }
    }

    /**
     * Activity変更要求をActivityに渡す
     * @param code [ChangeActivityCode]
     * @param data [Object]
     */
    protected final void onChangeActivity(ChangeActivityCode code, Object data) {
        OnFragmentListener listener = getFragmentListener();
        if (listener != null) {
            listener.onChangedActivity(code.getValue(), data);
        }
    }

    /**
     * イベントをActivityに渡す
     * @param code [FragmentEventCode]
     * @param data [Object]
     */
    protected final void onFragmentEvent(FragmentEventCode code, Object data) {
        OnFragmentListener listener = getFragmentListener();
        if (listener != null) {
            listener.onEvent(code.getValue(), data);
        }
    }

    /**
     * ダイアログを表示する
     * @param dialogCode [DialogCode]
     * @param data [Object]
     */
    protected final void onShowDialog(DialogCode dialogCode, Object data) {
        OnFragmentListener listener = getFragmentListener();
        if (listener != null) {
            listener.onShowDialog(dialogCode.getValue(), data);
        }
    }

    /**
     * Analyticsにスクリーン名を送る
     * @param screenName [String]
     */
    protected final void sendAnalyticsScreen(String screenName) {
        if (getApp() == null) {
            return;
        }

        getApp().getAnalyticsTracker().sendView(screenName);
    }


}
