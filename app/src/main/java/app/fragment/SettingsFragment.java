package app.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.topmission.gomipoi.R;

import java.util.ArrayList;
import java.util.List;

import app.adapter.SettingsAdapter;
import app.data.SettingsItem;
import app.define.DialogCode;
import app.define.SettingsType;
import common.fragment.GBFragmentBase;
import lib.application.ApplicationUtils;
import app.define.ChangeActivityCode;
import common.activity.GBActivityBase;
import lib.fragment.OnFragmentListener;

/**
 * 設定画面
 */
public class SettingsFragment extends GBFragmentBase implements AdapterView.OnItemClickListener {

    // ------------------------------
    // NewInstance
    // ------------------------------
    /**
     * インスタンスを返す
     */
    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    // ------------------------------
    // Member
    // ------------------------------
    private ListView listView;
    public WebView webView;

    // ------------------------------
    // Override
    // ------------------------------
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, null);
        if (view == null) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        List<SettingsItem> dataList = new ArrayList<>();
        dataList.add(new SettingsItem(
                SettingsType.WITH_DESCRIPT,
                SettingsItem.TITLE_USERID,
                getApp().getPreferenceManager().getUserId()));
        dataList.add(new SettingsItem(
                SettingsType.WITH_DESCRIPT,
                SettingsItem.TITLE_VERSION,
                "v." + ApplicationUtils.getApplicationVersion(getApplicationContext())));
        dataList.add(new SettingsItem(
                SettingsType.WITH_SWITCH,
                SettingsItem.TITLE_BGM,
                null));
        dataList.add(new SettingsItem(
                SettingsType.WITH_SWITCH,
                SettingsItem.TITLE_SE,
                null));
        dataList.add(new SettingsItem(
                SettingsType.ONLY_TITLE,
                SettingsItem.TITLE_CONTACT,
                null));
        if (getApp().getIsGuest()) {
            // ゲストユーザーは会員登録を表示
            dataList.add(new SettingsItem(
                    SettingsType.ONLY_TITLE,
                    SettingsItem.TITLE_USER_REGISTER,
                    null));
        } else  {
            // 登録済みユーザーはログアウトを表示
            dataList.add(new SettingsItem(
                    SettingsType.ONLY_TITLE,
                    SettingsItem.TITLE_LOGOUT,
                    null));
        }

        dataList.add(new SettingsItem(SettingsType.ONLY_TITLE,
                SettingsItem.TITLE_OPEN_SOURCE_LICENSE,
                null));

        listView = (ListView) view.findViewById(R.id.listView);
        if (listView != null) {
            listView.setAdapter(new SettingsAdapter(getApplicationContext(), dataList, null));
            listView.setOnItemClickListener(this);
        }

        webView = (WebView) view.findViewById(R.id.webView);
        if (webView != null) {
            webView.loadUrl("file:///android_asset/license/license.html");
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        if (listView != null) {
            listView.setAdapter(null);
            listView.setOnItemClickListener(null);
        }

        super.onDestroyView();
    }

    @Override
    protected int getAdContainerId() {
        return R.id.layoutAd;
    }

    // ------------------------------
    // AdapterView.OnItemClickListener
    // ------------------------------
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (isLocked()) {
            return;
        }
        lockEvent();

        Object tmpData = parent.getItemAtPosition(position);
        if (tmpData == null || !(tmpData instanceof SettingsItem)) {
            unlockEvent();
            return;
        }

        SettingsItem item = (SettingsItem)tmpData;
        if (!item.isItemClickable()) {
            unlockEvent();
            return;
        }

        if (item.getTitle().equals(SettingsItem.TITLE_CONTACT)) {
            // お問い合わせ
            onChangeActivity(ChangeActivityCode.Contact, GBActivityBase.ContactType.NORMAL);

        } else if (item.getTitle().equals(SettingsItem.TITLE_LOGOUT)) {
            // ログアウト
            onShowDialog(DialogCode.LogoutConfirm, null);

        } else if (item.getTitle().equals(SettingsItem.TITLE_USER_REGISTER)) {
            OnFragmentListener listener = getFragmentListener();
            if (listener != null) {
                listener.onChangedFragment(
                        this,
                        RegistFragment.newInstance(),
                        true,
                        RegistFragment.getName(getApplicationContext()),
                        true,
                        null);
            }
        } else if (item.getTitle().equals(SettingsItem.TITLE_OPEN_SOURCE_LICENSE)) {
            /*OnFragmentListener listener = getFragmentListener();
            if (listener != null) {
                listener.onChangedFragment(this,
                        LicenseFragment.newInstance(),
                        true,
                        LicenseFragment.class.getSimpleName(),
                        true,
                        null);
            }*/

            webView.setVisibility(View.VISIBLE);
            unlockEvent();
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
        return context.getString(R.string.fragment_name_settings);
    }

}
