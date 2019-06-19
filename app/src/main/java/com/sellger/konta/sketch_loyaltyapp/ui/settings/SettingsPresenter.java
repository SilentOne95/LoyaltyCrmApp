package com.sellger.konta.sketch_loyaltyapp.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;

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
        FirebaseMessaging.getInstance().subscribeToTopic(topic);
    }

    @Override
    public void unsubscribeFromTopic(String topic) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
    }

    @Override
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
