package com.sellger.konta.sketch_loyaltyapp.ui.settings;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sellger.konta.sketch_loyaltyapp.R;
import com.sellger.konta.sketch_loyaltyapp.base.BaseActivity;
import com.sellger.konta.sketch_loyaltyapp.ui.main.MainActivity;

public class SettingsActivity extends BaseActivity implements SettingsContract.View, View.OnClickListener {

    private static final String TAG = SettingsActivity.class.getSimpleName();

    private TextView mTermsText, mPrivacyText, mLicensesText;
    private Button mDeleteButton;

    @Override
    protected int getLayout() {
        return R.layout.activity_settings;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Ustawienia");

        // Init views
        initViews();

        // Setting up views
        mTermsText.setOnClickListener(this);
        mPrivacyText.setOnClickListener(this);
        mLicensesText.setOnClickListener(this);
        mDeleteButton.setOnClickListener(this);
    }

    @Override
    public void initViews() {
        mTermsText = findViewById(R.id.settings_terms_one);
        mPrivacyText = findViewById(R.id.settings_terms_two);
        mLicensesText = findViewById(R.id.settings_terms_three);
        mDeleteButton = findViewById(R.id.settings_delete_button);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settings_delete_button:
                showDialogDeleteAccount();
                break;
            default:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                startActivity(browserIntent);
                break;
        }
    }

    @Override
    public void showDialogDeleteAccount(){
        new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.settings_alert_dialog_style))
                .setCancelable(true)
                .setTitle(R.string.settings_delete_account_alert_title)
                .setMessage(R.string.settings_delete_account_alert_message)
                .setPositiveButton(R.string.settings_delete_account_alert_confirm, (dialog, which) -> deleteUserAccount())
                .setNegativeButton(R.string.settings_delete_account_alert_decline, null)
                .show();
    }

    @Override
    public void deleteUserAccount() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Start MainActivity to show Login layout as current account was deleted
                SettingsActivity.this.startActivity(new Intent(SettingsActivity.this, MainActivity.class));
            }
        });
    }
}
