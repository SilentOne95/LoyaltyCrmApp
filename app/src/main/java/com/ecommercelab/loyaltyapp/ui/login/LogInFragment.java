package com.ecommercelab.loyaltyapp.ui.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.ecommercelab.loyaltyapp.R;
import com.ecommercelab.loyaltyapp.base.activity.BaseActivity;
import com.ecommercelab.loyaltyapp.base.fragment.BaseFragment;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

import static com.ecommercelab.loyaltyapp.Constants.ANONYMOUS_REGISTRATION;
import static com.ecommercelab.loyaltyapp.Constants.FACEBOOK_SIGN_IN;
import static com.ecommercelab.loyaltyapp.Constants.GOOGLE_SIGN_IN;
import static com.ecommercelab.loyaltyapp.Constants.LAYOUT_DATA_EMPTY_STRING;
import static com.ecommercelab.loyaltyapp.Constants.LAYOUT_TYPE_HOME;
import static com.ecommercelab.loyaltyapp.Constants.LAYOUT_TYPE_PHONE;
import static com.ecommercelab.loyaltyapp.Constants.NOT_ANONYMOUS_REGISTRATION;
import static com.ecommercelab.loyaltyapp.Constants.REGISTRATION_CONVERSION;
import static com.ecommercelab.loyaltyapp.Constants.REGISTRATION_NORMAL;
import static com.ecommercelab.loyaltyapp.Constants.TOAST_ACCOUNT_AUTH_FAILED;
import static com.ecommercelab.loyaltyapp.Constants.TOAST_ACCOUNT_EXISTS;
import static com.ecommercelab.loyaltyapp.Constants.TOAST_AUTH_EMAIL_ALREADY_EXISTS;
import static com.ecommercelab.loyaltyapp.Constants.TOAST_ERROR;
import static com.ecommercelab.loyaltyapp.Constants.TOAST_INTERNET_CONNECTION_REQUIRED;

public class LogInFragment extends BaseFragment implements LogInContract.View, View.OnClickListener {

    private static final String TAG = LogInFragment.class.getSimpleName();

    private LogInPresenter presenter;

    private FirebaseAuth mFirebaseAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager mCallbackFacebookManager;

    private Button mSignInWithGoogleButton, mSignInWithFacebookButton, mSignInWithPhoneButton;
    private TextView mSignInAnonymously;

    private boolean mIsAuthInProgress = false;

