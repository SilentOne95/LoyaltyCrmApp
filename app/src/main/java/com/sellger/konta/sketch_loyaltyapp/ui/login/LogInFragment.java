package com.sellger.konta.sketch_loyaltyapp.ui.login;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sellger.konta.sketch_loyaltyapp.R;
import com.sellger.konta.sketch_loyaltyapp.base.activity.BaseActivity;
import com.sellger.konta.sketch_loyaltyapp.base.fragment.BaseFragment;
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

import static com.sellger.konta.sketch_loyaltyapp.Constants.ANONYMOUS_REGISTRATION;
import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_DATA_EMPTY_STRING;
import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_TYPE_HOME;
import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_TYPE_PHONE;
import static com.sellger.konta.sketch_loyaltyapp.Constants.NOT_ANONYMOUS_REGISTRATION;
import static com.sellger.konta.sketch_loyaltyapp.Constants.RC_SIGN_IN;
import static com.sellger.konta.sketch_loyaltyapp.Constants.REGISTRATION_CONVERSION;
import static com.sellger.konta.sketch_loyaltyapp.Constants.REGISTRATION_NORMAL;
import static com.sellger.konta.sketch_loyaltyapp.Constants.TOAST_ACCOUNT_AUTH_FAILED;
import static com.sellger.konta.sketch_loyaltyapp.Constants.TOAST_ACCOUNT_EXISTS;
import static com.sellger.konta.sketch_loyaltyapp.Constants.TOAST_ERROR;
import static com.sellger.konta.sketch_loyaltyapp.Constants.TOAST_INTERNET_CONNECTION_REQUIRED;

public class LogInFragment extends BaseFragment implements LogInContract.View, View.OnClickListener {

    private static final String TAG = LogInFragment.class.getSimpleName();

    private LogInPresenter presenter;

    private FirebaseAuth mFirebaseAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager mCallbackFacebookManager;

    private Button mSignInWithGoogleButton, mSignInWithFacebookButton, mSignInWithPhoneButton;
    private TextView mSignInAnonymously;

    @Override
    protected int getLayout() { return R.layout.fragment_log_in; }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseAuth.useAppLanguage();

        // Hide action bar
        ((BaseActivity) getActivity()).getSupportActionBar().hide();

        // Init views
        initViews();

        // Button listeners
        mSignInWithGoogleButton.setOnClickListener(this);
        mSignInWithFacebookButton.setOnClickListener(this);
        mSignInWithPhoneButton.setOnClickListener(this);
        mSignInAnonymously.setOnClickListener(this);

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
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v is view which was clicked
     */
    @Override
    public void onClick(View v) {

        if (checkInternetConnection()) {
            switch (v.getId()) {
                case R.id.login_google_button:
                    googleSignIn();
                    break;
                case R.id.login_facebook_button:
                    facebookSignIn();
                    break;
                case R.id.login_phone_button:
                    navigationPresenter.getSelectedLayoutType(LAYOUT_TYPE_PHONE, LAYOUT_DATA_EMPTY_STRING);
                    break;
                case R.id.register_guest_text:
                    if (mFirebaseAuth.getCurrentUser() != null) {
                        displayToastMessage(TOAST_ACCOUNT_EXISTS);
                    } else {
                        anonymousSignIn();
                    }
                    break;
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
     */
    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
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
     * sign user with Google or Facebook credentials and subscribe to push notification topics.
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
                    } else {
                        // If sign in fails, display a message to the user
                        displayToastMessage(TOAST_ACCOUNT_AUTH_FAILED);
                    }
                });
    }

    /**
     * Retrieves the results for registration / login with Google / Facebook account.
     *
     * @param requestCode is an int of permission that was requested
     * @param resultCode is either RESULT_OK if the operation was successful or RESULT_CANCELED
     *                   if the user backed out or the operation failed for some reason
     * @param data carries the result data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Google
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
            }
        }

        // Facebook
        // Pass the activity result back to the Facebook SDK
        mCallbackFacebookManager.onActivityResult(requestCode, resultCode, data);
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
                    } else {
                        // If sign in fails, display a message to the user.
                        displayToastMessage(TOAST_ACCOUNT_AUTH_FAILED);
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
                    } else {
                        displayToastMessage(TOAST_ACCOUNT_AUTH_FAILED);
                    }
                });
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
        }

        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }
}
