package com.sellger.konta.sketch_loyaltyapp.ui.settings;

import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.sellger.konta.sketch_loyaltyapp.service.pushNotification.ManageTopicsSubscriptions;

public class SettingsPresenter implements SettingsContract.Presenter, ManageTopicsSubscriptions.ManageSingleTopic {

    private static final String TAG = SettingsPresenter.class.getSimpleName();

    @NonNull
    private SettingsContract.View view;

    SettingsPresenter(@NonNull SettingsContract.View view) {
        this.view = view;
    }

    @Override
    public void saveSwitchState(SharedPreferences preferences, String name, boolean state) {
        preferences.edit().putBoolean(name, state).apply();
        if (state) {
            subscribeToTopic(name);
        } else {
            unsubscribeFromTopic(name);
        }
    }

    @Override
    public boolean getSwitchState(SharedPreferences preferences, String name) {
        return preferences.getBoolean(name, true);
    }

    @Override
    public void subscribeToTopic(String topic) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.d(TAG, "Subscribe failed: " + topic);
            }
        });
    }

    @Override
    public void unsubscribeFromTopic(String topic) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.d(TAG, "Unsubscribe failed: " + topic);
            }
        });
    }
}
