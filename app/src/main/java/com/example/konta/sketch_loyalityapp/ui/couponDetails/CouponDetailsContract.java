package com.example.konta.sketch_loyalityapp.ui.couponDetails;

import com.example.konta.sketch_loyalityapp.data.coupon.Coupon;

public interface CouponDetailsContract {

    interface View {
        void setUpViewWithData(Coupon coupon);
    }

    interface Presenter {
        void requestDataFromServer(int couponId);
    }

    interface Model {

        interface OnFinishedListener {
            void onFinished(Coupon coupon);
            void onFailure(Throwable t);
        }

        void fetchDataFromServer(OnFinishedListener onFinishedListener, int couponId);
    }
}
