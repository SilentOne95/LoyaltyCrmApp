package com.sellger.konta.sketch_loyaltyapp.ui.login.phoneAuthCode;

import androidx.annotation.NonNull;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.sellger.konta.sketch_loyaltyapp.service.pushNotification.ManageTopicsSubscriptions;

import static com.sellger.konta.sketch_loyaltyapp.Constants.ANONYMOUS_TOPIC_NAME;
import static com.sellger.konta.sketch_loyaltyapp.Constants.FIRST_TOPIC_NAME;
import static com.sellger.konta.sketch_loyaltyapp.Constants.REGISTRATION_CONVERSION;
import static com.sellger.konta.sketch_loyaltyapp.Constants.REGISTRATION_NORMAL;
import static com.sellger.konta.sketch_loyaltyapp.Constants.SECOND_TOPIC_NAME;
import static com.sellger.konta.sketch_loyaltyapp.Constants.THIRD_TOPIC_NAME;

public class LogInVerifyPresenter implements LogInVerifyContract.Presenter, ManageTopicsSubscriptions.ManageSingleTopic,
        ManageTopicsSubscriptions.ManageAllTopics {

    private static final String TAG = LogInVerifyPresenter.class.getSimpleName();

    @NonNull
    private LogInVerifyContract.View view;

    LogInVerifyPresenter(@NonNull LogInVerifyContract.View view) {
        this.view = view;
    }

    @Override
    public void manageTopicsSubscriptions(String subscriptionType) {
        String[] topicsList = new String[]{FIRST_TOPIC_NAME, SECOND_TOPIC_NAME, THIRD_TOPIC_NAME};

        switch (subscriptionType) {
            case REGISTRATION_NORMAL:
                for (String topic : topicsList) {
                    subscribeToTopic(topic);
                }
                break;
            case REGISTRATION_CONVERSION:
                for (String topic : topicsList) {
                    subscribeToTopic(topic);
                }
                unsubscribeFromTopic(ANONYMOUS_TOPIC_NAME);
                break;
        }
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
