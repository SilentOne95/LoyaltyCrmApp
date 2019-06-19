package com.sellger.konta.sketch_loyaltyapp.ui.login.phoneAuthNumber;

import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;

import com.sellger.konta.sketch_loyaltyapp.R;
import com.sellger.konta.sketch_loyaltyapp.base.activity.BaseActivity;
import com.sellger.konta.sketch_loyaltyapp.base.fragment.BaseFragment;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import static com.sellger.konta.sketch_loyaltyapp.Constants.DELAY_PHONE_AUTH_DISMISS_ERROR;
import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_TYPE_CODE;
import static com.sellger.konta.sketch_loyaltyapp.Constants.PHONE_EMPTY_DATA;
import static com.sellger.konta.sketch_loyaltyapp.Constants.PHONE_EMPTY_NUMBER;
import static com.sellger.konta.sketch_loyaltyapp.Constants.PHONE_EMPTY_PREFIX;
import static com.sellger.konta.sketch_loyaltyapp.Constants.PHONE_WRONG_DATA;
import static com.sellger.konta.sketch_loyaltyapp.Constants.PHONE_WRONG_TYPE;

public class LogInPhoneFragment extends BaseFragment implements LogInPhoneContract.View, View.OnClickListener {

    private static final String TAG = LogInPhoneFragment.class.getSimpleName();

    private TextInputLayout mTextInputLayoutPrefix;
    private TextInputEditText mTextInputPrefix;
    private TextInputLayout mTextInputLayoutPhoneNumber;
    private TextInputEditText mTextInputPhoneNumber;
    private Button mRegisterButton;

    private String mProvidedPhoneNumber;

    public LogInPhoneFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_log_in_phone;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Hide action bar
        ((BaseActivity) getActivity()).getSupportActionBar().hide();

        // Init views
        initViews();

        // Listeners
        mRegisterButton.setOnClickListener(this);
    }

    @Override
    public void initViews() {
        mTextInputLayoutPrefix = rootView.findViewById(R.id.register_prefix_input_box);
        mTextInputPrefix = rootView.findViewById(R.id.register_prefix_input);
        mTextInputLayoutPhoneNumber = rootView.findViewById(R.id.register_number_input_box);
        mTextInputPhoneNumber = rootView.findViewById(R.id.register_number_input);
        mRegisterButton = rootView.findViewById(R.id.register_number_button);
    }

    @Override
    public void onClick(View v) {
        if (isInputEditTextValid()) {
            // Hide keyboard and display next view
            mTextInputPrefix.onEditorAction(EditorInfo.IME_ACTION_DONE);
            mTextInputPhoneNumber.onEditorAction(EditorInfo.IME_ACTION_DONE);
            navigationPresenter.getSelectedLayoutType(LAYOUT_TYPE_CODE, mProvidedPhoneNumber);
        }
    }

    @Override
    public boolean isInputEditTextValid() {
        String prefix, phoneNumber, errorType;
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        boolean isValid = false;

        if (!TextUtils.isEmpty(mTextInputPrefix.getText().toString()) && !TextUtils.isEmpty(mTextInputPhoneNumber.getText().toString())) {
            prefix = mTextInputPrefix.getText().toString().trim();
            prefix = prefix.replaceAll("\\+", "");
            phoneNumber = mTextInputPhoneNumber.getText().toString().trim();
            String phone = "+" + prefix + " " + phoneNumber;

            try {
                Phonenumber.PhoneNumber phoneNumberObject = phoneNumberUtil.parse(phone, null);
                isValid = phoneNumberUtil.isValidNumber(phoneNumberObject);

            } catch (NumberParseException e) {
                e.printStackTrace();
            }

            if (isValid) {
                mProvidedPhoneNumber = phone;
                return true;
            } else {
                displayErrorInputMessage(PHONE_WRONG_DATA);
                return false;
            }

        } else {
            if (TextUtils.isEmpty(mTextInputPrefix.getText()) && TextUtils.isEmpty(mTextInputPhoneNumber.getText())) {
                errorType = PHONE_EMPTY_DATA;
            } else if (TextUtils.isEmpty(mTextInputPrefix.getText()) && !TextUtils.isEmpty(mTextInputPhoneNumber.getText())) {
                errorType = PHONE_EMPTY_PREFIX;
            } else if (!TextUtils.isEmpty(mTextInputPrefix.getText()) && TextUtils.isEmpty(mTextInputPhoneNumber.getText())) {
                errorType = PHONE_EMPTY_NUMBER;
            } else {
                errorType = PHONE_WRONG_TYPE;
            }

            displayErrorInputMessage(errorType);

            return false;
        }
    }

    @Override
    public void displayErrorInputMessage(String type) {

        switch (type) {
            case PHONE_EMPTY_DATA:
                mTextInputLayoutPrefix.setError(" ");
                mTextInputLayoutPhoneNumber.setError(getResources().getText(R.string.verify_error_empty_data));
                dismissError(mTextInputLayoutPrefix);
                dismissError(mTextInputLayoutPhoneNumber);
                break;
            case PHONE_EMPTY_PREFIX:
                mTextInputLayoutPrefix.setError(" ");
                dismissError(mTextInputLayoutPrefix);
                break;
            case PHONE_EMPTY_NUMBER:
                mTextInputLayoutPhoneNumber.setError(getResources().getText(R.string.verify_error_empty_number));
                dismissError(mTextInputLayoutPhoneNumber);
                break;
            case PHONE_WRONG_TYPE:
                mTextInputLayoutPhoneNumber.setError(getResources().getText(R.string.verify_error_wrong_type));
                dismissError(mTextInputLayoutPhoneNumber);
                break;
            case PHONE_WRONG_DATA:
                mTextInputLayoutPrefix.setError(" ");
                mTextInputLayoutPhoneNumber.setError(getResources().getText(R.string.verify_error_wrong_data));
                dismissError(mTextInputLayoutPrefix);
                dismissError(mTextInputLayoutPhoneNumber);
                break;
        }
    }

    @Override
    public void dismissError(View v) {
        new Handler().postDelayed(() -> {
            switch (v.getId()) {
                case R.id.register_prefix_input_box:
                    mTextInputLayoutPrefix.setError(null);
                case R.id.register_number_input_box:
                    mTextInputLayoutPhoneNumber.setError(null);
                default:
                    break;
            }
        }, DELAY_PHONE_AUTH_DISMISS_ERROR);
    }
}
