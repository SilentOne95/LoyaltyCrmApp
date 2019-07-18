package com.ecommercelab.loyaltyapp.ui.coupons;

import android.content.Context;

import com.ecommercelab.loyaltyapp.data.entity.Coupon;

import java.util.List;

public interface CouponsContract {

    interface View {

        void changeVisibilityNoNetworkConnectionView(boolean shouldBeVisible);

        void setUpAdapter(List<Coupon> couponList, int numOfColumns);

        void changeVisibilityProgressBar(boolean shouldBeVisible);

        void displayToastMessage(String message);
    }

    interface Presenter {

        void requestDataFromServer(Context context);

        boolean isNetworkAvailable(Context context);
    }
}
