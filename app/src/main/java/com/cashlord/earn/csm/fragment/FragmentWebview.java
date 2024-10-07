package com.cashlord.earn.csm.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.fragment.app.Fragment;
import com.cashlord.earn.R;

public class FragmentWebview extends Fragment {

    private String targetUrl;
    private WebView webView;

    public FragmentWebview(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("url", targetUrl);
        super.onSaveInstanceState(outState);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_webview, container, false);

        if (savedInstanceState != null) {
            targetUrl = savedInstanceState.getString("url");
        }

        webView = view.findViewById(R.id.webView);
        if (webView != null) {
            webView.loadUrl(targetUrl);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new MyBrowser());
        }

        return view;
    }

    private static class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.destroy();
            webView = null;
        }
    }
}
