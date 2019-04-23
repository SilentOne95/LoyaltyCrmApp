package com.sellger.konta.sketch_loyaltyapp.ui.login;

import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.sellger.konta.sketch_loyaltyapp.service.ManageTopicsSubscriptions;

public class LogInPresenter implements LogInContract.Presenter, ManageTopicsSubscriptions {

    private static final String TAG = LogInPresenter.class.getSimpleName();

    @Nullable
    private LogInContract.View view;

    LogInPresenter(@Nullable LogInContract.View view) {
        this.view = view;
    }

    @Override
    public void subscribeToTopic(String topic) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic).addOnCompleteListener(task -> {
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
