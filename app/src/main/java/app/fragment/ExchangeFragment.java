package app.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.HttpAuthHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.topmission.gomipoi.R;

import app.animation.button.ButtonAnimationManager;
import app.application.GBApplication;
import app.define.DebugMode;
import app.define.DialogCode;
import app.define.FragmentEventCode;
import app.define.GarbageId;
import app.define.SeData;
import app.jni.JniBridge;
import app.jni.OnGLJniBridgeListener;
import app.preference.PJniBridge;
import common.fragment.WebFragmentBase;
import lib.fragment.OnFragmentListener;
import lib.log.DebugLog;


/**
 * 景品交換画面
 */
public class ExchangeFragment extends WebFragmentBase implements OnGLJniBridgeListener {

    private final int PREMIUM_TICKET_COST = 1000;
    private WebView webView;

    // ------------------------------
    // NewInstance
    // ------------------------------
    /**
     * インスタンスを返す
     */
    public static ExchangeFragment newInstance() {
        ExchangeFragment fragment = new ExchangeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    // ------------------------------
    // Override
    // ------------------------------
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exchange, container, false);
        if (view == null) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        onFragmentEvent(FragmentEventCode.OpenConnectionDialog, null);
        webView = (WebView) view.findViewById(R.id.webView);
        if (webView != null) {
            webView.setWebViewClient(new WebViewClient(){
                @Override
                public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {

                    // Basic認証は開発用のみ使用
                    if (DebugMode.isTestServer){
                        handler.proceed(PJniBridge.nativeGetBasicUsername(), PJniBridge.nativeGetBasicPassword());
                    }
                }

                @SuppressWarnings("deprecation")
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (url.endsWith("regist_action")) {
                        // 会員登録画面に遷移
                        onRegistAction();
                        return true;
                    }
                    return false;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    DebugLog.i("ExchangeFragment - onPageFinished() called");
                    onFragmentEvent(FragmentEventCode.CloseConnectionDialog, null);
                }

            });
            setWebView(webView);
        }

        Button btnTicket = (Button) view.findViewById(R.id.btn_ticket);
        if (btnTicket != null) {
            new ButtonAnimationManager(btnTicket, v -> {
                if (isLocked()) return;

                lockEvent();
                if (getApp() != null) {
                    getApp().getSeManager().playSe(SeData.YES);
                }
                DebugLog.i("ExchangeFragment - URL: " + webView.getUrl());
                onShowDialog(DialogCode.ItemBuyTicket, null);
            });
        }

        return view;
    }

    @Override
    protected String getUrl() {

        // ステージングサーバ
        return DebugMode.isTestServer ? "http://dev-prize.mix-ict.net/keihin/index.php" :
                DebugMode.isStagingServer ? "https://stg-prize-exchange-web.top-mission-app.com/app/keihin" : "https://prize-exchange-web.top-mission-app.com/app/keihin";
    }

    @Override
    protected int getAdContainerId() {
        return R.id.layoutAd;
    }

    // ------------------------------
    // Acceser
    // ------------------------------
    /**
     * フラグメント名を返す
     * @param context [ApplicationContext]
     */
    public static String getName(Context context) {
        return context.getString(R.string.fragment_name_exchange);
    }
    
	// ------------------------------
    // Function
    // ------------------------------
    private void onRegistAction() {
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
    }

    @Override
    public void onResume() {
        super.onResume();
        getApp().getJniBridge().setOnGLJniBridgeListener(this);
    }

    @Override
    public void onPause() {
        getApp().getJniBridge().setOnGLJniBridgeListener(null);
        super.onPause();
    }

    public void onBuyProcess() {
        onFragmentEvent(FragmentEventCode.PostPremiumTicket, null);
        //DebugLog.i("ExchangeFragment - onBuyProcess Remaining Ticket: " + mRemainingTicketCount);
    }

    public void onBuyTicketSuccess() {
        JniBridge.nativeAddGem(-PREMIUM_TICKET_COST);
        onShowDialog(DialogCode.ItemBuyTicketSuccess, null);
    }

    public void onBuyTicketFailed() {
        DebugLog.i("ExchangeFragment - onBuyTicketFailed");
        onShowDialog(DialogCode.GemShortage, null);
    }

    public void reloadPage() {
        //webView.reload();
        setWebView(webView);
    }

    @Override
    public void onCompletedLoading() {

    }

    @Override
    public void onChangedGem(int gem) {
        DebugLog.i("ExchangeFragment - Current Gem = " + gem);

    }

    @Override
    public void onClearMission(int missionId) {

    }

    @Override
    public void onUndergroundGemGot(int gem) {

    }

    @Override
    public void onChangedPoint(int point) {

    }

    @Override
    public void onChangedFullness(double fullness) {

    }

    @Override
    public void onChangedLevel(int level) {

    }

    @Override
    public void onBrokenBroom(int broomType) {

    }

    @Override
    public void onBrokenGarbageCan() {

    }

    @Override
    public void onShowPoint(int point) {

    }

    @Override
    public void onShowGem(int gem) {

    }

    @Override
    public void onShowComboBonus(int comboCount, int point) {

    }

    @Override
    public void onShowSucceededSyntheses(GarbageId garbageId) {

    }

    @Override
    public void onRequestSaveGame(int add_point, int put_in_garbage_count, String garbages, int broom_use_count, int broom_broken) {

    }

    @Override
    public void onFinishSaveGame() {

    }

    @Override
    public void onRequestFoundGarbage(String idListText) {

    }

    @Override
    public void onEnterUnderground(boolean enterJirokichi) {

    }

    @Override
    public void onChangeStage(int stageId) {

    }

    @Override
    public void onRemainingBonusTime(int remainingSecond) {

    }
}
