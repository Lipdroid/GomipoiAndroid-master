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

import app.define.DebugMode;
import app.preference.PJniBridge;
import common.fragment.WebFragmentBase;

/**
 * ヘルプ画面
 */
public class NewsFragment extends WebFragmentBase {

    // ------------------------------
    // Define
    // ------------------------------
    private static final String ARG_KEY_URL = "#1" + System.currentTimeMillis();

    // ------------------------------
    // NewInstance
    // ------------------------------
    /**
     * インスタンスを返す
     */
    public static NewsFragment newInstance(String url) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_KEY_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    // ------------------------------
    // Override
    // ------------------------------
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, null);
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
        return getArguments().getString(ARG_KEY_URL);
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
        return context.getString(R.string.fragment_name_news);
    }

}
