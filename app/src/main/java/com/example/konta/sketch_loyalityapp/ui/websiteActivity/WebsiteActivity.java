package com.example.konta.sketch_loyalityapp.ui.websiteActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.konta.sketch_loyalityapp.R;
import com.example.konta.sketch_loyalityapp.base.BaseActivity;

public class WebsiteActivity extends BaseActivity {

    @Override
    protected int getLayout() { return R.layout.activity_website; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Website");

        WebView mWebView = findViewById(R.id.webview);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl("https://www.google.com");
    }
}