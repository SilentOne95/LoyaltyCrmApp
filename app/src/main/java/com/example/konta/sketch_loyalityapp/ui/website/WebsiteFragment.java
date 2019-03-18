package com.example.konta.sketch_loyalityapp.ui.website;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.konta.sketch_loyalityapp.R;
import com.example.konta.sketch_loyalityapp.base.BaseFragment;

public class WebsiteFragment extends BaseFragment {

    private static final String TAG = WebsiteFragment.class.getSimpleName();

    private WebView mWebView;

    @Override
    protected int getLayout() { return R.layout.fragment_website; }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Nasza strona");

        mWebView = rootView.findViewById(R.id.webview);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("https://www.google.com/");
    }
}