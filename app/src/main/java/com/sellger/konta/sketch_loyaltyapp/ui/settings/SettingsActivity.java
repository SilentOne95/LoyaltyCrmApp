package com.sellger.konta.sketch_loyaltyapp.ui.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sellger.konta.sketch_loyaltyapp.R;
import com.sellger.konta.sketch_loyaltyapp.base.BaseActivity;

public class SettingsActivity extends BaseActivity implements SettingsContract.View, View.OnClickListener {

    private static final String TAG = SettingsActivity.class.getSimpleName();

    private TextView termsText, privacyText, licensesText;
    private Button deleteButton;

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
        deleteButton = findViewById(R.id.settings_delete_button);

        termsText.setOnClickListener(this);
        privacyText.setOnClickListener(this);
        licensesText.setOnClickListener(this);
        deleteButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settings_delete_button:
                DialogFragment newDialog = new DeleteAccountDialogFragment();
                newDialog.show(getSupportFragmentManager(), "delete");
                break;
            default:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                startActivity(browserIntent);
                break;
        }
    }
}
