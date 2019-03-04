package com.example.konta.sketch_loyalityapp.ui.login;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public interface LogInContract {

    interface View {

        void googleSignIn();
        void facebookSignIn();
        void firebaseAuthWithGoogle(GoogleSignInAccount account);
        void handleFacebookAccessToken(AccessToken token);
        void anonymousSignIn();
    }

    interface Presenter {

    }
}
