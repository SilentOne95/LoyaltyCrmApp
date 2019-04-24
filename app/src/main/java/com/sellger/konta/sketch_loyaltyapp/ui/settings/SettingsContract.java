package com.sellger.konta.sketch_loyaltyapp.ui.settings;

import android.content.SharedPreferences;

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

        void saveSwitchState(SharedPreferences preferences, String name, boolean state);
        boolean getSwitchState(SharedPreferences preferences, String name);
    }
}
