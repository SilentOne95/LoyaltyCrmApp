package com.example.konta.sketch_loyalityapp.ui.contact;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.konta.sketch_loyalityapp.R;
import com.example.konta.sketch_loyalityapp.base.BaseFragment;

public class ContactFragment extends BaseFragment {

    private static final String TAG = ContactFragment.class.getSimpleName();

    @Override
    protected int getLayout() { return R.layout.fragment_contact; }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Kontakt");
    }
}