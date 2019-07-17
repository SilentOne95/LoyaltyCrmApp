package com.jemsushi.loyaltyapp.ui.map.bottomSheet;

public interface BottomSheetContract {

    interface OpeningHoursView {

        void setUpViewsWithData(String[] singleDayOpenHours);

        void displayToastMessage(String message);
    }

    interface OpeningHoursPresenter {

        void setUpObservable();
    }

    interface ContactInfoView {

        void setUpViewsWithData(String phoneNumber, String emailAddress, String websiteAddress);

        void displayToastMessage(String message);
    }

    interface ContactInfoPresenter {

        void setUpObservable();
    }
}
