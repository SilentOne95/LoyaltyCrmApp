package com.example.konta.sketch_loyalityapp.ui.terms;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.konta.sketch_loyalityapp.R;
import com.example.konta.sketch_loyalityapp.base.BaseFragment;
import com.example.konta.sketch_loyalityapp.pojo.staticPage.Page;

public class TermsFragment extends BaseFragment implements TermsContract.View {

    private static final String TAG = TermsFragment.class.getSimpleName();

    TermsPresenter presenter;

    private ProgressBar mProgressBar;
    private View mLayoutContainer;
    private TextView mDescription;

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
        mDescription = rootView.findViewById(R.id.terms_conditions_text_view);


        presenter = new TermsPresenter(this, new TermsModel());
        presenter.requestDataFromServer(1);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem searchItem = menu.findItem(R.id.main_menu_search);
        searchItem.setVisible(false);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
        mLayoutContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void setUpViewWithData(Page page) {
        mDescription.setText(Html.fromHtml(page.getBody()));
    }
}