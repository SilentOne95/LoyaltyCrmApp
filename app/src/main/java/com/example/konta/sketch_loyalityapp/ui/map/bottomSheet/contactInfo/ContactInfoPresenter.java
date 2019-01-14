package com.example.konta.sketch_loyalityapp.ui.map.bottomSheet.contactInfo;

import android.support.annotation.Nullable;

import com.example.konta.sketch_loyalityapp.data.map.Marker;
import com.example.konta.sketch_loyalityapp.ui.map.bottomSheet.BottomSheetContract;

import java.util.List;

public class ContactInfoPresenter implements BottomSheetContract.ContactInfoPresenter, BottomSheetContract.Model.OnFinishedListener {

    @Nullable
    private BottomSheetContract.ContactInfoView view;
    private BottomSheetContract.Model model;

    ContactInfoPresenter(@Nullable BottomSheetContract.ContactInfoView view,
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
