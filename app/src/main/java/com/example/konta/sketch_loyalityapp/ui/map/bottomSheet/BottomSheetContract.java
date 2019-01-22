package com.example.konta.sketch_loyalityapp.ui.map.bottomSheet;

import android.util.SparseArray;

import com.example.konta.sketch_loyalityapp.pojo.map.Marker;

import java.util.List;

public interface BottomSheetContract {

    interface ContactInfoView {

        void setUpViewsWithData(List<Marker> markerList);
    }

    interface OpeningHoursView {

        void setUpViewsWithData(SparseArray<String> singleDayOpenHours);
    }

    interface ContactInfoPresenter {

        void setUpObservable();
    }

    interface OpeningHoursPresenter {

        void setUpObservable();

        void formatOpenHoursData(List<Marker> markerList);
        String checkIfOpenHoursAreValid(String openHour, String openMinute, String closeHour, String closeMinute);}
}
