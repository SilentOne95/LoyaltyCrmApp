package com.example.konta.sketch_loyalityapp.ui.map.bottomSheet;

import com.example.konta.sketch_loyalityapp.pojo.map.Marker;
import com.example.konta.sketch_loyalityapp.pojo.map.OpenHour;

import java.util.List;

public interface BottomSheetContract {

    interface ContactInfoView {

        void setUpViewsWithData(List<Marker> markerList);
    }

    interface OpeningHoursView {

        void setUpViewsWithData(String[] singleDayOpenHours);
    }

    interface ContactInfoPresenter {

        void setUpObservable();

        void passDataToView(List<Marker> markerList);

        void getMarkerList();
    }

    interface OpeningHoursPresenter {

        void setUpObservable();

        void getMarkerList();
        void formatOpenHoursData(List<Marker> markerList);
        String checkIfOpenHoursAreValid(OpenHour time);
    }
}
