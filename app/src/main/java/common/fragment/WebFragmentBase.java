package common.fragment;

import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.util.HashMap;

import app.preference.PJniBridge;
import common.activity.GBActivityBase;
import lib.log.DebugLog;

/**
 * WebViewを管理するFragmentの基幹クラス
 */
public abstract class WebFragmentBase extends GBFragmentBase {

    // ------------------------------
    // Member
    // ------------------------------
    WebView webView;

    // ------------------------------
    // Override
    // ------------------------------
    @Override
    public void onDestroyView() {
        if (webView != null) {
            webView.stopLoading();
            webView.setWebChromeClient(null);
            webView.setWebViewClient(null);
            unregisterForContextMenu(webView);
            webView.destroy();
            webView = null;
        }

        super.onDestroyView();
    }

    // ------------------------------
    // Abstract
    // ------------------------------
    /**
     * URLを返す
     */
    protected abstract String getUrl();

    // ------------------------------
    // Accesser
    // ------------------------------
    /**
     * Webの戻る処理を行う
     */
    public final boolean goBackWeb() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return false;
    }

    // ------------------------------
    // Function
    // ------------------------------
    /**
     * WebViewをセットする
     */
    protected final void setWebView(WebView webView) {
        this.webView = webView;
        if (this.webView != null) {
            webView.clearCache(true);

            this.webView.getSettings().setJavaScriptEnabled(true);
            this.webView.getSettings().setLoadWithOverviewMode(true);
            this.webView.getSettings().setUseWideViewPort(true);
            this.webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

            this.webView.getSettings().setDefaultTextEncodingName("UTF-8");

            HashMap<String, String> map = new HashMap<>();
            map.put("X_PRIZE_EX_ACCESS_TOKEN", getApp().getAccessToken());
            map.put("X_PRIZE_EX_ACCESS_APPLICATION_ID", String.valueOf(PJniBridge.nativeGetApplicationId()));
            map.put("X_PRIZE_EX_ACCESS_DEVICE_ID", ((GBActivityBase)getActivity()).getDeviceId());
            map.put("X_PRIZE_EX_ACCESS_DEVICE_TYPE","2");
            map.put("X_PRIZE_EX_ACCESS_USER_TYPE",(getApp().getIsGuest() ? "1" : "0"));

            DebugLog.i("WebFragmentBase - URL: " + getUrl());
            DebugLog.i("WebFragmentBase - X_PRIZE_EX_ACCESS_TOKEN:" + getApp().getAccessToken());
            DebugLog.i("WebFragmentBase - X_PRIZE_EX_ACCESS_APPLICATION_ID:" + String.valueOf(PJniBridge.nativeGetApplicationId()));
            DebugLog.i("WebFragmentBase - X_PRIZE_EX_ACCESS_DEVICE_ID:" + ((GBActivityBase)getActivity()).getDeviceId());
            DebugLog.i("WebFragmentBase - X_PRIZE_EX_ACCESS_DEVICE_TYPE:2");
            DebugLog.i("WebFragmentBase - X_PRIZE_EX_ACCESS_USER_TYPE:" + (getApp().getIsGuest() ? "1" : "0"));
            this.webView.loadUrl(getUrl(), map);

        }
    }

}
