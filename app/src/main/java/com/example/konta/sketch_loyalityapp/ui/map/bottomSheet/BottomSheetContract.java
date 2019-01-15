package com.example.konta.sketch_loyalityapp.ui.map.bottomSheet;

import android.util.SparseArray;

import com.example.konta.sketch_loyalityapp.data.map.Marker;

import java.util.List;

public interface BottomSheetContract {

    interface ContactInfoView {

        void setUpViewsWithData(List<Marker> markerList);
    }

    interface OpeningHoursView {

        void setUpViewsWithData(SparseArray<String> singleDayOpenHours);
    }

    interface ContactInfoPresenter {

    }

    interface OpeningHoursPresenter {

        void formatOpenHoursData(List<Marker> markerList);
    }
}
