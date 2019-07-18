package com.ecommercelab.loyaltyapp.service.pushNotification;

public interface ManageTopicsSubscriptions {

    interface ManageSingleTopic {

        void subscribeToTopic(String topic);

        void unsubscribeFromTopic(String topic);
    }

    interface ManageAllTopics {

        void manageTopicsSubscriptions(String subscriptionType);
    }
}
