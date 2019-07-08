package com.sellger.konta.sketch_loyaltyapp.ui.launch;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.ContextThemeWrapper;

import com.sellger.konta.sketch_loyaltyapp.R;
import com.sellger.konta.sketch_loyaltyapp.ui.main.MainActivity;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (checkInternetConnection()) {
            startActivity(new Intent(LaunchActivity.this, MainActivity.class));
            finish();
        } else {
            displayNoInternetAlertDialog();
        }
    }

    private boolean checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    private void displayNoInternetAlertDialog() {
        new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.settings_alert_dialog_accent_style))
                .setCancelable(true)
                .setMessage(R.string.launcher_alert_dialog_message)
                .setPositiveButton(R.string.launcher_alert_dialog_confirm, ((dialog, which) -> finish()))
                .show();
    }
}