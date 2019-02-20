package com.example.konta.sketch_loyalityapp.ui.signUp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.konta.sketch_loyalityapp.R;
import com.example.konta.sketch_loyalityapp.base.BaseFragment;

public class SignUpFragment extends BaseFragment implements View.OnClickListener {

    private TextView mLogInTextView;

    @Override
    protected int getLayout() { return R.layout.fragment_sing_up; }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mLogInTextView = rootView.findViewById(R.id.log_in_text_view);
        mLogInTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }

}
