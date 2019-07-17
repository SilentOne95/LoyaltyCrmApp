package com.jemsushi.loyaltyapp.base.activity;

import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.jemsushi.loyaltyapp.R;
import com.jemsushi.loyaltyapp.utils.CustomSnackbar;

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
    }

    protected abstract int getLayout();

    public abstract void initViews();

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
}
