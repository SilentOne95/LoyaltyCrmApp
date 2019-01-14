package com.example.konta.sketch_loyalityapp.ui.map.bottomSheet.openingHours;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.konta.sketch_loyalityapp.R;
import com.example.konta.sketch_loyalityapp.base.BaseFragment;
import com.example.konta.sketch_loyalityapp.ui.map.bottomSheet.BottomSheetContract;
import com.example.konta.sketch_loyalityapp.ui.map.bottomSheet.BottomSheetModel;

public class OpeningHoursFragment extends BaseFragment implements BottomSheetContract.OpeningHoursView {

    OpeningHoursPresenter presenter;

    public OpeningHoursFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayout() { return R.layout.fragment_opening_hours; }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new OpeningHoursPresenter(this, new BottomSheetModel());
        presenter.requestMarker();
    }
}