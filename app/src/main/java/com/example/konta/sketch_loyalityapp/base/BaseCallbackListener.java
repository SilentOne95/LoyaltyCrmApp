package com.example.konta.sketch_loyalityapp.base;

import java.util.List;

public interface BaseCallbackListener {

    interface ListItemsOnFinishListener<T> {
        void onFinished(List<T> listOfItems);
        void onFailure(Throwable throwable);
    }

    interface SingleItemOnFinishListener<T> {
        void onFinished(T listItem);
        void onFailure(Throwable throwable);
    }
}
