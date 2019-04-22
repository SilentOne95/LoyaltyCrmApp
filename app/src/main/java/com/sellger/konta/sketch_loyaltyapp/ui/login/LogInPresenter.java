package com.sellger.konta.sketch_loyaltyapp.ui.login;

import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.sellger.konta.sketch_loyaltyapp.service.SubscribeToTopic;

import static com.sellger.konta.sketch_loyaltyapp.Constants.ANONYMOUS_TOPIC_NAME;
import static com.sellger.konta.sketch_loyaltyapp.Constants.FIRST_TOPIC_NAME;
import static com.sellger.konta.sketch_loyaltyapp.Constants.SECOND_TOPIC_NAME;
import static com.sellger.konta.sketch_loyaltyapp.Constants.THIRD_TOPIC_NAME;

public class LogInPresenter implements LogInContract.Presenter, SubscribeToTopic.NonAnonymousTopic,
        SubscribeToTopic.AnonymousTopic {

    private static final String TAG = LogInPresenter.class.getSimpleName();

    @Nullable
    private LogInContract.View view;

    LogInPresenter(@Nullable LogInContract.View view) {
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
    public void subscribeToAnonymousTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic(ANONYMOUS_TOPIC_NAME).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.d(TAG, "Unsuccessfully subscribed to: anonymous");
            }
        });
    }

    @Override
    public void unsubscribeFromAnonymousTopic() {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(ANONYMOUS_TOPIC_NAME).addOnCompleteListener(task -> {
           if (!task.isSuccessful()) {
               Log.d(TAG, "Unsubscription failed: anonymous");
           }
        });
    }
}
