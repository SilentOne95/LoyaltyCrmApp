package com.example.konta.sketch_loyalityapp.ui.login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.konta.sketch_loyalityapp.R;
import com.example.konta.sketch_loyalityapp.base.BaseActivity;
import com.example.konta.sketch_loyalityapp.base.BaseFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import static com.example.konta.sketch_loyalityapp.Constants.RC_SIGN_IN;

public class LogInFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = LogInFragment.class.getSimpleName();

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private Button mLogInWithGoogleButton, mLogInWithFacebookButton, mLogInWithPhoneButton;


    @Override
    protected int getLayout() { return R.layout.fragment_log_in; }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mLogInWithGoogleButton = rootView.findViewById(R.id.login_google_button);
        mLogInWithGoogleButton.setOnClickListener(this);

        mLogInWithFacebookButton = rootView.findViewById(R.id.login_facebook_button);
        mLogInWithFacebookButton.setOnClickListener(this);

        mLogInWithPhoneButton = rootView.findViewById(R.id.login_phone_button);
        mLogInWithPhoneButton.setOnClickListener(this);

        ((BaseActivity) getActivity()).getSupportActionBar().hide();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), googleSignInOptions);

        // TODO: Create attach method
        // Temporary init FirebaseAuth in this fragment - testing purpose
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.login_google_button:
                googleSignIn();
                break;
            case R.id.login_facebook_button:
                break;
            case R.id.login_phone_button:
                break;
            default:
                break;

        }
    }

    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.d(TAG, "signInWithCredential:failure", task.getException());
                    }
                });
    }
}
