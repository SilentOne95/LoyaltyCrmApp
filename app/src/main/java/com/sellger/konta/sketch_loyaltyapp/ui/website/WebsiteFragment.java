package com.sellger.konta.sketch_loyaltyapp.ui.website;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.sellger.konta.sketch_loyaltyapp.R;
import com.sellger.konta.sketch_loyaltyapp.base.BaseFragment;
import com.sellger.konta.sketch_loyaltyapp.data.Injection;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Page;

public class WebsiteFragment extends BaseFragment implements WebsiteContract.View {

    private static final String TAG = WebsiteFragment.class.getSimpleName();

    WebsitePresenter presenter;

    private WebView mWebView;

    @Override
    protected int getLayout() { return R.layout.fragment_website; }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Nasza strona");

        setHasOptionsMenu(true);

        // Init views
        initViews();

        presenter = new WebsitePresenter(this, Injection.provideLoyaltyRepository(getContext()));
        presenter.requestDataFromServer(2);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem searchItem = menu.findItem(R.id.main_menu_search);
        MenuItem optionsItem = menu.findItem(R.id.main_menu_options);
        searchItem.setVisible(false);
        optionsItem.setVisible(false);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void initViews() {
        mWebView = rootView.findViewById(R.id.webview);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void setUpViewWithData(Page page) {
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(page.getBody());
    }

    @Override
    public void displayToastMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }
}