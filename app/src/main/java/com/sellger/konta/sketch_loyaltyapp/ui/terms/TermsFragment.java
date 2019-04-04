package com.sellger.konta.sketch_loyaltyapp.ui.terms;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.sellger.konta.sketch_loyaltyapp.R;
import com.sellger.konta.sketch_loyaltyapp.base.BaseFragment;
import com.sellger.konta.sketch_loyaltyapp.pojo.staticPage.Page;

public class TermsFragment extends BaseFragment implements TermsContract.View {

    private static final String TAG = TermsFragment.class.getSimpleName();

    TermsPresenter presenter;

    private ProgressBar mProgressBar;
    private View mLayoutContainer;
    private WebView mTermsTextView;

    @Override
    protected int getLayout() { return R.layout.fragment_terms_conditions; }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Regulamin");

        setHasOptionsMenu(true);

        mLayoutContainer = rootView.findViewById(R.id.layout_container);
        mLayoutContainer.setVisibility(View.GONE);
        mProgressBar = rootView.findViewById(R.id.progress_bar);
        mTermsTextView = rootView.findViewById(R.id.terms_webview);


        presenter = new TermsPresenter(this, new TermsModel());
        presenter.requestDataFromServer(1);
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
    public void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
        mLayoutContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void setUpViewWithData(Page page) {
        mTermsTextView.loadData(page.getBody(), "text/html", null);
    }
}