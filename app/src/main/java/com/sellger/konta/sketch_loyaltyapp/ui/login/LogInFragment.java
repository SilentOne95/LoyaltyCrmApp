package com.sellger.konta.sketch_loyaltyapp.ui.login;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sellger.konta.sketch_loyaltyapp.R;
import com.sellger.konta.sketch_loyaltyapp.base.BaseActivity;
import com.sellger.konta.sketch_loyaltyapp.base.BaseFragment;
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
import static com.sellger.konta.sketch_loyaltyapp.Constants.ANONYMOUS_TOPIC_NAME;
import static com.sellger.konta.sketch_loyaltyapp.Constants.FIRST_TOPIC_NAME;
import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_DATA_EMPTY_STRING;
import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_TYPE_HOME;
import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_TYPE_PHONE;
import static com.sellger.konta.sketch_loyaltyapp.Constants.NOT_ANONYMOUS_REGISTRATION;
import static com.sellger.konta.sketch_loyaltyapp.Constants.RC_SIGN_IN;
import static com.sellger.konta.sketch_loyaltyapp.Constants.REGISTRATION_ANONYMOUS;
import static com.sellger.konta.sketch_loyaltyapp.Constants.REGISTRATION_CONVERSION;
import static com.sellger.konta.sketch_loyaltyapp.Constants.REGISTRATION_NORMAL;
import static com.sellger.konta.sketch_loyaltyapp.Constants.SECOND_TOPIC_NAME;
import static com.sellger.konta.sketch_loyaltyapp.Constants.THIRD_TOPIC_NAME;

public class LogInFragment extends BaseFragment implements LogInContract.View, View.OnClickListener {

    private static final String TAG = LogInFragment.class.getSimpleName();

    LogInPresenter presenter;

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
                Log.d(TAG, "onSuccess");
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.w(TAG, "onError");
            }
        });
    }

    @Override
    public void initViews() {
        mSignInWithGoogleButton = rootView.findViewById(R.id.login_google_button);
        mSignInWithFacebookButton = rootView.findViewById(R.id.login_facebook_button);
        mSignInWithPhoneButton = rootView.findViewById(R.id.login_phone_button);
        mSignInAnonymously = rootView.findViewById(R.id.register_guest_text);
    }

    @Override
    public void onClick(View view) {

        if (checkInternetConnection()) {
            switch (view.getId()) {
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
                        displayAccountAlreadyExists();
                    } else {
                        anonymousSignIn();
                    }
                    break;
            }
        } else {
            Toast.makeText(getContext(),
                    getResources().getText(R.string.internet_connection_required), Toast.LENGTH_LONG)
                    .show();
        }
    }

    protected boolean checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        if (mFirebaseAuth.getCurrentUser() != null) {
            convertAnonymousAccount(credential);
        } else {
            signInWithGoogleCredential(credential);
        }
    }

    @Override
    public void signInWithGoogleCredential(AuthCredential credential) {
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithCredential:success");

                        // Subscribe user to topics of push notifications
                        subscribeToTopics(REGISTRATION_NORMAL);

                        // Open "home" view
                        // TODO: Workaround
                        // Pass string to display / hide information about account in nav view header
                        navigationPresenter.getSelectedLayoutType(LAYOUT_TYPE_HOME, NOT_ANONYMOUS_REGISTRATION);
                    } else {
                        // If sign in fails, display a message to the user
                        Toast.makeText(getContext(), "Oops, something went wrong", Toast.LENGTH_LONG).show();
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                    }
                });
    }

    @Override
    public void facebookSignIn() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
    }

    @Override
    public void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        if (mFirebaseAuth.getCurrentUser() != null) {
            convertAnonymousAccount(credential);
        } else {
            signInWithFacebookCredential(credential);
        }
    }

    @Override
    public void signInWithFacebookCredential(AuthCredential credential) {
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithCredential:success");

                        // Subscribe user to topics of push notifications
                        subscribeToTopics(REGISTRATION_NORMAL);

                        // Open "home" view
                        // TODO: Workaround
                        // Pass string to display / hide information about account in nav view header
                        navigationPresenter.getSelectedLayoutType(LAYOUT_TYPE_HOME, NOT_ANONYMOUS_REGISTRATION);
                    } else {
                        // If sign in fails, display a message to the user
                        Toast.makeText(getContext(), "Oops, something went wrong", Toast.LENGTH_LONG).show();
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                    }
                });
    }

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
                Log.d(TAG, "Google sign in failed", e);
            }
        }

        // Facebook
        // Pass the activity result back to the Facebook SDK
        mCallbackFacebookManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void anonymousSignIn() {
        mFirebaseAuth.signInAnonymously()
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInAnonymously:success");

                        // Subscribe user to topics of push notifications
                        subscribeToTopics(ANONYMOUS_REGISTRATION);

                        // Open "home" view
                        // TODO: Workaround
                        // Pass string to display / hide information about account in nav view header
                        navigationPresenter.getSelectedLayoutType(LAYOUT_TYPE_HOME, ANONYMOUS_REGISTRATION);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInAnonymously:failure", task.getException());
                        Toast.makeText(getContext(), "Authentication failed",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void convertAnonymousAccount(AuthCredential credential) {
        mFirebaseAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "linkWithCredential:success");

                        // Subscribe user to topics of push notifications
                        subscribeToTopics(REGISTRATION_CONVERSION);

                        // Open "home" view
                        // TODO: Workaround
                        // Pass string to display / hide information about account in nav view header
                        navigationPresenter.getSelectedLayoutType(LAYOUT_TYPE_HOME, NOT_ANONYMOUS_REGISTRATION);
                    } else {
                        Log.w(TAG, "linkWithCredential:failure", task.getException());
                        Toast.makeText(getContext(), "Authentication failed",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void displayAccountAlreadyExists() {
        Toast.makeText(getContext(), "Already signed in as a guest!\nPlease choose other option", Toast.LENGTH_LONG).show();
    }

    @Override
    public void subscribeToTopics(String subscriptionType) {
        String[] topicsList = new String[] {FIRST_TOPIC_NAME, SECOND_TOPIC_NAME, THIRD_TOPIC_NAME};

        switch (subscriptionType) {
            case REGISTRATION_NORMAL:
                for (String topic : topicsList) {
                    presenter.subscribeToTopic(topic);
                }
                break;
            case REGISTRATION_ANONYMOUS:
                presenter.subscribeToTopic(ANONYMOUS_TOPIC_NAME);
                break;
            case REGISTRATION_CONVERSION:
                for (String topic : topicsList) {
                    presenter.subscribeToTopic(topic);
                }
                presenter.unsubscribeFromTopic(ANONYMOUS_TOPIC_NAME);
                break;
            default:
                break;
        }
    }
}
