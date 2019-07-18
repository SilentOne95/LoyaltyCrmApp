package com.ecommercelab.loyaltyapp.ui.login.phoneAuthCode;

import androidx.annotation.NonNull;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ecommercelab.loyaltyapp.service.pushNotification.ManageTopicsSubscriptions;

import static com.ecommercelab.loyaltyapp.Constants.ANONYMOUS_TOPIC_NAME;
import static com.ecommercelab.loyaltyapp.Constants.FIRST_TOPIC_NAME;
import static com.ecommercelab.loyaltyapp.Constants.REGISTRATION_CONVERSION;
import static com.ecommercelab.loyaltyapp.Constants.REGISTRATION_NORMAL;
import static com.ecommercelab.loyaltyapp.Constants.SECOND_TOPIC_NAME;
import static com.ecommercelab.loyaltyapp.Constants.THIRD_TOPIC_NAME;

public class LogInVerifyPresenter implements LogInVerifyContract.Presenter, ManageTopicsSubscriptions.ManageSingleTopic,
        ManageTopicsSubscriptions.ManageAllTopics {

    private static final String TAG = LogInVerifyPresenter.class.getSimpleName();

    @NonNull
    private LogInVerifyContract.View view;

    LogInVerifyPresenter(@NonNull LogInVerifyContract.View view) {
        this.view = view;
    }

    /**
     * Called from {@link LogInVerifyFragment#signInWithPhoneAuthCredential(PhoneAuthCredential, boolean)}
     * and {@link LogInVerifyFragment#convertAnonymousAccount(AuthCredential, boolean)} to manage
     * push notification subscriptions.
     *
     * @param subscriptionType that should be performed based on auth method
     */
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

    /**
     * Called from {@link #manageTopicsSubscriptions(String)} to subscribe to relevant FCM topic.
     * Refer {@link ManageTopicsSubscriptions}.
     *
     * @param topic that app should subscribe to
     */
    @Override
    public void subscribeToTopic(String topic) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic);
    }

    /**
     * Called from {@link #manageTopicsSubscriptions(String)} to unsubscribe from relevant FCM topic.
     * Refer {@link ManageTopicsSubscriptions}.
     *
     * @param topic that app should unsubscribe from
     */
    @Override
    public void unsubscribeFromTopic(String topic) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
    }
}
