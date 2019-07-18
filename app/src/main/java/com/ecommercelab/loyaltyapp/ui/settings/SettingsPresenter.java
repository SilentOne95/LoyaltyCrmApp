package com.ecommercelab.loyaltyapp.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessaging;
import com.ecommercelab.loyaltyapp.service.pushNotification.ManageTopicsSubscriptions;

public class SettingsPresenter implements SettingsContract.Presenter, ManageTopicsSubscriptions.ManageSingleTopic {

    private static final String TAG = SettingsPresenter.class.getSimpleName();

    @NonNull
    private SettingsContract.View view;

    SettingsPresenter(@NonNull SettingsContract.View view) {
        this.view = view;
    }

    /**
     * Called from {@link SettingsActivity#onCheckedChanged(CompoundButton, boolean)} save button state
     * and un- / subscribe accordingly depends on button state.
     *
     * @param preferences instance
     * @param name        of push notification topic
     * @param state       of the switch button
     */
    @Override
    public void saveSwitchState(SharedPreferences preferences, String name, boolean state) {
        preferences.edit().putBoolean(name, state).apply();
        if (state) {
            subscribeToTopic(name);
        } else {
            unsubscribeFromTopic(name);
        }
    }

    /**
     * Called from {@link SettingsActivity#onCheckedChanged(CompoundButton, boolean)} to get current
     * button state.
     *
     * @param preferences instance
     * @param name        of push notification topic
     * @return true / false depends on whether button should be active or not
     */
    @Override
    public boolean getSwitchState(SharedPreferences preferences, String name) {
        return preferences.getBoolean(name, true);
    }

    /**
     * Called from {@link #saveSwitchState(SharedPreferences, String, boolean)} to subscribe to relevant FCM topic.
     * Refer {@link ManageTopicsSubscriptions}.
     *
     * @param topic that app should subscribe to
     */
    @Override
    public void subscribeToTopic(String topic) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic);
    }

    /**
     * Called from {@link #saveSwitchState(SharedPreferences, String, boolean)} to unsubscribe from relevant FCM topic.
     * Refer {@link ManageTopicsSubscriptions}.
     *
     * @param topic that app should unsubscribe from
     */
    @Override
    public void unsubscribeFromTopic(String topic) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
    }

    /**
     * Called from {@link SettingsActivity#onCheckedChanged(CompoundButton, boolean)} to check
     * if network connection is active.
     *
     * @param context of the app
     * @return boolean depends on network status
     */
    @Override
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
