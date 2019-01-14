package com.example.konta.sketch_loyalityapp.ui.map.bottomSheet.openingHours;

import android.support.annotation.Nullable;

import com.example.konta.sketch_loyalityapp.data.map.Marker;
import com.example.konta.sketch_loyalityapp.ui.map.bottomSheet.BottomSheetContract;

import java.util.List;

public class OpeningHoursPresenter implements BottomSheetContract.OpeningHoursPresenter, BottomSheetContract.Model.OnFinishedListener {

    @Nullable
    private BottomSheetContract.OpeningHoursView view;
    private BottomSheetContract.Model model;

    OpeningHoursPresenter(@Nullable BottomSheetContract.OpeningHoursView view,
                          BottomSheetContract.Model model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void requestMarkersList() {
        model.fetchMarkerList(this);
    }

    @Override
    public void onFinished(List<Marker> markerList) {

    }

    @Override
    public void onFailure(Throwable t) {

    }
}
