package com.sellger.konta.sketch_loyaltyapp.ui.map.bottomSheet;

import com.sellger.konta.sketch_loyaltyapp.pojo.map.Marker;
import com.sellger.konta.sketch_loyaltyapp.pojo.map.OpenHour;

import java.util.List;

public interface BottomSheetContract {

    interface ContactInfoView {

        void initViews();

        void setUpViewsWithData(String phoneNumber, String emailAddress, String websiteAddress);
    }

    interface OpeningHoursView {

        void initViews();

        void setUpViewsWithData(String[] singleDayOpenHours);
    }

    interface ContactInfoPresenter {

        void setUpObservable();

        void getMarkerList();

        void formatContactInfoData(List<Marker> markerList);
        String formatPhoneNumber(Marker marker);
        String formatEmailAddress(Marker marker);
        String formatWebsiteAddress(Marker marker);
        void passDataToView(String phoneNumber, String emailAddress, String websiteAddress);
    }

    interface OpeningHoursPresenter {

        void setUpObservable();

        void getMarkerList();
        void formatOpenHoursData(List<Marker> markerList);
        String checkIfOpenHoursAreValid(OpenHour time);
    }
}
