package com.sellger.konta.sketch_loyaltyapp.ui.settings;

public interface SettingsContract {

    interface View {

        void initViews();

        void showDialogDeleteAccount();
        void deleteUserAccount();
    }

    interface Presenter {

    }
}
