package app.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.topmission.gomipoi.R;

import app.animation.button.ButtonAnimationManager;
import app.define.SeData;
import app.define.DialogCode;
import common.fragment.GBFragmentBase;
import lib.fragment.OnFragmentListener;

/**
 * ログイン画面
 */
public class LoginTopFragment extends GBFragmentBase implements View.OnClickListener {

    // ------------------------------
    // NewInstance
    // ------------------------------
    /**
     * インスタンスを返す
     */
    public static LoginTopFragment newInstance() {
        LoginTopFragment fragment = new LoginTopFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    // ------------------------------
    // Member
    // ------------------------------
    private Button buttonLogin;
    private Button buttonGuest;

    // ------------------------------
    // Override
    // ------------------------------
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_top, null);
        if (view == null) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        buttonLogin = (Button) view.findViewById(R.id.buttonLogin);
        if (buttonLogin != null) {
            new ButtonAnimationManager(buttonLogin, this);
        }

        buttonGuest = (Button) view.findViewById(R.id.buttonGuest);
        if (buttonGuest != null) {
            new ButtonAnimationManager(buttonGuest, this);
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        if (buttonLogin != null) {
            buttonLogin.setOnClickListener(null);
        }

        if (buttonGuest != null) {
            buttonGuest.setOnClickListener(null);
        }

        super.onDestroyView();
    }

    // ------------------------------
    // View.OnClickListener
    // ------------------------------
    @Override
    public void onClick(View v) {
        if (isLocked()) {
            return;
        }
        lockEvent();

        if (getApp() != null) {
            getApp().getSeManager().playSe(SeData.YES);
        }

        switch (v.getId()) {
            case R.id.buttonLogin: {
                OnFragmentListener listener = getFragmentListener();
                if (listener != null) {
                    listener.onChangedFragment(
                            this,
                            LoginFragment.newInstance(),
                            true,
                            LoginFragment.getName(getApplicationContext()),
                            true,
                            null);
                }
                break;
            }

            case R.id.buttonGuest: {
                onShowDialog(DialogCode.LoginGuestConfirm, null);
                break;
            }

            default: {
                unlockEvent();
                break;
            }
        }
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    /**
     * フラグメント名を返す
     * @param context [ApplicationContext]
     */
    public static String getName(Context context) {
        return context.getString(R.string.fragment_name_logintop);
    }

}
