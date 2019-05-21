package com.sellger.konta.sketch_loyaltyapp.ui.login;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthCredential;

public interface LogInContract {

    interface View {

        void initViews();

        void googleSignIn();

        void firebaseAuthWithGoogle(GoogleSignInAccount account);

        void facebookSignIn();

        void handleFacebookAccessToken(AccessToken token);

        void signInWithCredential(AuthCredential credential);

        void anonymousSignIn();

        void convertAnonymousAccount(AuthCredential credential);

        void displayToastMessage(String message);
    }

    interface Presenter {

        void manageTopicsSubscriptions(String subscriptionType);
    }
}
