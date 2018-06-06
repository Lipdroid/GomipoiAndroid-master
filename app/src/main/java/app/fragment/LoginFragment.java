package app.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.iid.FirebaseInstanceId;
import com.topmission.gomipoi.R;

import app.activity.TopActivity;
import app.animation.button.ButtonAnimationManager;
import app.data.http.RegisterDeviceTokenParam;
import app.data.http.UserSessionsParam;
import app.define.DialogCode;
import app.define.FragmentEventCode;
import app.define.SeData;
import app.notification.InstanceIDListenerService;
import app.uuid.OnUUIDManagerListener;
import common.fragment.GBFragmentBase;
import app.define.ChangeActivityCode;
import common.activity.GBActivityBase;

/**
 * 会員ログイン画面
 */
public class LoginFragment extends GBFragmentBase implements View.OnClickListener, OnUUIDManagerListener {

    // ------------------------------
    // NewInstance
    // ------------------------------
    /**
     * インスタンスを返す
     */
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    // ------------------------------
    // Member
    // ------------------------------
    private EditText editTextId;
    private EditText editTextPass;
    private Button buttonLogin;
    private Button buttonContact;

    private boolean isNew;
    private String deviceToken;

    // ------------------------------
    // Override
    // ------------------------------
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isNew = false;

        final InstanceIDListenerService notificationService = InstanceIDListenerService.getInstance();
        if (notificationService != null && !notificationService.isSetup()) {
            notificationService.setup(new InstanceIDListenerService.InstanceIDListenerServiceListener() {
                @Override
                public void onTokenRefresh(String token, boolean isNew) {
                    LoginFragment.this.isNew = isNew;
                    deviceToken = token;

                    String deviceToken = FirebaseInstanceId.getInstance().getToken();
                    if (TopActivity.sShouldSendDeviceToken && deviceToken != null) {
                        onFragmentEvent(FragmentEventCode.RegisterDeviceToken, new RegisterDeviceTokenParam(deviceToken));
                    }
                    notificationService.removeListener();
                }
            });
        }
        else {
            isNew = InstanceIDListenerService.isNew(getContext());
            deviceToken = InstanceIDListenerService.getDeviceToken(getContext());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        if (view == null) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        editTextId = (EditText) view.findViewById(R.id.editTextId);
        if (editTextId != null) {
            InputFilter inputFilter = new InputFilter() {
                @Override
                public CharSequence filter(CharSequence source, int start, int end,
                                           Spanned dest, int dstart, int dend) {
                    if (source.toString().matches("^[0-9a-zA-Z]+$")) {
                        return source;
                    } else {
                        return "";
                    }
                }
            };
            InputFilter[] filters = new InputFilter[] { inputFilter };
            editTextId.setFilters(filters);
            
			// Preferenceに保存していれば、IDを自動入力
            String userId = getApp().getPreferenceManager().getUserId();
            if (userId.length() > 0) {
                editTextId.setText(userId);
            }
        }

        editTextPass = (EditText) view.findViewById(R.id.editTextPass);
        if (editTextPass != null) {
            InputFilter inputFilter = new InputFilter() {
                @Override
                public CharSequence filter(CharSequence source, int start, int end,
                                           Spanned dest, int dstart, int dend) {
                    if (source.toString().matches("^[0-9a-zA-Z]+$")) {
                        return source;
                    } else {
                        return "";
                    }
                }
            };
            InputFilter[] filters = new InputFilter[] { inputFilter };
            editTextPass.setFilters(filters);
            
        	// Preferenceに保存していれば、パスワードを自動入力
            String password = getApp().getPreferenceManager().getUserPassword();
            if (password.length() > 0) {
                editTextPass.setText(password);
            }
        }

        buttonLogin = (Button) view.findViewById(R.id.buttonLogin);
        if (buttonLogin != null) {
            new ButtonAnimationManager(buttonLogin, this);
        }
        
        buttonContact = (Button)view.findViewById(R.id.buttonContact);
        if (buttonContact != null) {
            new ButtonAnimationManager(buttonContact, this);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        onFragmentEvent(FragmentEventCode.OnResume, null);
    }

    @Override
    public void onPause() {
        onFragmentEvent(FragmentEventCode.OnPause, null);
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        if (buttonLogin != null) {
            buttonLogin.setOnClickListener(null);
        }
        
        if (buttonContact != null) {
            buttonContact.setOnClickListener(null);
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
                if (editTextId == null || editTextPass == null) {
                    onShowDialog(DialogCode.ERROR, null);
                    return;
                }

                if (editTextId.getText().toString().replaceAll(" ", "").replaceAll("　", "").length() == 0
                        || editTextPass.getText().toString().replaceAll(" ", "").replaceAll("　", "").length() == 0) {
                    onShowDialog(DialogCode.InputShortage, null);
                    return;
                }

                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                v.requestFocus();

                onFragmentEvent(
                        FragmentEventCode.PostUserSessions,
                        new UserSessionsParam(
                                UserSessionsParam.ParentActivity.LOGIN_ACTIVITY,
                                editTextId.getText().toString(),
                                editTextPass.getText().toString(),
                                isNew ? deviceToken : null)
                        );
                break;
            }
            
			case R.id.buttonContact:
            {
                // お問い合わせのメーラーを起動
                onChangeActivity(ChangeActivityCode.Contact, GBActivityBase.ContactType.LOGIN_PASSWORD);
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
        return context.getString(R.string.fragment_name_login);
    }

    // ------------------------------
    // OnUUIDManagerListener
    // ------------------------------
    @Override
    public void UUIDManager_OnPermissionError() {
    }

    @Override
    public void UUIDManager_OnStorageError() {
    }

}
