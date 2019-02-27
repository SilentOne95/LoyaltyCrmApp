package com.example.konta.sketch_loyalityapp.ui.login.phoneNumber;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.konta.sketch_loyalityapp.R;
import com.example.konta.sketch_loyalityapp.base.BaseActivity;
import com.example.konta.sketch_loyalityapp.base.BaseFragment;

public class LogInVerifyFragment extends BaseFragment {

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
    }
}
