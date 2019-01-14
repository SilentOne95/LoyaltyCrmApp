package com.example.konta.sketch_loyalityapp.ui.map.bottomSheet.openingHours;

import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.example.konta.sketch_loyalityapp.data.map.Marker;
import com.example.konta.sketch_loyalityapp.ui.map.bottomSheet.BottomSheetContract;

import java.util.List;

public class OpeningHoursPresenter implements BottomSheetContract.OpeningHoursPresenter,
        BottomSheetContract.Model.OnFinishedListener {

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
    public void formatOpenHoursData(List<Marker> markerList) {
        SparseArray<String> list = new SparseArray<>();
        String openHour, openMinute, closeHour, closeMinute, day;
        Marker marker = markerList.get(0);

        // Monday
        openHour = marker.getOpenHours().getMonday().getOpenHour().toString();
        openMinute = marker.getOpenHours().getMonday().getOpenMinute().toString();
        closeHour = marker.getOpenHours().getMonday().getCloseHour().toString();
        closeMinute = marker.getOpenHours().getMonday().getCloseMinute().toString();

        day = openHour + ":" + openMinute + " - " + closeHour + ":" + closeMinute;
        list.append(0, day);

        if (view != null) {
            view.setUpViewsWithData(list);
        }
    }

    @Override
    public void onFinished(List<Marker> markerList) {
        formatOpenHoursData(markerList);
    }

    @Override
    public void onFailure(Throwable t) {

    }
}
