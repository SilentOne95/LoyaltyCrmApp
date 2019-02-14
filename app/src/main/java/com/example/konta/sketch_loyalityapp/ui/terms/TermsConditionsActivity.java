package com.example.konta.sketch_loyalityapp.ui.terms;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.konta.sketch_loyalityapp.R;
import com.example.konta.sketch_loyalityapp.base.BaseActivity;
import com.example.konta.sketch_loyalityapp.pojo.staticPage.Page;

public class TermsConditionsActivity extends BaseActivity implements TermsContract.View {

    private static final String TAG = TermsConditionsActivity.class.getSimpleName();

    TermsPresenter presenter;

    private ProgressBar progressBar;
    private View layoutContainer;

    @Override
    protected int getLayout() { return R.layout.activity_terms_conditions; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Terms & Conditions");

        layoutContainer = findViewById(R.id.layout_container);
        layoutContainer.setVisibility(View.GONE);
        progressBar = findViewById(R.id.progress_bar);

        presenter = new TermsPresenter(this, new TermsModel());
        presenter.requestDataFromServer(1);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
        layoutContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void setUpViewWithData(Page page) {
        TextView description = findViewById(R.id.terms_conditions_text_view);
        description.setText(Html.fromHtml(page.getBody()));
    }
}