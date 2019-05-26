package com.sellger.konta.sketch_loyaltyapp.ui.terms;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sellger.konta.sketch_loyaltyapp.R;
import com.sellger.konta.sketch_loyaltyapp.base.BaseFragment;
import com.sellger.konta.sketch_loyaltyapp.data.Injection;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Page;

import static com.sellger.konta.sketch_loyaltyapp.Constants.BUNDLE_TITLE_STRING;
import static com.sellger.konta.sketch_loyaltyapp.Constants.TOAST_ERROR;

public class TermsFragment extends BaseFragment implements TermsContract.View {

    private static final String TAG = TermsFragment.class.getSimpleName();

    private TermsPresenter presenter;

    private ProgressBar mProgressBar;
    private View mLayoutContainer;
    private WebView mTermsTextView;

    @Override
    protected int getLayout() { return R.layout.fragment_terms_conditions; }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Extract additional data, which is fragment's title
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            getActivity().setTitle(bundle.getString(BUNDLE_TITLE_STRING));
        } else {
            getActivity().setTitle("Regulamin");
        }

        setHasOptionsMenu(true);

        // Init views
        initViews();

        // Setting up views
        mLayoutContainer.setVisibility(View.GONE);

        // Setting up presenter
        presenter = new TermsPresenter(this, Injection.provideLoyaltyRepository(getContext()));
        presenter.requestDataFromServer(1);
    }

    @Override
    public void initViews() {
        mLayoutContainer = rootView.findViewById(R.id.layout_container);
        mProgressBar = rootView.findViewById(R.id.progress_bar);
        mTermsTextView = rootView.findViewById(R.id.terms_webview);
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

    @Override
    public void displayToastMessage(String message) {
        if (message.equals(TOAST_ERROR)) {
            message = getString(R.string.default_toast_error_message);
        }

        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }
}