package com.sellger.konta.sketch_loyaltyapp.base;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.sellger.konta.sketch_loyaltyapp.R;
import com.sellger.konta.sketch_loyaltyapp.service.network.NetworkSchedulerService;
import com.sellger.konta.sketch_loyaltyapp.utils.CustomSnackbar;

public abstract class BaseActivity extends AppCompatActivity implements BaseActivityContract.View {

    private BaseActivityPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());

        presenter = new BaseActivityPresenter(this);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            presenter.scheduleNetworkJob(this);
        }

        presenter.startNetworkIntentService(getApplicationContext());
    }

    protected abstract int getLayout();

    @Override
    public void displaySnackbar(boolean isNetwork) {
        if (isNetwork) {
            CustomSnackbar customSnackbar = CustomSnackbar.make(findViewById(android.R.id.content), CustomSnackbar.LENGTH_LONG);
            customSnackbar.getView().setPadding(0,0,0,0);
            TextView snackbarTextView = customSnackbar.getView().findViewById(R.id.snackbar_text);
            customSnackbar.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.colorNetworkSnackbarAvailable));
            snackbarTextView.setText(R.string.snackbar_connection_restored);
            customSnackbar.show();
        } else {
            CustomSnackbar customSnackbar = CustomSnackbar.make(findViewById(android.R.id.content), CustomSnackbar.LENGTH_INDEFINITE);
            customSnackbar.getView().setPadding(0,0,0,0);
            TextView snackbarTextView = customSnackbar.getView().findViewById(R.id.snackbar_text);
            customSnackbar.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.colorNetworkSnackbarNotAvailable));
            snackbarTextView.setText(R.string.snackbar_no_network_connection);
            customSnackbar.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.startNetworkIntentService(this);
    }

    @Override
    protected void onPause() {
        stopService(new Intent(this, NetworkSchedulerService.class));
        super.onPause();
    }
}
