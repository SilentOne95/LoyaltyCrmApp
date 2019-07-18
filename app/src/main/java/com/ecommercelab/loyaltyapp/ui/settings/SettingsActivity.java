package com.ecommercelab.loyaltyapp.ui.settings;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.WindowManager;
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
import com.ecommercelab.loyaltyapp.R;
import com.ecommercelab.loyaltyapp.base.activity.BaseActivity;
import com.ecommercelab.loyaltyapp.ui.main.MainActivity;

import java.util.List;

import static com.ecommercelab.loyaltyapp.Constants.FIRST_TOPIC_NAME;
import static com.ecommercelab.loyaltyapp.Constants.SECOND_TOPIC_NAME;
import static com.ecommercelab.loyaltyapp.Constants.SHARED_PREF_FILE_NAME;
import static com.ecommercelab.loyaltyapp.Constants.THIRD_TOPIC_NAME;
import static com.ecommercelab.loyaltyapp.Constants.TOAST_DELETE_SUCCESS;
import static com.ecommercelab.loyaltyapp.Constants.TOAST_ERROR;
import static com.ecommercelab.loyaltyapp.Constants.TOAST_INTERNET_CONNECTION_REQUIRED;
import static com.ecommercelab.loyaltyapp.Constants.TOAST_LOG_OUT_SUCCESS;

public class SettingsActivity extends BaseActivity implements SettingsContract.View, View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {

    private static final String TAG = SettingsActivity.class.getSimpleName();

    private SettingsPresenter presenter;
    private SharedPreferences preferences;

    private TextView mTermsText, mPrivacyText, mLicensesText;
    private Switch mSwitchFirstTopic, mSwitchSecondTopic, mSwitchThirdTopic;
    private Button mLogOutButton, mDeleteButton;

