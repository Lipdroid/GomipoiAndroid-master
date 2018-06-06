package app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import common.fragment.GBFragmentBase;

/**
 *
 * Created by kazuya on 2017/05/23.
 */
public class LicenseFragment extends GBFragmentBase {

    /**
     *
     * @return license fragment
     */
    public static LicenseFragment newInstance() {
        LicenseFragment fragment = new LicenseFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return new WebView(getActivity());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        if (view != null) {
            WebView webView = (WebView) view;
            webView.loadUrl("file:///android_asset/license/license.html");
        }
    }
}
