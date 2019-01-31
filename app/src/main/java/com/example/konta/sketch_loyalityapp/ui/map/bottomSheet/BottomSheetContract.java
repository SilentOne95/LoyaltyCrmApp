package com.example.konta.sketch_loyalityapp.ui.map.bottomSheet;

import com.example.konta.sketch_loyalityapp.pojo.map.Marker;
import com.example.konta.sketch_loyalityapp.pojo.map.OpenHour;

import java.util.List;

public interface BottomSheetContract {

    interface ContactInfoView {

        void setUpViewsWithData(String phoneNumber, String emailAddress);
    }

    interface OpeningHoursView {

        void setUpViewsWithData(String[] singleDayOpenHours);
    }

    interface ContactInfoPresenter {

        void setUpObservable();

        void getMarkerList();

        void formatContactInfoData(List<Marker> markerList);
        String formatPhoneNumber(Marker marker);
        String formatEmailAddress(Marker marker);
        void passDataToView(String phoneNumber, String emailAddress);
    }

    interface OpeningHoursPresenter {

        void setUpObservable();

        void getMarkerList();
        void formatOpenHoursData(List<Marker> markerList);
        String checkIfOpenHoursAreValid(OpenHour time);
    }
}
