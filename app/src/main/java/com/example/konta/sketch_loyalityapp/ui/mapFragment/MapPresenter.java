package com.example.konta.sketch_loyalityapp.ui.mapFragment;

import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.view.View;

import com.example.konta.sketch_loyalityapp.modelClasses.ItemLocation;
import com.google.android.gms.maps.model.LatLng;

public class MapPresenter implements MapContract.Presenter {

    @Nullable
    private MapContract.View view;

    MapPresenter(MapContract.View view) { this.view = view; }

    @Override
    public void switchBottomSheetState(Object object) {

        if (object instanceof ItemLocation) {
            if (view.getBottomSheetState() != BottomSheetBehavior.STATE_COLLAPSED) {
                view.setBottomSheetState(BottomSheetBehavior.STATE_COLLAPSED);
            }
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
