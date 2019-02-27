package com.example.konta.sketch_loyalityapp.ui.login.phoneNumber;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.konta.sketch_loyalityapp.R;
import com.example.konta.sketch_loyalityapp.base.BaseActivity;
import com.example.konta.sketch_loyalityapp.base.BaseFragment;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LogInVerifyFragment extends BaseFragment {

    private static final String TAG = LogInVerifyFragment.class.getSimpleName();

    private TextInputEditText mTextInputCode;
    private TextView mTextWaitingForCode;
    private ProgressBar mProgressBar;

    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacksPhoneNumber;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private String mSmsCode, mVerificationId;

    // TODO: Replace this variable
    // Add white-listed data: emulator's or real phone number and verification code:
    private String mPhoneNumber = "";
    private String mTestPhoneNumber = "";
    private String mTestVerificationCode = "";

    public LogInVerifyFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayout() { return R.layout.fragment_log_in_verify; }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Hide action bar
        ((BaseActivity) getActivity()).getSupportActionBar().hide();

        // Views
        mTextInputCode = rootView.findViewById(R.id.verify_code_input_text);
        mTextWaitingForCode = rootView.findViewById(R.id.verify_waiting_for_code_text);
        mProgressBar = rootView.findViewById(R.id.verify_progress_bar);

        // TODO:
        // Temporary init FirebaseAuth in this fragment - testing purpose
        mAuth = FirebaseAuth.getInstance();
        mAuth.useAppLanguage();

        // Log In with Phone Number
        mCallbacksPhoneNumber = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Log.d(TAG,"onVerificationCompleted:" + phoneAuthCredential);
                mSmsCode = phoneAuthCredential.getSmsCode();
                setCodeInEditText(mSmsCode);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verificationId, forceResendingToken);
                Log.d(TAG,"onCodeSent:" + forceResendingToken);
                mVerificationId = verificationId;
                mResendToken = forceResendingToken;
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        // TODO:
        // Decide whether run on emulator (testPhoneSignIn) or real device (phoneNumberSignIn)
        phoneNumberSignIn(mPhoneNumber);
    }

    private void testPhoneSignIn() {
        FirebaseAuthSettings firebaseAuthSettings = mAuth.getFirebaseAuthSettings();
        firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber(mTestPhoneNumber, mTestVerificationCode);

        PhoneAuthProvider phoneAuthProvider = PhoneAuthProvider.getInstance();
        phoneAuthProvider.verifyPhoneNumber(
                mTestPhoneNumber,
                60L,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        Log.d(TAG, "testOnVerificationCompleted: " + phoneAuthCredential);

                        setCodeInEditText(phoneAuthCredential.getSmsCode());
                        new Handler().postDelayed(() -> signInWithPhoneAuthCredential(phoneAuthCredential), 1000);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Log.w(TAG, "testOnVerificationFailed", e);
                    }
                }
        );
    }

    private void phoneNumberSignIn(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacksPhoneNumber);
    }

    private void setCodeInEditText(String code) {
        new Handler().postDelayed(() -> {
            mTextInputCode.setFocusableInTouchMode(true);
            mTextInputCode.setText(code);
            mTextWaitingForCode.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);
        }, 3000);

        // Verify sms code after 10 sec
        new Handler().postDelayed(() -> verifyPhoneNumberWithCode(mVerificationId, mSmsCode), 1000);
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = task.getResult().getUser();
                    } else {
                        Log.d(TAG, "signInWithCredential:failure", task.getException());
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                        }
                    }
                });
    }
}
