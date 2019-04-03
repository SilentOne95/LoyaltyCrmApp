package com.sellger.konta.sketch_loyaltyapp.ui.settings;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sellger.konta.sketch_loyaltyapp.R;
import com.sellger.konta.sketch_loyaltyapp.base.BaseActivity;
import com.sellger.konta.sketch_loyaltyapp.ui.main.MainActivity;

import java.util.concurrent.atomic.AtomicBoolean;

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
                showDialogDeleteAccount();
                break;
            default:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                startActivity(browserIntent);
                break;
        }
    }

    private void showDialogDeleteAccount(){
        AtomicBoolean result = new AtomicBoolean(false);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true)
                .setTitle("Usunąć konto?")
                .setMessage(R.string.settings_delete_account_alert_message)
                .setPositiveButton(R.string.settings_delete_account_alert_confirm, (dialog, which) -> result.set(true))
                .setNegativeButton(R.string.settings_delete_account_alert_decline, null);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Start MainActivity to show Login layout as current account was deleted
                SettingsActivity.this.startActivity(new Intent(SettingsActivity.this, MainActivity.class));
            }
        });
    }
}
