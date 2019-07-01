package com.sellger.konta.sketch_loyaltyapp.ui.couponDetails;

import com.sellger.konta.sketch_loyaltyapp.data.entity.Coupon;

public interface CouponDetailsContract {

    interface View {

        void setUpViewWithData(Coupon coupon);

        void hideProgressBar();

        void displayToastMessage(String message);
    }

    interface Presenter {

        void requestDataFromServer(int couponId);
    }
}