    private boolean mIsActionProcessing = false;

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
    }

    /**
     * Called from {@link #onCreate(Bundle)} to init all the views.
     */
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

    /**
     * Called when a view has been clicked.
     *
     * @param view which was clicked
     * @see <a href="https://developer.android.com/reference/android/view/View.OnClickListener">Android Dev Doc</a>
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.settings_log_out_button:
                showLogOutDialog();
                break;
            case R.id.settings_delete_button:
                showDeleteAccountDialog();
                break;
            default:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                startActivity(browserIntent);
                break;
        }
    }

    /**
     * Interface definition for a callback to be invoked when the checked state of a compound button changed.
     *
     * @param buttonView which was clicked
     * @param isChecked  is boolean value depends on whether view is checked or not
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.settings_notification_switch_one:
                if (presenter.isNetworkAvailable(this)) {
                    presenter.saveSwitchState(preferences, FIRST_TOPIC_NAME, isChecked);
                } else {
                    mSwitchFirstTopic.setChecked(presenter.getSwitchState(preferences, FIRST_TOPIC_NAME));
                    displayToastMessage(TOAST_INTERNET_CONNECTION_REQUIRED);
                }
                break;
            case R.id.settings_notification_switch_two:
                if (presenter.isNetworkAvailable(this)) {
                    presenter.saveSwitchState(preferences, SECOND_TOPIC_NAME, isChecked);
                } else {
                    mSwitchSecondTopic.setChecked(presenter.getSwitchState(preferences, SECOND_TOPIC_NAME));
                    displayToastMessage(TOAST_INTERNET_CONNECTION_REQUIRED);
                }
                break;
            case R.id.settings_notification_switch_three:
                if (presenter.isNetworkAvailable(this)) {
                    presenter.saveSwitchState(preferences, THIRD_TOPIC_NAME, isChecked);
                } else {
                    mSwitchThirdTopic.setChecked(presenter.getSwitchState(preferences, THIRD_TOPIC_NAME));
                    displayToastMessage(TOAST_INTERNET_CONNECTION_REQUIRED);
                }
                break;
        }
    }

    /**
     * Called from {@link #onClick(View)} to show AlertDialog to user to confirm if want to log out.
     */
    private void showLogOutDialog() {
        new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.settings_alert_dialog_accent_style))
                .setCancelable(true)
                .setTitle(R.string.settings_log_out_alert_title)
                .setMessage(R.string.settings_log_out_account_alert_message)
                .setPositiveButton(R.string.settings_log_out_account_alert_confirm, (dialog, which) -> {
                    handleUserActionsWhileProcessing(mIsActionProcessing = true);
                    logOutAccount();
                })
                .setNegativeButton(R.string.settings_log_out_account_alert_decline, null)
                .show();
    }

    /**
     * Called from {@link #onClick(View)} to show AlertDialog to user to confirm if want to delete account.
     */
    private void showDeleteAccountDialog() {
        new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.settings_alert_dialog_delete_style))
                .setCancelable(true)
                .setTitle(R.string.settings_delete_account_alert_title)
                .setMessage(R.string.settings_delete_account_alert_message)
                .setPositiveButton(R.string.settings_delete_account_alert_confirm, (dialog, which) -> {
                    handleUserActionsWhileProcessing(mIsActionProcessing = true);
                    deleteUserAccount();
                })
                .setNegativeButton(R.string.settings_delete_account_alert_decline, null)
                .show();
    }

    /**
     * Called from {@link #showLogOutDialog()} to log out and switch layout.
     */
    private void logOutAccount() {
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

    /**
     * Called from {@link #logOutAccount()} to check authentication option of current account.
     *
     * @return string that contains auth option
     */
    private String checkAuthMethod() {
        String authMethod = "";
        FirebaseAuth.getInstance().getCurrentUser().getProviderData();
        List<? extends UserInfo> providers = FirebaseAuth.getInstance().getCurrentUser().getProviderData();
        for (UserInfo userInfo : providers) {
            authMethod = userInfo.getProviderId();
        }

        return authMethod;
    }

    /**
     * Called from {@link #showDeleteAccountDialog()} to log out and switch layout.
     */
    private void deleteUserAccount() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                displayToastMessage(TOAST_DELETE_SUCCESS);
                unsubscribeAndUpdateUI();
            }
        }).addOnFailureListener(task -> {
            handleUserActionsWhileProcessing(mIsActionProcessing = false);
            displayToastMessage(TOAST_ERROR);
        });
    }

    /**
     * Called from {@link #deleteUserAccount()} to unsubscribe from every push notification topic and
     * switch layout.
     */
    private void unsubscribeAndUpdateUI() {
        // Remove topics subscriptions
        String[] topicsList = new String[]{FIRST_TOPIC_NAME, SECOND_TOPIC_NAME, THIRD_TOPIC_NAME};
        for (String topic : topicsList) {
            presenter.unsubscribeFromTopic(topic);
        }

        // Start MainActivity to show Login layout as current account was deleted
        SettingsActivity.this.startActivity(new Intent(SettingsActivity.this, MainActivity.class));
    }

    /**
     * Called from {@link #showLogOutDialog()}, {@link #showDeleteAccountDialog()} and
     * {@link #deleteUserAccount()} to enable / disable user touch actions as log out / delete account
     * action process is running / failed.
     */
    private void handleUserActionsWhileProcessing(boolean isActionProcessing) {
        if (isActionProcessing) {
            // Disable user actions while processing log out or delete action
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            // Enable user actions while processing log out or delete action failed
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    @Override
    public void onBackPressed() {
        if (!mIsActionProcessing) {
            super.onBackPressed();
        }
    }

    /**
     * Called from {@link #onCheckedChanged(CompoundButton, boolean)}, {@link #logOutAccount()} and
     * {@link #deleteUserAccount()} to display relevant information to user with toast message.
     *
     * @param message is a string with type of toast that should be displayed
     */
    private void displayToastMessage(String message) {
        switch (message) {
            case TOAST_LOG_OUT_SUCCESS:
                message = getString(R.string.settings_acc_log_out_successfully);
                break;
            case TOAST_DELETE_SUCCESS:
                message = getString(R.string.settings_acc_delete_successfully);
                break;
            case TOAST_INTERNET_CONNECTION_REQUIRED:
                message = getString(R.string.internet_connection_required);
                break;
            case TOAST_ERROR:
                message = getString(R.string.default_toast_error_message);
                break;
        }

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
