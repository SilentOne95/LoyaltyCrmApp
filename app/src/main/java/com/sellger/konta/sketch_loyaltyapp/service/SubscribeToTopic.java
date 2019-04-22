package com.sellger.konta.sketch_loyaltyapp.service;

public interface SubscribeToTopic {

    interface NonAnonymousTopic {
        void subscribeToFirstTopic();
        void subscribeToSecondTopic();
        void subscribeToThirdTopic();
    }

    interface AnonymousTopic {
        void subscribeToAnonymousTopic();
        void unsubscribeFromAnonymousTopic();
    }

    interface UnsubscribeFromTopic {
        void unsubscribeFromFirstTopic();
        void unsubscribeFromSecondTopic();
        void unsubscribeFromThirdTopic();
    }
}
