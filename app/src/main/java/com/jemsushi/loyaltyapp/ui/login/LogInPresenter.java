package com.jemsushi.loyaltyapp.ui.login;

import androidx.annotation.NonNull;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.messaging.FirebaseMessaging;
import com.jemsushi.loyaltyapp.service.pushNotification.ManageTopicsSubscriptions;

import static com.jemsushi.loyaltyapp.Constants.ANONYMOUS_TOPIC_NAME;
import static com.jemsushi.loyaltyapp.Constants.FIRST_TOPIC_NAME;
import static com.jemsushi.loyaltyapp.Constants.REGISTRATION_ANONYMOUS;
import static com.jemsushi.loyaltyapp.Constants.REGISTRATION_CONVERSION;
import static com.jemsushi.loyaltyapp.Constants.REGISTRATION_NORMAL;
import static com.jemsushi.loyaltyapp.Constants.SECOND_TOPIC_NAME;
import static com.jemsushi.loyaltyapp.Constants.THIRD_TOPIC_NAME;

public class LogInPresenter implements LogInContract.Presenter, ManageTopicsSubscriptions.ManageSingleTopic,
        ManageTopicsSubscriptions.ManageAllTopics {

    private static final String TAG = LogInPresenter.class.getSimpleName();

    @NonNull
    private LogInContract.View view;

    LogInPresenter(@NonNull LogInContract.View view) {
        this.view = view;
    }

    /**
     * Called from {@link LogInFragment#signInWithCredential(AuthCredential)}, {@link LogInFragment#anonymousSignIn()}
     * and {@link LogInFragment#convertAnonymousAccount(AuthCredential)} to manage push notification
     * subscriptions.
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
            case REGISTRATION_ANONYMOUS:
                subscribeToTopic(ANONYMOUS_TOPIC_NAME);
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
