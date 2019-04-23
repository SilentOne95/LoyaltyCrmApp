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

import static com.sellger.konta.sketch_loyaltyapp.Constants.ANONYMOUS_TOPIC_NAME;
import static com.sellger.konta.sketch_loyaltyapp.Constants.FIRST_TOPIC_NAME;
import static com.sellger.konta.sketch_loyaltyapp.Constants.NOT_ANONYMOUS_REGISTRATION;
import static com.sellger.konta.sketch_loyaltyapp.Constants.REGISTRATION_CONVERSION;
import static com.sellger.konta.sketch_loyaltyapp.Constants.REGISTRATION_NORMAL;
import static com.sellger.konta.sketch_loyaltyapp.Constants.SECOND_TOPIC_NAME;
import static com.sellger.konta.sketch_loyaltyapp.Constants.THIRD_TOPIC_NAME;

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

                        setCodeInEditText(phoneAuthCredential.getSmsCode());
                        if (mFirebaseAuth.getCurrentUser() != null) {
                            new Handler().postDelayed(() -> convertAnonymousAccount(phoneAuthCredential), 1000);
                        } else {
                            new Handler().postDelayed(() -> signInWithPhoneAuthCredential(phoneAuthCredential), 1000);
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
    public void setCodeInEditText(String code) {
        new Handler().postDelayed(() -> {
            mTextInputCode.setFocusableInTouchMode(true);
            mTextInputCode.setText(code);
            mTextWaitingForCode.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);
        }, 3000);

        // Verify sms code with delay
        new Handler().postDelayed(() -> verifyPhoneNumberWithCode(mVerificationId, mSmsCode), 1000);
    }

    @Override
    public void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

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
                        subscribeToTopics(REGISTRATION_NORMAL);

                        // Switch layout to "home" with delay
                        // TODO: Workaround
                        // Pass string to display / hide information about account in nav view header
                        new Handler().postDelayed(() -> navigationPresenter.getSelectedLayoutType("home", NOT_ANONYMOUS_REGISTRATION),2000);
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
                        subscribeToTopics(REGISTRATION_CONVERSION);

                        // Switch layout to "home" with delay
                        // TODO: Workaround
                        // Pass string to display / hide information about account in nav view header
                        new Handler().postDelayed(() -> navigationPresenter.getSelectedLayoutType("home", NOT_ANONYMOUS_REGISTRATION),5000);
                    } else {
                        Log.w(TAG, "linkWithCredential:failure", task.getException());
                        Toast.makeText(getContext(), "Authentication failed",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void subscribeToTopics(String subscriptionType) {
        String[] topicsList = new String[] {FIRST_TOPIC_NAME, SECOND_TOPIC_NAME, THIRD_TOPIC_NAME};

        switch (subscriptionType) {
            case REGISTRATION_NORMAL:
                for (String topic : topicsList) {
                    presenter.subscribeToTopic(topic);
                }
                break;
            case REGISTRATION_CONVERSION:
                for (String topic : topicsList) {
                    presenter.subscribeToTopic(topic);
                }
                presenter.unsubscribeFromTopic(ANONYMOUS_TOPIC_NAME);
                break;
            default:
                break;
        }
    }
}
