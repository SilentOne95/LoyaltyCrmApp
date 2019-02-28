package com.example.konta.sketch_loyalityapp.ui.login.phoneAuthCode;

import com.google.firebase.auth.PhoneAuthCredential;

public interface LogInVerifyContract {

    interface View {

        void testPhoneSignIn();

        void phoneNumberSignIn(String phoneNumber);
        void setCodeInEditText(String code);
        void verifyPhoneNumberWithCode(String verificationId, String code);
        void signInWithPhoneAuthCredential(PhoneAuthCredential credential);
    }
}
