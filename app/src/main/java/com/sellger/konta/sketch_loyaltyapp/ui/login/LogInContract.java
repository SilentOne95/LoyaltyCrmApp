package com.sellger.konta.sketch_loyaltyapp.ui.login;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthCredential;

public interface LogInContract {

    interface View {

        void initViews();

        void googleSignIn();
        void signInWithGoogleCredential(AuthCredential credential);
        void firebaseAuthWithGoogle(GoogleSignInAccount account);

        void facebookSignIn();
        void signInWithFacebookCredential(AuthCredential credential);
        void handleFacebookAccessToken(AccessToken token);

        void anonymousSignIn();

        void convertAnonymousAccount(AuthCredential credential);

        void displayAccountAlreadyExists();

        void subscribeToTopics(String subscriptionType);
    }

    interface Presenter {

    }
}
