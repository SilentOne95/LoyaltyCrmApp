package com.ecommercelab.loyaltyapp.ui.login.phoneAuthCode;

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

import com.ecommercelab.loyaltyapp.R;
import com.ecommercelab.loyaltyapp.base.activity.BaseActivity;
import com.ecommercelab.loyaltyapp.base.fragment.BaseFragment;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

import static com.ecommercelab.loyaltyapp.Constants.BUNDLE_DATA_STRING;
import static com.ecommercelab.loyaltyapp.Constants.DEFAULT_SMS_CODE;
import static com.ecommercelab.loyaltyapp.Constants.DELAY_LOADING_LOGIN_SWITCH_LAYOUT;
import static com.ecommercelab.loyaltyapp.Constants.DELAY_LOGIN_SWITCH_LAYOUT;
import static com.ecommercelab.loyaltyapp.Constants.DELAY_PHONE_AUTH;
import static com.ecommercelab.loyaltyapp.Constants.DELAY_SET_SMS_CODE;
import static com.ecommercelab.loyaltyapp.Constants.LAYOUT_TYPE_HOME;
import static com.ecommercelab.loyaltyapp.Constants.NOT_ANONYMOUS_REGISTRATION;
import static com.ecommercelab.loyaltyapp.Constants.REGISTRATION_CONVERSION;
import static com.ecommercelab.loyaltyapp.Constants.REGISTRATION_NORMAL;
import static com.ecommercelab.loyaltyapp.Constants.TOAST_ACCOUNT_AUTH_FAILED;
import static com.ecommercelab.loyaltyapp.Constants.TOAST_ERROR;
import static com.ecommercelab.loyaltyapp.Constants.TOAST_PHONE_NUMBER_AUTH_FAILED;

public class LogInVerifyFragment extends BaseFragment implements LogInVerifyContract.View, View.OnClickListener {

    private static final String TAG = LogInVerifyFragment.class.getSimpleName();

    private LogInVerifyPresenter presenter;

    private TextInputEditText mTextInputCode;
    private TextView mTextWaitingForCode, mTextProvidedPhoneNumber, mTextSmsLimitReached;
    private ProgressBar mProgressBar;
    private CircularProgressButton mCircularProgressButton;

    private FirebaseAuth mFirebaseAuth;
    private PhoneAuthCredential mPhoneAuthCredential;
    private String mSmsCode, mVerificationId;
    private String mProvidedPhoneNumber;

