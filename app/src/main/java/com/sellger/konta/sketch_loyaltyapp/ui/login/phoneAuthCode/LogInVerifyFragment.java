package com.sellger.konta.sketch_loyaltyapp.ui.login.phoneAuthCode;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputEditText;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sellger.konta.sketch_loyaltyapp.R;
import com.sellger.konta.sketch_loyaltyapp.base.activity.BaseActivity;
import com.sellger.konta.sketch_loyaltyapp.base.fragment.BaseFragment;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

import static com.sellger.konta.sketch_loyaltyapp.Constants.BUNDLE_DATA_STRING;
import static com.sellger.konta.sketch_loyaltyapp.Constants.DEFAULT_SMS_CODE;
import static com.sellger.konta.sketch_loyaltyapp.Constants.DELAY_LOADING_LOGIN_SWITCH_LAYOUT;
import static com.sellger.konta.sketch_loyaltyapp.Constants.DELAY_LOGIN_SWITCH_LAYOUT;
import static com.sellger.konta.sketch_loyaltyapp.Constants.DELAY_PHONE_AUTH;
import static com.sellger.konta.sketch_loyaltyapp.Constants.DELAY_SET_SMS_CODE;
import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_TYPE_HOME;
import static com.sellger.konta.sketch_loyaltyapp.Constants.NOT_ANONYMOUS_REGISTRATION;
import static com.sellger.konta.sketch_loyaltyapp.Constants.REGISTRATION_CONVERSION;
import static com.sellger.konta.sketch_loyaltyapp.Constants.REGISTRATION_NORMAL;
import static com.sellger.konta.sketch_loyaltyapp.Constants.TOAST_ACCOUNT_AUTH_FAILED;
import static com.sellger.konta.sketch_loyaltyapp.Constants.TOAST_ERROR;

public class LogInVerifyFragment extends BaseFragment implements LogInVerifyContract.View, View.OnClickListener {

    private static final String TAG = LogInVerifyFragment.class.getSimpleName();

    private LogInVerifyPresenter presenter;

    private TextInputEditText mTextInputCode;
    private TextView mTextWaitingForCode , mTextProvidedPhoneNumber, mTextSmsLimitReached;
    private ProgressBar mProgressBar;
    private CircularProgressButton mCircularProgressButton;

    private FirebaseAuth mFirebaseAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacksPhoneNumber;
    private PhoneAuthCredential mPhoneAuthCredential;
    private String mSmsCode, mVerificationId;
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

        mTextSmsLimitReached.setVisibility(View.GONE);
        mCircularProgressButton.setVisibility(View.GONE);
        mCircularProgressButton.setOnClickListener(this);

        // Setting up presenter
        presenter = new LogInVerifyPresenter(this);

        // Log In with Phone Number
        mCallbacksPhoneNumber = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                mSmsCode = phoneAuthCredential.getSmsCode();
                if (mSmsCode != null) {
                    displayCodeInEditText(mSmsCode);
                } else {
                    displaySmsCodeLimitInfo(phoneAuthCredential);
                }
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verificationId, forceResendingToken);
                mVerificationId = verificationId;
            }
        };

        // Extract additional data
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mProvidedPhoneNumber = bundle.getString(BUNDLE_DATA_STRING);
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
        mTextSmsLimitReached = rootView.findViewById(R.id.verify_sms_limit_reached_text);
        mCircularProgressButton = rootView.findViewById(R.id.verify_sms_limit_reached_button);
    }

    @Override
    public void onStart() {
        super.onStart();
        phoneNumberSignIn(mProvidedPhoneNumber);
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
        new Handler().postDelayed(() -> verifyPhoneNumberWithCode(PhoneAuthProvider.getCredential(mVerificationId, mSmsCode), false), DELAY_PHONE_AUTH);
    }

    @Override
    public void displaySmsCodeLimitInfo(PhoneAuthCredential credential) {
        mPhoneAuthCredential = credential;

        new Handler().postDelayed(() -> {
            mTextInputCode.setFocusableInTouchMode(true);
            mTextInputCode.setText(DEFAULT_SMS_CODE);
            mTextWaitingForCode.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);
            mTextSmsLimitReached.setVisibility(View.VISIBLE);
            mCircularProgressButton.setVisibility(View.VISIBLE);
        }, DELAY_SET_SMS_CODE);
    }

    @Override
    public void onClick(View v) {
        if (mPhoneAuthCredential != null) {
            mCircularProgressButton.startMorphAnimation();
            verifyPhoneNumberWithCode(mPhoneAuthCredential, true);
        }
    }

    @Override
    public void verifyPhoneNumberWithCode(PhoneAuthCredential credential, boolean isSmsLimitReached) {
        if (mFirebaseAuth.getCurrentUser() != null) {
            convertAnonymousAccount(credential, isSmsLimitReached);
        } else {
            signInWithPhoneAuthCredential(credential, isSmsLimitReached);
        }
    }

    @Override
    public void signInWithPhoneAuthCredential(PhoneAuthCredential credential, boolean isSmsLimitReached) {
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        // Subscribe user to topics of push notifications
                        presenter.manageTopicsSubscriptions(REGISTRATION_NORMAL);

                        // Open "home" view with delay, pass string to display / hide information about account in nav view header
                        if (isSmsLimitReached) {
                            finishLoadingAndSwitchScreen();
                        } else {
                            switchScreen();
                        }
                    } else {
                        displayToastMessage(TOAST_ACCOUNT_AUTH_FAILED);
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // TODO:
                            // The verification code entered was invalid
                        }
                    }
                });
    }

    @Override
    public void convertAnonymousAccount(AuthCredential credential, boolean isSmsLimitReached) {
        mFirebaseAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        // Subscribe user to topics of push notifications
                        presenter.manageTopicsSubscriptions(REGISTRATION_CONVERSION);

                        // Open "home" view with delay, pass string to display / hide information about account in nav view header
                        if (isSmsLimitReached) {
                            finishLoadingAndSwitchScreen();
                        } else {
                            switchScreen();
                        }
                    } else {
                        displayToastMessage(TOAST_ACCOUNT_AUTH_FAILED);
                    }
                });
    }

    @Override
    public void finishLoadingAndSwitchScreen() {
        new Handler().postDelayed(() -> mCircularProgressButton.doneLoadingAnimation(Color.rgb(255,152,0),
                BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_check)),DELAY_LOGIN_SWITCH_LAYOUT);
        new Handler().postDelayed(() -> navigationPresenter.getSelectedLayoutType(LAYOUT_TYPE_HOME, NOT_ANONYMOUS_REGISTRATION),DELAY_LOADING_LOGIN_SWITCH_LAYOUT);
    }

    @Override
    public void switchScreen() {
        new Handler().postDelayed(() -> navigationPresenter.getSelectedLayoutType(LAYOUT_TYPE_HOME, NOT_ANONYMOUS_REGISTRATION),DELAY_LOGIN_SWITCH_LAYOUT);
    }


    @Override
    public void displayToastMessage(String message) {
        switch (message) {
            case TOAST_ERROR:
                message = getString(R.string.default_toast_error_message);
                break;
            case TOAST_ACCOUNT_AUTH_FAILED:
                message = getString(R.string.account_auth_failed);
                break;
        }

        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }
}
