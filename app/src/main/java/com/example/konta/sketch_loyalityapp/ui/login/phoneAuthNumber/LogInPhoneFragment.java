package com.example.konta.sketch_loyalityapp.ui.login.phoneAuthNumber;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;

import com.example.konta.sketch_loyalityapp.R;
import com.example.konta.sketch_loyalityapp.base.BaseActivity;
import com.example.konta.sketch_loyalityapp.base.BaseFragment;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

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

        // Views
        mTextInputLayoutPrefix = rootView.findViewById(R.id.register_prefix_input_box);
        mTextInputPrefix = rootView.findViewById(R.id.register_prefix_input);
        mTextInputLayoutPhoneNumber = rootView.findViewById(R.id.register_number_input_box);
        mTextInputPhoneNumber = rootView.findViewById(R.id.register_number_input);
        mRegisterButton = rootView.findViewById(R.id.register_number_button);

        // Listeners
        mRegisterButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (isInputEditTextValid()) {
            // Hide keyboard and display next view
            mTextInputPrefix.onEditorAction(EditorInfo.IME_ACTION_DONE);
            mTextInputPhoneNumber.onEditorAction(EditorInfo.IME_ACTION_DONE);
            navigationPresenter.getSelectedLayoutType("code", mProvidedPhoneNumber);
        }
    }

    @Override
    public boolean isInputEditTextValid() {
        String prefix, phoneNumber, typeError;
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        boolean isValid = false;

        if (!TextUtils.isEmpty(mTextInputPrefix.getText().toString()) && !TextUtils.isEmpty(mTextInputPhoneNumber.getText().toString())) {
            prefix = mTextInputPrefix.getText().toString().trim();
            prefix = prefix.replaceAll("\\+", "");
            phoneNumber = mTextInputPhoneNumber.getText().toString().trim();
            String phone = "+" + prefix + phoneNumber;

            try {
                Phonenumber.PhoneNumber phonetest = phoneNumberUtil.parse(phone, null);
                isValid = phoneNumberUtil.isValidNumber(phonetest);

            } catch (NumberParseException e) {
                e.printStackTrace();
            }

            if (isValid) {
                mProvidedPhoneNumber = phone;
                return true;
            } else {
                displayErrorInputMessage("wrong data");
                return false;
            }

        } else {
            if (TextUtils.isEmpty(mTextInputPrefix.getText()) && TextUtils.isEmpty(mTextInputPhoneNumber.getText())) {
                typeError = "empty data";
            } else if (TextUtils.isEmpty(mTextInputPrefix.getText()) && !TextUtils.isEmpty(mTextInputPhoneNumber.getText())) {
                typeError = "empty prefix";
            } else if (!TextUtils.isEmpty(mTextInputPrefix.getText()) && TextUtils.isEmpty(mTextInputPhoneNumber.getText())) {
                typeError = "empty number";
            } else {
                typeError = "wrong type";
            }

            displayErrorInputMessage(typeError);

            return false;
        }
    }

    @Override
    public void displayErrorInputMessage(String type) {

        switch (type) {
            case "empty data":
                mTextInputLayoutPrefix.setError(" ");
                mTextInputLayoutPhoneNumber.setError("Please enter data");
                dismissError(mTextInputLayoutPrefix);
                dismissError(mTextInputLayoutPhoneNumber);
                break;
            case "empty prefix":
                mTextInputLayoutPrefix.setError(" ");
                dismissError(mTextInputLayoutPrefix);
                break;
            case "empty number":
                mTextInputLayoutPhoneNumber.setError("Please enter phone number");
                dismissError(mTextInputLayoutPhoneNumber);
                break;
            case "wrong type":
                mTextInputLayoutPhoneNumber.setError("Wrong format of data");
                dismissError(mTextInputLayoutPhoneNumber);
                break;
            case "wrong data":
                mTextInputLayoutPhoneNumber.setError("Wrong prefix or phone number");
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
        }, 2000);
    }
}
