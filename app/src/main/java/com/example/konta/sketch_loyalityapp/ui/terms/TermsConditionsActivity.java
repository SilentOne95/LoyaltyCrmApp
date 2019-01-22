package com.example.konta.sketch_loyalityapp.ui.terms;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;

import com.example.konta.sketch_loyalityapp.R;
import com.example.konta.sketch_loyalityapp.base.BaseActivity;
import com.example.konta.sketch_loyalityapp.pojo.staticPage.Page;

public class TermsConditionsActivity extends BaseActivity implements TermsContract.View {

    TermsPresenter presenter;

    @Override
    protected int getLayout() { return R.layout.activity_terms_conditions; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Terms & Conditions");

        presenter = new TermsPresenter(this, new TermsModel());
        presenter.requestDataFromServer(6);
    }

    @Override
    public void setUpViewWithData(Page page) {
        TextView description = findViewById(R.id.terms_conditions_text_view);
        description.setText(Html.fromHtml(page.getBody()));
    }

    @Override
    public void onResponseFailure() {
        Toast.makeText(TermsConditionsActivity.this, "Oops.. something went wrong!", Toast.LENGTH_LONG).show();
    }
}