    @Override
    protected int getLayout() {
        return R.layout.fragment_log_in;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseAuth.useAppLanguage();

        // Hide action bar
        ((BaseActivity) getActivity()).getSupportActionBar().hide();

        // Init views
        initViews();

        // Setting up presenter
        presenter = new LogInPresenter(this);

        // Log In with Google
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), googleSignInOptions);

        // Log In with Facebook
        mCallbackFacebookManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackFacebookManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
            }
        });
    }

    /**
     * Called from {@link #onCreate(Bundle)} to init all the views.
     */
    @Override
    public void initViews() {
        mSignInWithGoogleButton = rootView.findViewById(R.id.login_google_button);
        mSignInWithFacebookButton = rootView.findViewById(R.id.login_facebook_button);
        mSignInWithPhoneButton = rootView.findViewById(R.id.login_phone_button);
        mSignInAnonymously = rootView.findViewById(R.id.register_guest_text);

        // Button listeners
        mSignInWithGoogleButton.setOnClickListener(this);
        mSignInWithFacebookButton.setOnClickListener(this);
        mSignInWithPhoneButton.setOnClickListener(this);
        mSignInAnonymously.setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param view which was clicked
     * @see <a href="https://developer.android.com/reference/android/view/View.OnClickListener">Android Dev Doc</a>
     */
    @Override
    public void onClick(View view) {
        if (checkInternetConnection()) {
            if (!mIsAuthInProgress) {
                switch (view.getId()) {
                    case R.id.login_google_button:
                        handleUserActionsWhileProcessing(mIsAuthInProgress = true);
                        googleSignIn();
                        break;
                    case R.id.login_facebook_button:
                        handleUserActionsWhileProcessing(mIsAuthInProgress = true);
                        facebookSignIn();
                        break;
                    case R.id.login_phone_button:
                        navigationPresenter.getSelectedLayoutType(LAYOUT_TYPE_PHONE, LAYOUT_DATA_EMPTY_STRING);
                        break;
                    case R.id.register_guest_text:
                        if (mFirebaseAuth.getCurrentUser() != null) {
                            displayToastMessage(TOAST_ACCOUNT_EXISTS);
                        } else {
                            handleUserActionsWhileProcessing(mIsAuthInProgress = true);
                            displayAnonymousSignInAlertDialog();
                        }
                        break;
                }
            }
        } else {
            displayToastMessage(TOAST_INTERNET_CONNECTION_REQUIRED);
        }
    }

    /**
     * Called from {@link #onClick(View)} to verify if internet connection is available.
     *
     * @return boolean depends on whether connection is available or not
     */
    private boolean checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    /**
     * Called from {@link #onClick(View)} to continue with Google account.
     *
     * @see <a href="https://firebase.google.com/docs/auth/android/google-signin">Firebase Doc</a>
     */
    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }

    /**
     * Called from {@link #onActivityResult(int, int, Intent)} if auth was successful to consider
     * whether account should be converted from anonymous or just created new one.
     *
     * @param account Google object
     */
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        if (mFirebaseAuth.getCurrentUser() != null) {
            convertAnonymousAccount(credential);
        } else {
            signInWithCredential(credential);
        }
    }

    /**
     * Called from {@link #onClick(View)} to continue with Facebook account.
     *
     * @see <a href="https://firebase.google.com/docs/auth/android/facebook-login">Firebase Doc</a>
     */
    private void facebookSignIn() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
    }

    /**
     * Called from {@link #onActivityResult(int, int, Intent)} if auth was successful to consider
     * whether account should be converted from anonymous or just created new one.
     *
     * @param token Facebook object
     */
    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        if (mFirebaseAuth.getCurrentUser() != null) {
            convertAnonymousAccount(credential);
        } else {
            signInWithCredential(credential);
        }
    }

    /**
     * Called from {@link #firebaseAuthWithGoogle(GoogleSignInAccount)} and {@link #firebaseAuthWithGoogle(GoogleSignInAccount)}
     * to sign user with Google or Facebook credentials and subscribe to push notification topics.
     *
     * @param credential retrieved from {@link #onActivityResult(int, int, Intent)}
     */
    private void signInWithCredential(AuthCredential credential) {
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        // Subscribe user to topics of push notifications
                        presenter.manageTopicsSubscriptions(REGISTRATION_NORMAL);

                        // Open "home" view, pass string to display / hide information about account in nav view header
                        navigationPresenter.getSelectedLayoutType(LAYOUT_TYPE_HOME, NOT_ANONYMOUS_REGISTRATION);
                        handleUserActionsWhileProcessing(mIsAuthInProgress = false);
                    } else {
                        // Check if user has already used this email with different auth method
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            displayToastMessage(TOAST_AUTH_EMAIL_ALREADY_EXISTS);
                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                // If account with this email already exists, display a message to the user
                                displayToastMessage(TOAST_AUTH_EMAIL_ALREADY_EXISTS);
                            } else {
                                // If sign in fails, display a message to the user
                                displayToastMessage(TOAST_ACCOUNT_AUTH_FAILED);
                            }
                            // Registration failed, enable user touch actions
                            handleUserActionsWhileProcessing(mIsAuthInProgress = false);
                        }
                    }
                });
    }

    /**
     * Retrieves the results for registration / login with Google / Facebook account.
     *
     * @param requestCode is an int of permission that was requested
     * @param resultCode  is either RESULT_OK if the operation was successful or RESULT_CANCELED
     *                    if the user backed out or the operation failed for some reason
     * @param data        carries the result data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != 0) {
            if (requestCode == GOOGLE_SIGN_IN) {
                // Google
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    firebaseAuthWithGoogle(account);
                } catch (ApiException e) {
                    // Google Sign In failed, update UI appropriately
                }
            } else if (requestCode == FACEBOOK_SIGN_IN) {
                // Facebook
                // Pass the activity result back to the Facebook SDK
                mCallbackFacebookManager.onActivityResult(requestCode, resultCode, data);
            }
        } else {
            // Registration aborted, enable user touch actions
            handleUserActionsWhileProcessing(mIsAuthInProgress = false);
        }
    }

    private void displayAnonymousSignInAlertDialog() {
        new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.settings_alert_dialog_accent_style))
                .setCancelable(true)
                .setTitle(R.string.login_alert_dialog_title)
                .setMessage(R.string.login_alert_dialog_message)
                .setPositiveButton(R.string.login_alert_dialog_confirm, (dialog, which) -> anonymousSignIn())
                .setNegativeButton(R.string.login_alert_dialog_decline, (dialog, which) -> handleUserActionsWhileProcessing(mIsAuthInProgress = false))
                .show();
    }

    /**
     * Called from {@link #onClick(View)} to continue with anonymous account.
     */
    private void anonymousSignIn() {
        mFirebaseAuth.signInAnonymously()
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        // Subscribe user to topics of push notifications
                        presenter.manageTopicsSubscriptions(ANONYMOUS_REGISTRATION);

                        // Open "home" view, pass string to display / hide information about account in nav view header
                        navigationPresenter.getSelectedLayoutType(LAYOUT_TYPE_HOME, ANONYMOUS_REGISTRATION);
                        handleUserActionsWhileProcessing(mIsAuthInProgress = false);
                    } else {
                        // If sign in fails, display a message to the user.
                        displayToastMessage(TOAST_ACCOUNT_AUTH_FAILED);
                        // Registration failed, enable user touch actions
                        handleUserActionsWhileProcessing(mIsAuthInProgress = false);
                    }
                });
    }

    /**
     * Called from {@link #firebaseAuthWithGoogle(GoogleSignInAccount)} and {@link #handleFacebookAccessToken(AccessToken)}
     * to convert anonymous account to Google / Facebook depends on the choice.
     *
     * @param credential that the Firebase Authentication server can use to authenticate a user
     */
    private void convertAnonymousAccount(AuthCredential credential) {
        mFirebaseAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        // Subscribe user to topics of push notifications
                        presenter.manageTopicsSubscriptions(REGISTRATION_CONVERSION);

                        // Open "home" view, pass string to display / hide information about account in nav view header
                        navigationPresenter.getSelectedLayoutType(LAYOUT_TYPE_HOME, NOT_ANONYMOUS_REGISTRATION);
                        handleUserActionsWhileProcessing(mIsAuthInProgress = false);
                    } else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            // If account with this email already exists, display a message to the user
                            displayToastMessage(TOAST_AUTH_EMAIL_ALREADY_EXISTS);
                        } else {
                            // If sign in fails, display a message to the user
                            displayToastMessage(TOAST_ACCOUNT_AUTH_FAILED);
                        }
                        // Registration failed, enable user touch actions
                        handleUserActionsWhileProcessing(mIsAuthInProgress = false);
                    }
                });
    }

    /**
     * Called whenever process of logging in / registration has started / ended up to enable / disable user's
     * touch actions to prevent from breaking off connection with firebase.
     */
    private void handleUserActionsWhileProcessing(boolean isActionProcessing) {
        if (isActionProcessing) {
            // Disable user actions while processing logging in / registration
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            // Enable user actions while processing logging in / registration
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    /**
     * Called from {@link #onClick(View)}, {@link #signInWithCredential(AuthCredential)}, {@link #anonymousSignIn()}
     * and {@link #convertAnonymousAccount(AuthCredential)} to display to user relevant string with toast message.
     *
     * @param message that contains info what kind of message should be displayed
     */
    private void displayToastMessage(String message) {
        switch (message) {
            case TOAST_ACCOUNT_EXISTS:
                message = getString(R.string.account_already_exits);
                break;
            case TOAST_ERROR:
                message = getString(R.string.default_toast_error_message);
                break;
            case TOAST_INTERNET_CONNECTION_REQUIRED:
                message = getString(R.string.internet_connection_required);
                break;
            case TOAST_ACCOUNT_AUTH_FAILED:
                message = getString(R.string.account_auth_failed);
                break;
            case TOAST_AUTH_EMAIL_ALREADY_EXISTS:
                message = getString(R.string.auth_email_already_exists);
                break;
        }

        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }
}
