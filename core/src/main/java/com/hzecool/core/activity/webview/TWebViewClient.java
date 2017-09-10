package com.hzecool.core.activity.webview;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;

/**
 * Discribe:WebViewClient
 * 用来设置给WebView 在应用内打开网页
 * Created by tutu on 2017/3/29.
 */

public class TWebViewClient extends BridgeWebViewClient {

    public TWebViewClient(BridgeWebView webView) {
        super(webView);
    }
}
