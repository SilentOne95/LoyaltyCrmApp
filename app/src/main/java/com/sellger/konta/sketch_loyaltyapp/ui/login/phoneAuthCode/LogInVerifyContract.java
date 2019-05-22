package com.sellger.konta.sketch_loyaltyapp.ui.login.phoneAuthCode;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.PhoneAuthCredential;

public interface LogInVerifyContract {

    interface View {

        void initViews();

        void testPhoneSignIn();

        void phoneNumberSignIn(String phoneNumber);

        void displayCodeInEditText(String code);

        void displaySmsCodeLimitInfo(PhoneAuthCredential credential);

        void verifyPhoneNumberWithCode(PhoneAuthCredential credential, boolean isSmsLimitReached);

        void signInWithPhoneAuthCredential(PhoneAuthCredential credential, boolean isSmsLimitReached);

        void convertAnonymousAccount(AuthCredential credential, boolean isSmsLimitReached);

        void finishLoadingAndSwitchScreen();

        void switchScreen();

        void displayToastMessage(String message);
    }

    interface Presenter {

    }
}
