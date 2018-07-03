package com.bsw.mydemo.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bsw.mydemo.R;
import com.bsw.mydemo.Utils.Logger;
import com.bsw.mydemo.base.BaseActivity;

public class WebViewActivity extends BaseActivity {

    public static final int QUESTION_DETAIL = 0x042;
    public static final int PRIVACY_POLICIES = 0x043;
    public static final int VIDEO_LINKAGE = 0x044;

    /**
     * 展示问题详情的WebView
     */
    private WebView mWebView;

    /**
     * 当前Activity
     */
    private WebViewActivity webViewActivity;

    private int showType;
    private String deviceId;

    private String url="http://192.168.36.35:8020/MapTest/video_play_m3u8.html?videoUrl=http://58.87.77.103:10088/hls/1234567890-1/1234567890-1_live.m3u8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switch (showType) {
            case VIDEO_LINKAGE:
                mWebView.loadUrl(url);
                break;
        }
        webViewActivity = this;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web_view;
    }

    @Override
    protected void findViews() {
        mWebView = getView(R.id.web_view);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void formatViews() {
        mWebView.setWebChromeClient(new WebChromeClient() {
        });
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
                    //表示返回键监听，可以返回则执行返回，不然关闭当前Activity
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                        return true;
                    } else {
                        finish();
                    }
                }
                return false;
            }
        });
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setAllowFileAccess(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDatabasePath(this.getCacheDir().getAbsolutePath());
        /*if (Build.VERSION.SDK_INT >= 19) {
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        } else {
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        }*/
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDomStorageEnabled(true);// 允许DCOM
        webSettings.setDatabaseEnabled(true);
        webSettings.setAppCacheMaxSize(1024 * 1024 * 8);
        String dir = getApplicationContext().getDir("database",
                Context.MODE_PRIVATE).getPath();
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setBlockNetworkImage(false);
        // 启用地理定位
        webSettings.setGeolocationEnabled(true);
        // 设置定位的数据库路径
        webSettings.setGeolocationDatabasePath(dir);
        mWebView.requestFocus();
        if (! TextUtils.isEmpty(url)) {
            Logger.i(getName(),"WebView load url = "  + url);
            mWebView.loadUrl(url);
        }
    }

    @Override
    protected void formatData() {

    }

    @Override
    protected void getBundle(Bundle bundle) {
        if (bundle != null) {
            url = bundle.getString("url");
            showType = bundle.getInt("showType");
            deviceId = bundle.getString("deviceId");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }

    /**
     * 当网页开始加载的时候出现加载提示框，加载结束则关闭
     */
    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }
}
