package com.sellger.konta.sketch_loyaltyapp.ui.login.phoneAuthCode;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sellger.konta.sketch_loyaltyapp.R;
import com.sellger.konta.sketch_loyaltyapp.base.BaseActivity;
import com.sellger.konta.sketch_loyaltyapp.base.BaseFragment;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import static com.sellger.konta.sketch_loyaltyapp.Constants.DEFAULT_SMS_CODE;
import static com.sellger.konta.sketch_loyaltyapp.Constants.DELAY_LOGIN_SWITCH_LAYOUT;
import static com.sellger.konta.sketch_loyaltyapp.Constants.DELAY_PHONE_AUTH;
import static com.sellger.konta.sketch_loyaltyapp.Constants.DELAY_SET_SMS_CODE;
import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_TYPE_HOME;
import static com.sellger.konta.sketch_loyaltyapp.Constants.NOT_ANONYMOUS_REGISTRATION;
import static com.sellger.konta.sketch_loyaltyapp.Constants.REGISTRATION_CONVERSION;
import static com.sellger.konta.sketch_loyaltyapp.Constants.REGISTRATION_NORMAL;
import static com.sellger.konta.sketch_loyaltyapp.Constants.TOAST_ERROR;
import static com.sellger.konta.sketch_loyaltyapp.Constants.TOAST_SMS_LIMIT;

public class LogInVerifyFragment extends BaseFragment implements LogInVerifyContract.View {

    private static final String TAG = LogInVerifyFragment.class.getSimpleName();

    LogInVerifyPresenter presenter;

    private TextInputEditText mTextInputCode;
    private TextView mTextWaitingForCode , mTextProvidedPhoneNumber;
    private ProgressBar mProgressBar;

    private FirebaseAuth mFirebaseAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacksPhoneNumber;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private String mSmsCode, mVerificationId;

    // TODO: Replace this variable
    // Add white-listed data: emulator's or real phone number and verification code:
    private String mPhoneNumber = "";
    private String mTestPhoneNumber = "";
    private String mTestVerificationCode = "";
    private String mProvidedPhoneNumber;

    public LogInVerifyFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayout() { return R.layout.fragment_log_in_verify; }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseAuth.useAppLanguage();

        // Hide action bar
        ((BaseActivity) getActivity()).getSupportActionBar().hide();

        // Init views
        initViews();

        // Setting up presenter
        presenter = new LogInVerifyPresenter(this);

        // Log In with Phone Number
        mCallbacksPhoneNumber = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Log.d(TAG,"onVerificationCompleted:" + phoneAuthCredential);

                mSmsCode = phoneAuthCredential.getSmsCode();
                Log.d(TAG, "sms code: " + mSmsCode);
                if (mSmsCode != null) {
                    displayCodeInEditText(mSmsCode);
                } else {
                    displaySmsCodeLimitInfo(phoneAuthCredential);
                }
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verificationId, forceResendingToken);
                Log.d(TAG,"onCodeSent:" + forceResendingToken);
                Log.d(TAG,"onCodeSent:" + verificationId);

                mVerificationId = verificationId;
                mResendToken = forceResendingToken;
            }
        };

        // Extract additional data
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mProvidedPhoneNumber = bundle.getString("DATA_STRING");
            mProvidedPhoneNumber = getResources().getText(R.string.verify_text) + " " + mProvidedPhoneNumber;
            mTextProvidedPhoneNumber.setText(mProvidedPhoneNumber);
        }
    }

    @Override
    public void initViews() {
        mTextProvidedPhoneNumber = rootView.findViewById(R.id.verify_your_number_text);
        mTextInputCode = rootView.findViewById(R.id.verify_code_input_text);
        mTextWaitingForCode = rootView.findViewById(R.id.verify_waiting_for_code_text);
        mProgressBar = rootView.findViewById(R.id.verify_progress_bar);
    }

    @Override
    public void onStart() {
        super.onStart();
        // TODO:
        // Decide whether run on emulator (testPhoneSignIn) or real device (phoneNumberSignIn)
        phoneNumberSignIn(mProvidedPhoneNumber);
    }

    @Override
    public void testPhoneSignIn() {
        FirebaseAuthSettings firebaseAuthSettings = mFirebaseAuth.getFirebaseAuthSettings();
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

                        displayCodeInEditText(phoneAuthCredential.getSmsCode());
                        if (mFirebaseAuth.getCurrentUser() != null) {
                            new Handler().postDelayed(() -> convertAnonymousAccount(phoneAuthCredential), DELAY_PHONE_AUTH);
                        } else {
                            new Handler().postDelayed(() -> signInWithPhoneAuthCredential(phoneAuthCredential), DELAY_PHONE_AUTH);
                        }
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Log.w(TAG, "testOnVerificationFailed", e);
                    }
                }
        );
    }

    @Override
    public void phoneNumberSignIn(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacksPhoneNumber);
    }

    @Override
    public void displayCodeInEditText(String code) {
        new Handler().postDelayed(() -> {
            mTextInputCode.setFocusableInTouchMode(true);
            mTextInputCode.setText(code);
            mTextWaitingForCode.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);
        }, DELAY_SET_SMS_CODE);

        // Verify sms code with delay
        new Handler().postDelayed(() -> verifyPhoneNumberWithCode(PhoneAuthProvider.getCredential(mVerificationId, mSmsCode)), DELAY_PHONE_AUTH);
    }

    @Override
    public void displaySmsCodeLimitInfo(PhoneAuthCredential credential) {
        new Handler().postDelayed(() -> {
            mTextInputCode.setFocusableInTouchMode(true);
            mTextInputCode.setText(DEFAULT_SMS_CODE);
            mTextWaitingForCode.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);
        }, DELAY_SET_SMS_CODE);

        displayToastMessage(TOAST_SMS_LIMIT);
        new Handler().postDelayed(() -> verifyPhoneNumberWithCode(credential), DELAY_PHONE_AUTH);
    }

    @Override
    public void verifyPhoneNumberWithCode(PhoneAuthCredential credential) {
        if (mFirebaseAuth.getCurrentUser() != null) {
            convertAnonymousAccount(credential);
        } else {
            signInWithPhoneAuthCredential(credential);
        }
    }

    @Override
    public void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithCredential:success");

                        // Subscribe user to topics of push notifications
                        presenter.manageTopicsSubscriptions(REGISTRATION_NORMAL);

                        // Open "home" view with delay, pass string to display / hide information about account in nav view header
                        new Handler().postDelayed(() -> navigationPresenter.getSelectedLayoutType(LAYOUT_TYPE_HOME, NOT_ANONYMOUS_REGISTRATION),DELAY_LOGIN_SWITCH_LAYOUT);
                    } else {
                        Log.d(TAG, "signInWithCredential:failure", task.getException());
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                        }
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
                        presenter.manageTopicsSubscriptions(REGISTRATION_CONVERSION);

                        // Open "home" view with delay, pass string to display / hide information about account in nav view header
                        new Handler().postDelayed(() -> navigationPresenter.getSelectedLayoutType(LAYOUT_TYPE_HOME, NOT_ANONYMOUS_REGISTRATION),DELAY_LOGIN_SWITCH_LAYOUT);
                    } else {
                        Log.w(TAG, "linkWithCredential:failure", task.getException());
                        Toast.makeText(getContext(), "Authentication failed",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void displayToastMessage(String message) {
        switch (message) {
            case TOAST_ERROR:
                message = (String) getText(R.string.default_toast_error_message);
            case TOAST_SMS_LIMIT:
                message = (String) getText(R.string.sms_code_limit_reached);
            default:
                break;
        }

        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }
}
