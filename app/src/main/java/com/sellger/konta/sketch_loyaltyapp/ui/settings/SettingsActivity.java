package com.sellger.konta.sketch_loyaltyapp.ui.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sellger.konta.sketch_loyaltyapp.R;
import com.sellger.konta.sketch_loyaltyapp.base.BaseActivity;

public class SettingsActivity extends BaseActivity implements SettingsContract.View, View.OnClickListener {

    private static final String TAG = SettingsActivity.class.getSimpleName();

    private TextView termsText, privacyText, licensesText;

    @Override
    protected int getLayout() {
        return R.layout.activity_settings;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Ustawienia");

        termsText = findViewById(R.id.settings_terms_one);
        privacyText = findViewById(R.id.settings_terms_two);
        licensesText = findViewById(R.id.settings_terms_three);

        termsText.setOnClickListener(this);
        privacyText.setOnClickListener(this);
        licensesText.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
        startActivity(browserIntent);
    }
}
