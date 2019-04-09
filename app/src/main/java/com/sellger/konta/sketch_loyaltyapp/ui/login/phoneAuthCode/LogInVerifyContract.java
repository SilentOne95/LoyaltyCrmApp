package com.sellger.konta.sketch_loyaltyapp.ui.login.phoneAuthCode;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.PhoneAuthCredential;

public interface LogInVerifyContract {

    interface View {

        void initViews();

        void testPhoneSignIn();

        void phoneNumberSignIn(String phoneNumber);
        void setCodeInEditText(String code);
        void verifyPhoneNumberWithCode(String verificationId, String code);
        void signInWithPhoneAuthCredential(PhoneAuthCredential credential);

        void convertAnonymousAccount(AuthCredential credential);
    }
}
