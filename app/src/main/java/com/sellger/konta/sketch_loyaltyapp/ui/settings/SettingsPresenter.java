package com.sellger.konta.sketch_loyaltyapp.ui.settings;

import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.sellger.konta.sketch_loyaltyapp.service.SubscribeToTopic;

import static com.sellger.konta.sketch_loyaltyapp.Constants.FIRST_TOPIC_NAME;
import static com.sellger.konta.sketch_loyaltyapp.Constants.SECOND_TOPIC_NAME;
import static com.sellger.konta.sketch_loyaltyapp.Constants.THIRD_TOPIC_NAME;

public class SettingsPresenter implements SettingsContract.Presenter, SubscribeToTopic.NonAnonymousTopic,
        SubscribeToTopic.UnsubscribeFromTopic {

    private static final String TAG = SettingsPresenter.class.getSimpleName();

    @Nullable
    private SettingsContract.View view;

    SettingsPresenter(@Nullable SettingsContract.View view) {
        this.view = view;
    }

    @Override
    public void subscribeToFirstTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic(FIRST_TOPIC_NAME).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.d(TAG, "Unsuccessfully subscribed to: news");
            }
        });
    }

    @Override
    public void subscribeToSecondTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic(SECOND_TOPIC_NAME).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.d(TAG, "Unsuccessfully subscribed to: offers");
            }
        });
    }

    @Override
    public void subscribeToThirdTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic(THIRD_TOPIC_NAME).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.d(TAG, "Unsuccessfully subscribed to: discounts");
            }
        });
    }

    @Override
    public void unsubscribeFromFirstTopic() {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(FIRST_TOPIC_NAME).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.d(TAG, "Unsubscription failed: news");
            }
        });
    }

    @Override
    public void unsubscribeFromSecondTopic() {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(SECOND_TOPIC_NAME).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.d(TAG, "Unsubscription failed: offers");
            }
        });
    }

    @Override
    public void unsubscribeFromThirdTopic() {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(THIRD_TOPIC_NAME).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.d(TAG, "Unsubscription failed: discounts");
            }
        });
    }
}
