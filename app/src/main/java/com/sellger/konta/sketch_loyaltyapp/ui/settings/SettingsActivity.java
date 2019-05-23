package com.sellger.konta.sketch_loyaltyapp.ui.settings;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.sellger.konta.sketch_loyaltyapp.R;
import com.sellger.konta.sketch_loyaltyapp.base.BaseActivity;
import com.sellger.konta.sketch_loyaltyapp.ui.main.MainActivity;

import java.util.List;

import static com.sellger.konta.sketch_loyaltyapp.Constants.FIRST_TOPIC_NAME;
import static com.sellger.konta.sketch_loyaltyapp.Constants.SECOND_TOPIC_NAME;
import static com.sellger.konta.sketch_loyaltyapp.Constants.SHARED_PREF_FILE_NAME;
import static com.sellger.konta.sketch_loyaltyapp.Constants.THIRD_TOPIC_NAME;
import static com.sellger.konta.sketch_loyaltyapp.Constants.TOAST_DELETE_SUCCESS;
import static com.sellger.konta.sketch_loyaltyapp.Constants.TOAST_LOG_OUT_SUCCESS;

public class SettingsActivity extends BaseActivity implements SettingsContract.View, View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {

    private static final String TAG = SettingsActivity.class.getSimpleName();

    private SettingsPresenter presenter;

    private TextView mTermsText, mPrivacyText, mLicensesText;
    private Switch mSwitchFirstTopic, mSwitchSecondTopic, mSwitchThirdTopic;
    private Button mLogOutButton, mDeleteButton;

    private SharedPreferences preferences;

    @Override
    protected int getLayout() {
        return R.layout.activity_settings;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Ustawienia");

        // Setting up presenter
        presenter = new SettingsPresenter(this);

        // Init SharedPrefs for saving switch states
        preferences = getSharedPreferences(SHARED_PREF_FILE_NAME, MODE_PRIVATE);

        // Init views
        initViews();

        // Setting up views
        mTermsText.setOnClickListener(this);
        mPrivacyText.setOnClickListener(this);
        mLicensesText.setOnClickListener(this);

        mSwitchFirstTopic.setChecked(presenter.getSwitchState(preferences, FIRST_TOPIC_NAME));
        mSwitchSecondTopic.setChecked(presenter.getSwitchState(preferences, SECOND_TOPIC_NAME));
        mSwitchThirdTopic.setChecked(presenter.getSwitchState(preferences, THIRD_TOPIC_NAME));

        mSwitchFirstTopic.setOnCheckedChangeListener(this);
        mSwitchSecondTopic.setOnCheckedChangeListener(this);
        mSwitchThirdTopic.setOnCheckedChangeListener(this);

        mLogOutButton.setOnClickListener(this);
        mDeleteButton.setOnClickListener(this);
    }

    @Override
    public void initViews() {
        mTermsText = findViewById(R.id.settings_terms_one);
        mPrivacyText = findViewById(R.id.settings_terms_two);
        mLicensesText = findViewById(R.id.settings_terms_three);
        mSwitchFirstTopic = findViewById(R.id.settings_notification_switch_one);
        mSwitchSecondTopic = findViewById(R.id.settings_notification_switch_two);
        mSwitchThirdTopic = findViewById(R.id.settings_notification_switch_three);
        mLogOutButton = findViewById(R.id.settings_log_out_button);
        mDeleteButton = findViewById(R.id.settings_delete_button);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settings_log_out_button:
                showDialogLogOut();
                break;
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
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.settings_notification_switch_one:
                presenter.saveSwitchState(preferences, FIRST_TOPIC_NAME, isChecked);
                break;
            case R.id.settings_notification_switch_two:
                presenter.saveSwitchState(preferences, SECOND_TOPIC_NAME, isChecked);
                break;
            case R.id.settings_notification_switch_three:
                presenter.saveSwitchState(preferences, THIRD_TOPIC_NAME, isChecked);
                break;
        }
    }

    @Override
    public void showDialogLogOut() {
        new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.settings_alert_dialog_log_out_style))
                .setCancelable(true)
                .setTitle(R.string.settings_log_out_alert_title)
                .setMessage(R.string.settings_log_out_account_alert_message)
                .setPositiveButton(R.string.settings_log_out_account_alert_confirm, (dialog, which) -> logOutAccount())
                .setNegativeButton(R.string.settings_log_out_account_alert_decline, null)
                .show();
    }

    @Override
    public void showDialogDeleteAccount(){
        new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.settings_alert_dialog_delete_style))
                .setCancelable(true)
                .setTitle(R.string.settings_delete_account_alert_title)
                .setMessage(R.string.settings_delete_account_alert_message)
                .setPositiveButton(R.string.settings_delete_account_alert_confirm, (dialog, which) -> deleteUserAccount())
                .setNegativeButton(R.string.settings_delete_account_alert_decline, null)
                .show();
    }

    @Override
    public void logOutAccount() {
        switch (checkAuthMethod()) {
            case "google.com":
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .build();
                GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
                mGoogleSignInClient.signOut();
                break;
            case "facebook.com":
                LoginManager.getInstance().logOut();
                break;
        }

        FirebaseAuth.getInstance().signOut();
        displayToastMessage(TOAST_LOG_OUT_SUCCESS);

        unsubscribeAndUpdateUI();
    }

    @Override
    public String checkAuthMethod() {
        String authMethod = "";
        FirebaseAuth.getInstance().getCurrentUser().getProviderData();
        List<? extends UserInfo> providers = FirebaseAuth.getInstance().getCurrentUser().getProviderData();
        for (UserInfo userInfo : providers) {
            Log.d(TAG, "provider: " + userInfo.getProviderId());
            authMethod = userInfo.getProviderId();
        }

        return authMethod;
    }

    @Override
    public void deleteUserAccount() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                displayToastMessage(TOAST_DELETE_SUCCESS);
                unsubscribeAndUpdateUI();
            }
        });
    }

    @Override
    public void unsubscribeAndUpdateUI() {
        // Remove topics subscriptions
        String[] topicsList = new String[] {FIRST_TOPIC_NAME, SECOND_TOPIC_NAME, THIRD_TOPIC_NAME};
        for (String topic : topicsList) {
            presenter.unsubscribeFromTopic(topic);
        }

        // Start MainActivity to show Login layout as current account was deleted
        SettingsActivity.this.startActivity(new Intent(SettingsActivity.this, MainActivity.class));
    }

    @Override
    public void displayToastMessage(String message) {
        if (message.equals(TOAST_LOG_OUT_SUCCESS)) {
            message = getString(R.string.settings_acc_log_out_successfully);
        } else if (message.equals(TOAST_DELETE_SUCCESS)) {
            message = getString(R.string.settings_acc_delete_successfully);
        }

        Toast.makeText(this, message , Toast.LENGTH_LONG).show();
    }
}
