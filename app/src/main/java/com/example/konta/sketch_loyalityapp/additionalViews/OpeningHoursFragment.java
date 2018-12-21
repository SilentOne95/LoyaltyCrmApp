package com.example.konta.sketch_loyalityapp.additionalViews;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.konta.sketch_loyalityapp.R;

public class OpeningHoursFragment extends Fragment {

    public OpeningHoursFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_opening_hours, container, false);
    }
}