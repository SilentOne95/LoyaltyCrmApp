package com.example.konta.sketch_loyalityapp.ui.map;

import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.view.View;

import com.example.konta.sketch_loyalityapp.data.map.Marker;
import com.example.konta.sketch_loyalityapp.ui.map.bottomSheet.BottomSheetContract;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class MapPresenter implements MapContract.Presenter, MapContract.Model.OnFinishedListener,
        BottomSheetContract.Model.OnFinishedListener {

    @Nullable
    private MapContract.View view;
    private MapContract.Model model;
    private BottomSheetContract.Model bottomModel;

    MapPresenter(@Nullable MapContract.View view, MapContract.Model model,
                 BottomSheetContract.Model bottomModel) {
        this.view = view;
        this.model = model;
        this.bottomModel = bottomModel;
    }

    @Override
    public void requestDataFromServer() {
        model.fetchDataFromServer(this);
    }

    @Override
    public void switchBottomSheetState(Object object) {

        if (view != null){
            if (object instanceof Marker) {
                if (view.getBottomSheetState() != BottomSheetBehavior.STATE_COLLAPSED) {
                    view.setBottomSheetState(BottomSheetBehavior.STATE_COLLAPSED);
                }

                requestSingleMarkerData();

            } else if (object instanceof LatLng) {
                if (view.getBottomSheetState() != BottomSheetBehavior.STATE_HIDDEN) {
                    view.setBottomSheetState(BottomSheetBehavior.STATE_HIDDEN);
                }
            } else if (object instanceof View){
                if (view.getBottomSheetState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    view.setBottomSheetState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    view.setBottomSheetState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        }
    }

    @Override
    public void requestSingleMarkerData() {
        bottomModel.fetchDataFromServer(this);
    }

    @Override
    public void onFinished(List<Marker> markerList) {
        if (view != null) {
            view.setUpCluster(markerList);
        }
    }

    @Override
    public void onFailure(Throwable t) {

    }
}
