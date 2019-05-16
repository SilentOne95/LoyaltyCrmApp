package com.sellger.konta.sketch_loyaltyapp.ui.map.bottomSheet;

import com.sellger.konta.sketch_loyaltyapp.data.entity.Marker;
import com.sellger.konta.sketch_loyaltyapp.data.entity.OpenHour;

public interface BottomSheetContract {

    interface ContactInfoView {

        void initViews();

        void setUpViewsWithData(String phoneNumber, String emailAddress, String websiteAddress);
    }

    interface ContactInfoPresenter {

        void setUpObservable();

        void getSelectedMarker(int markerId);

        void formatMarkerData(Marker marker);

        String formatPhoneNumber(Marker marker);

        String formatEmailAddress(Marker marker);

        String formatWebsiteAddress(Marker marker);

        void passDataToView(String phoneNumber, String emailAddress, String websiteAddress);
    }

    interface OpeningHoursView {

        void initViews();

        void setUpViewsWithData(String[] singleDayOpenHours);
    }

    interface OpeningHoursPresenter {

        void setUpObservable();

        void getSelectedMarker(int markerId);

        void formatMarkerData(Marker marker);

        String checkIfOpenHoursAreValid(OpenHour time);

        void passDataToView(String[] singleDayOpenHours);
    }
}
