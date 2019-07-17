package com.jemsushi.loyaltyapp.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;

public interface SettingsContract {

    interface View {

    }

    interface Presenter {

        void saveSwitchState(SharedPreferences preferences, String name, boolean state);

        boolean getSwitchState(SharedPreferences preferences, String name);

        boolean isNetworkAvailable(Context context);
    }
}
