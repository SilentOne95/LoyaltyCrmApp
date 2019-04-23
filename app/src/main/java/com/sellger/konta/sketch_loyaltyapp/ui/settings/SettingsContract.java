package com.sellger.konta.sketch_loyaltyapp.ui.settings;

public interface SettingsContract {

    interface View {

        void initViews();

        void showDialogLogOut();
        void logOutAccount();

        void showDialogDeleteAccount();
        void deleteUserAccount();

        void unsubscribeAndUpdateUI();
    }

    interface Presenter {

    }
}
