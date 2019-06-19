package com.sellger.konta.sketch_loyaltyapp.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;

public interface SettingsContract {

    interface View {

        void initViews();

        void showDialogLogOut();

        void showDialogDeleteAccount();

        void logOutAccount();

        String checkAuthMethod();

        void deleteUserAccount();

        void unsubscribeAndUpdateUI();

        void displayToastMessage(String message);
    }

    interface Presenter {

        void saveSwitchState(SharedPreferences preferences, String name, boolean state);

        boolean getSwitchState(SharedPreferences preferences, String name);

        boolean isNetworkAvailable(Context context);
    }
}
