package com.example.konta.sketch_loyalityapp.independentViews;

import android.os.Bundle;

import com.example.konta.sketch_loyalityapp.R;
import com.example.konta.sketch_loyalityapp.base.BaseActivity;

public class TermsConditionsActivity extends BaseActivity {

    @Override
    protected int getLayout() { return R.layout.activity_terms_conditions; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Terms & Conditions");
    }
}