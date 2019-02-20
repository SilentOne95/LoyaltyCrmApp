package com.example.konta.sketch_loyalityapp.ui.login;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.konta.sketch_loyalityapp.R;
import com.example.konta.sketch_loyalityapp.base.BaseFragment;

public class LogInFragment extends BaseFragment implements View.OnClickListener {

    private TextView mSignUpTextView;

    @Override
    protected int getLayout() { return R.layout.fragment_log_in; }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSignUpTextView = rootView.findViewById(R.id.sign_up_text_view);
        mSignUpTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
