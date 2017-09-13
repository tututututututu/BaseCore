package com.hzecool.core.activity.webview;

import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;
import com.hzecool.core.R;
import com.hzecool.core.R2;
import com.hzecool.core.MvpBase.TBaseActivity;
import com.hzecool.core.common.utils.ToastUtils;
import com.hzecool.core.data.AppArouterConstants;

import butterknife.BindView;


@Route(path = AppArouterConstants.AR_URL_WEB_VIEW)
public class JsBridgeWebViewActivity extends TBaseActivity<IJsBridgeWebViewView, WebViewPresenter>
implements IJsBridgeWebViewView{

    @BindView(R2.id.webview)
    BridgeWebView webview;


    @Autowired
    String url;

    @Autowired
    String title;

    @Override
    public int getLayoutID() {
        return R.layout.activity_web_view;
    }

    @Override
    public void initView() {

        initWebView();

        if (!TextUtils.isEmpty(url)) {
            webview.loadUrl(url);
        }

//        webview.loadUrl("http://xiyifenheart.s1.natapp.cc/jsbradge/index.html");


    }

    @Override
    public void initTitle(ImageView ivBack, TextView tvBack, View llBack, TextView titleName, TextView tvMenu, View titleRoot) {
        if (!TextUtils.isEmpty(title)) {
            titleName.setText(title);
        }

        llBack.setOnClickListener(v -> onBackPressed());

       /* tvMenu.setVisibility(View.VISIBLE);
        tvMenu.setOnClickListener(v -> registJavaCallJsMethod());*/
    }

    private void initWebView() {
        webview.setDefaultHandler(new DefaultHandler());

        webview.setWebChromeClient(new WebChromeClient() {

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType, String capture) {
                this.openFileChooser(uploadMsg);
            }

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType) {
                this.openFileChooser(uploadMsg);
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg) {

            }
        });

        webview.setWebViewClient(new TWebViewClient(webview));

        registJsCallJavaMethod();
    }


    @Override
    protected WebViewPresenter createPresenter() {
        return new WebViewPresenter();
    }

    @Override
    public void  onBackPressed() {

        if (webview.canGoBack()) {
            webview.goBack();// 返回前一个页面

        } else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onDestroy() {
        if (webview != null) {
            webview.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webview.clearHistory();
            ((ViewGroup) webview.getParent()).removeView(webview);
            webview.destroy();
            webview = null;
        }
        super.onDestroy();

    }


    /**
     * 注册方法提供给JS调用  demo
     */
    public void registJsCallJavaMethod() {
        webview.registerHandler("nativeFunc", new BridgeHandler() {

            /**
             * Js调用submitFromWeb这个方法后 在handler中处理逻辑
             * @param data  Js带入的参数
             * @param function Java返回给Js的回调
             */
            @Override
            public void handler(String data, CallBackFunction function) {

                String str = "这是html带入给java的数据:" + data;

                ToastUtils.showShortToast(str);

                function.onCallBack("Java返回给js传递过来的数据="+data);
            }

        });
    }

    /**
     * Java调用Js方法  demo
     */
    public void registJavaCallJsMethod() {

        /**
         * functionInJs     协议的方法名
         * data             传递给js的数据
         * CallBackFunction Js返回的的回调
         */
        webview.callHandler("jsFunc", "data", new CallBackFunction() {

            /**
             * Js的回调
             * @param data Js的回调的数据
             */
            @Override
            public void onCallBack(String data) {

                ToastUtils.showShortToast("reponse data from js " + data);
            }

        });
    }


    @Override
    public void onLoadData(Object o) {

    }

    @Override
    public void onEmptyData() {

    }

    @Override
    public void onLoadError(String msg) {

    }

    @Override
    public void onNetError(String msg) {

    }
}
