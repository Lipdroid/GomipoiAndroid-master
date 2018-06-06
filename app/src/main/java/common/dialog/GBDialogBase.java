package common.dialog;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;

import app.application.GBApplication;
import lib.dialog.DialogBase;

/**
 *
 */
public class GBDialogBase extends DialogBase {

    // ------------------------------
    // Member
    // ------------------------------
    private Handler mHandler;

    // ------------------------------
    // Constructor
    // ------------------------------
    public GBDialogBase() {
        super();
        mHandler = new Handler();
    }

    // ------------------------------
    // Override
    // ------------------------------
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        GBApplication app = getApp();
        if (app != null) {
            app.getResizeManager().resize(getContentView());
        }
        return dialog;
    }

    @Override
    public void onDestroyView() {
        mHandler = null;
        super.onDestroyView();
    }

    // ------------------------------
    // Function
    // ------------------------------
    /**
     * GBApplicationのインスタンスを返す
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

}
