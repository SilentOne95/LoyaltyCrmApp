package com.sellger.konta.sketch_loyaltyapp.base.activity;

import android.content.Context;

public interface BaseActivityContract {

    interface View {

        void displaySnackbar(boolean isNetwork);
    }

    interface Presenter {

        void scheduleNetworkJob(Context context);

        void startNetworkIntentService(Context context);

        void setUpNetworkObservable();
    }
}
