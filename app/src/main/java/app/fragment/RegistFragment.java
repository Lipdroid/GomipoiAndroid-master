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

import com.topmission.gomipoi.R;

import app.activity.SettingsActivity;
import app.activity.TopActivity;
import app.animation.button.ButtonAnimationManager;
import app.data.http.UserDefinitiveRegisterParam;
import app.data.http.UserSessionsParam;
import app.data.http.UsersParam;
import app.define.DialogCode;
import app.define.FragmentEventCode;
import app.define.SeData;
import common.fragment.GBFragmentBase;
import common.activity.GBActivityBase;
import lib.text.TextUtils;

/**
 * 会員登録画面
 */
public class RegistFragment extends GBFragmentBase implements View.OnClickListener {

    // ------------------------------
    // NewInstance
    // ------------------------------
    /**
     * インスタンスを返す
     */
    public static RegistFragment newInstance() {
        RegistFragment fragment = new RegistFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    // ------------------------------
    // Member
    // ------------------------------
    private EditText editTextAccount;
    private EditText editTextId;
    private EditText editTextPass;
    private Button buttonRegist;

    // ------------------------------
    // Override
    // ------------------------------
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_regist, null);
        if (view == null) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        editTextAccount = (EditText) view.findViewById(R.id.editTextAccount);
        if (editTextAccount != null) {
            InputFilter inputFilter = new InputFilter() {
                @Override
                public CharSequence filter(CharSequence source, int start, int end,
                                           Spanned dest, int dstart, int dend) {
                    if (TextUtils.isIncludeEmoji(source.toString())) {
                        return "";
                    } else {
                        return source;
                    }
                }
            };
            InputFilter[] filters = new InputFilter[] { inputFilter };
            editTextAccount.setFilters(filters);
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
        }

        buttonRegist = (Button) view.findViewById(R.id.buttonRegist);
        if (buttonRegist != null) {
            new ButtonAnimationManager(buttonRegist, this);
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
        if (buttonRegist != null) {
            buttonRegist.setOnClickListener(null);
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
            case R.id.buttonRegist: {
                if (editTextAccount == null || editTextId == null || editTextPass == null) {
                    onShowDialog(DialogCode.ERROR, null);
                    return;
                }

                if (editTextAccount.getText().toString().replaceAll(" ", "").replaceAll("　", "").length() == 0
                        || editTextId.getText().toString().replaceAll(" ", "").replaceAll("　", "").length() == 0
                        || editTextPass.getText().toString().replaceAll(" ", "").replaceAll("　", "").length() == 0) {
                    onShowDialog(DialogCode.InputShortage, null);
                    return;
                }

                //
                // 制限事項追加
                // ------------------------------
                String text = null;
                // Account 1 ~ 20文字 (全角・半角)
                text = editTextAccount.getText().toString();
                if (text.length() < 1 || text.length() > 20) {
                    onShowDialog(DialogCode.InputAccountOverTextLength, null);
                    return;
                }
                // UserID 6 ~ 20文字 (半角)
                text = editTextId.getText().toString();
                if (text.length() < 6 || text.length() > 20) {
                    onShowDialog(DialogCode.InputUserIdOverTextLength, null);
                    return;
                }
                // Password 6 ~ 20文字 (半角)
                text = editTextPass.getText().toString();
                if (text.length() < 6 || text.length() > 20) {
                    onShowDialog(DialogCode.InputPasswordOverTextLength, null);
                    return;
                }


                if (TextUtils.isIncludeEmoji(editTextAccount.getText().toString())) {
                    onShowDialog(DialogCode.InputSurrogate, null);
                    return;
                }

                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                v.requestFocus();

                String nickname = editTextAccount.getText().toString();
                String userId = editTextId.getText().toString();
                String password = editTextPass.getText().toString();

                if (getApp().getIsGuest()) {
                    // ゲストユーザーから会員登録
                    // ParentActivityは、GBActivityBaseで設定する
                    onFragmentEvent(
                            FragmentEventCode.PostUserDefinitiveRegister,
                            new UserDefinitiveRegisterParam(nickname, userId,password, null));

                } else  {
                    // 初回で会員登録
                    onFragmentEvent(
                            FragmentEventCode.PostUser,
                            new UsersParam(nickname, userId,password, UserSessionsParam.ParentActivity.LOGIN_ACTIVITY));
                }
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
        return context.getString(R.string.fragment_name_regist);
    }

}
