package app.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.HttpAuthHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.topmission.gomipoi.R;

import app.preference.PJniBridge;
import common.fragment.WebFragmentBase;
import app.define.DebugMode;

/**
 * ランキング画面
 */
public class RankingFragment extends WebFragmentBase {

    // ------------------------------
    // NewInstance
    // ------------------------------
    /**
     * インスタンスを返す
     */
    public static RankingFragment newInstance() {
        RankingFragment fragment = new RankingFragment();
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
        View view = inflater.inflate(R.layout.fragment_ranking, container, false);
        if (view == null) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        WebView webView = (WebView) view.findViewById(R.id.webView);
        if (webView != null) {
            webView.setWebViewClient(new WebViewClient(){
                @Override
                public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
                    // Basic認証は開発用のみ使用
                    if (DebugMode.isTestServer){
                        handler.proceed(PJniBridge.nativeGetBasicUsername(), PJniBridge.nativeGetBasicPassword());
                    }
                }
            });
            setWebView(webView);
        }

        return view;
    }

    @Override
    protected String getUrl() {
        return DebugMode.isTestServer ? "http://dev-prize.mix-ict.net/ranking/index.php" :
                DebugMode.isStagingServer ? "https://stg-prize-exchange-web.top-mission-app.com/app/ranking" : "https://prize-exchange-web.top-mission-app.com/app/ranking";
    }

    @Override
    protected int getAdContainerId() {
        return R.id.layoutAd;
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    /**
     * フラグメント名を返す
     * @param context [ApplicationContext]
     */
    public static String getName(Context context) {
        return context.getString(R.string.fragment_name_ranking);
    }

}
