package com.example.konta.sketch_loyalityapp.ui.login.phoneNumber;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.konta.sketch_loyalityapp.R;
import com.example.konta.sketch_loyalityapp.base.BaseActivity;
import com.example.konta.sketch_loyalityapp.base.BaseFragment;

public class LogInPhoneFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = LogInPhoneFragment.class.getSimpleName();

    private EditText mTextInputPrefix;
    private EditText mTextInputPhoneNumber;
    private Button mRegisterButton;

    public LogInPhoneFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayout() { return R.layout.fragment_log_in_phone; }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Hide action bar
        ((BaseActivity) getActivity()).getSupportActionBar().hide();

        // Views
        mTextInputPrefix = rootView.findViewById(R.id.register_prefix_input);
        mTextInputPhoneNumber = rootView.findViewById(R.id.register_number_input);
        mRegisterButton = rootView.findViewById(R.id.register_number_button);

        // Listeners
        mRegisterButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (getTextInputEditText()) {
            navigationPresenter.getSelectedLayoutType("code");
        }
    }

    private boolean getTextInputEditText() {
        String prefix, phoneNumber, number;

        if (!TextUtils.isEmpty(mTextInputPrefix.getText().toString()) && !TextUtils.isEmpty(mTextInputPhoneNumber.getText().toString())) {
            prefix = "+" + mTextInputPrefix.getText().toString().trim();
            phoneNumber = mTextInputPhoneNumber.getText().toString().trim();
            number = prefix + phoneNumber;

            return true;
        } else {
            if (mTextInputPrefix.getText() == null) {
                mTextInputPrefix.setError("Wrong prefix type");
            } else if (mTextInputPhoneNumber.getText() == null) {
                mTextInputPhoneNumber.setError("Please enter phone number");
            } else {
                mTextInputPhoneNumber.setError("Wrong format of data");
            }

            return false;
        }
    }
}