    public LogInVerifyFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_log_in_verify;
    }

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

        // Extract additional data
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mProvidedPhoneNumber = bundle.getString(BUNDLE_DATA_STRING);
            mProvidedPhoneNumber = getResources().getText(R.string.verify_text) + " " + mProvidedPhoneNumber;
            mTextProvidedPhoneNumber.setText(mProvidedPhoneNumber);
        }
    }

    /**
     * Called from {@link #onCreate(Bundle)} to init all the views.
     */
    @Override
    public void initViews() {
        mTextProvidedPhoneNumber = rootView.findViewById(R.id.verify_your_number_text);
        mTextInputCode = rootView.findViewById(R.id.verify_code_input_text);
        mTextWaitingForCode = rootView.findViewById(R.id.verify_waiting_for_code_text);
        mProgressBar = rootView.findViewById(R.id.verify_progress_bar);
        mTextSmsLimitReached = rootView.findViewById(R.id.verify_sms_limit_reached_text);
        mCircularProgressButton = rootView.findViewById(R.id.verify_sms_limit_reached_button);

        mTextSmsLimitReached.setVisibility(View.GONE);
        mCircularProgressButton.setVisibility(View.GONE);
        mCircularProgressButton.setOnClickListener(this);
    }

    /**
     * Called from {@link #onStart()} to kick off phone number registration method.
     *
     * @param phoneNumber string
     * @see <a href="https://firebase.google.com/docs/auth/android/phone-auth">Firebase Doc</a>
     */
    private void phoneNumberSignIn(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacksPhoneNumber);
    }

    /**
     * Implementation of callback listener that handle auth events.
     *
     * @see <a href="https://firebase.google.com/docs/reference/android/com/google/firebase/auth/PhoneAuthProvider.OnVerificationStateChangedCallbacks">Firebase Doc</a>
     */
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacksPhoneNumber = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
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
            displayToastMessage(TOAST_PHONE_NUMBER_AUTH_FAILED);
        }

        @Override
        public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(verificationId, forceResendingToken);
            mVerificationId = verificationId;
        }
    };

    /**
     * Called from callback listener implemented in {@link #onCreate(Bundle)} to set received verification
     * SMS code and set in EditText and verify phone number with this code.
     *
     * @param code string verification
     */
    private void displayCodeInEditText(String code) {
        new Handler().postDelayed(() -> {
            mTextInputCode.setFocusableInTouchMode(true);
            mTextInputCode.setText(code);
            mTextWaitingForCode.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);
        }, DELAY_SET_SMS_CODE);

        // Verify sms code with delay
        new Handler().postDelayed(() -> verifyPhoneNumberWithCode(PhoneAuthProvider.getCredential(mVerificationId, mSmsCode), false), DELAY_PHONE_AUTH);
    }

    /**
     * Called from callback listener implemented in {@link #onCreate(Bundle)} to display info about
     * reached sending SMS limit with verification code.
     *
     * @param credential that wraps phone number and verification information for authentication purposes
     */
    private void displaySmsCodeLimitInfo(PhoneAuthCredential credential) {
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

    /**
     * Called from {@link #displayCodeInEditText(String)} and {@link #onClick(View)} to consider
     * whether account should be converted from anonymous or just created new one.
     *
     * @param credential        that wraps phone number and verification information for authentication purposes
     * @param isSmsLimitReached boolean value that inform about reached sending SMS limit
     */
    private void verifyPhoneNumberWithCode(PhoneAuthCredential credential, boolean isSmsLimitReached) {
        if (mFirebaseAuth.getCurrentUser() != null) {
            convertAnonymousAccount(credential, isSmsLimitReached);
        } else {
            signInWithPhoneAuthCredential(credential, isSmsLimitReached);
        }
    }

    /**
     * Called from {@link #verifyPhoneNumberWithCode(PhoneAuthCredential, boolean)} to sign user with
     * phone number auth credentials and subscribe to push notification topics.
     *
     * @param credential        that wraps phone number and verification information for authentication purposes
     * @param isSmsLimitReached boolean value that inform about reached sending SMS limit
     */
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential, boolean isSmsLimitReached) {
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

    /**
     * Called from {@link #verifyPhoneNumberWithCode(PhoneAuthCredential, boolean)} to convert
     * anonymous account to Google / Facebook depends on the choice.
     *
     * @param credential        that wraps phone number and verification information for authentication purposes
     * @param isSmsLimitReached boolean value that inform about reached sending SMS limit
     */
    private void convertAnonymousAccount(AuthCredential credential, boolean isSmsLimitReached) {
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

    /**
     * Called from {@link #signInWithPhoneAuthCredential(PhoneAuthCredential, boolean)} and
     * {@link #convertAnonymousAccount(AuthCredential, boolean)} (if reached SMS limit) to set
     * loading indicator to finish state and switch screen.
     */
    private void finishLoadingAndSwitchScreen() {
        new Handler().postDelayed(() -> mCircularProgressButton.doneLoadingAnimation(Color.rgb(255, 152, 0),
                BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_check)), DELAY_LOGIN_SWITCH_LAYOUT);
        new Handler().postDelayed(() -> navigationPresenter.getSelectedLayoutType(LAYOUT_TYPE_HOME, NOT_ANONYMOUS_REGISTRATION), DELAY_LOADING_LOGIN_SWITCH_LAYOUT);
    }

    /**
     * Called from {@link #signInWithPhoneAuthCredential(PhoneAuthCredential, boolean)} and
     * {@link #convertAnonymousAccount(AuthCredential, boolean)} (if didn't reach SMS limit) to
     * switch screen.
     */
    private void switchScreen() {
        new Handler().postDelayed(() -> navigationPresenter.getSelectedLayoutType(LAYOUT_TYPE_HOME, NOT_ANONYMOUS_REGISTRATION), DELAY_LOGIN_SWITCH_LAYOUT);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param view which was clicked
     * @see <a href="https://developer.android.com/reference/android/view/View.OnClickListener">Android Dev Doc</a>
     */
    @Override
    public void onClick(View view) {
        if (mPhoneAuthCredential != null) {
            mCircularProgressButton.startMorphAnimation();
            verifyPhoneNumberWithCode(mPhoneAuthCredential, true);
        }
    }

    /**
     * Called from {@link #signInWithPhoneAuthCredential(PhoneAuthCredential, boolean)} and
     * {@link #convertAnonymousAccount(AuthCredential, boolean)} to display to user relevant
     * string with toast message.
     *
     * @param message that contains info what kind of message should be displayed
     */
    private void displayToastMessage(String message) {
        switch (message) {
            case TOAST_ERROR:
                message = getString(R.string.default_toast_error_message);
                break;
            case TOAST_ACCOUNT_AUTH_FAILED:
                message = getString(R.string.account_auth_failed);
                break;
            case TOAST_PHONE_NUMBER_AUTH_FAILED:
                message = getString(R.string.phone_number_auth_failed);
                break;
        }

        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        phoneNumberSignIn(mProvidedPhoneNumber);
    }
}
