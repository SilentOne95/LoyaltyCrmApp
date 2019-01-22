package com.example.konta.sketch_loyalityapp.ui.couponDetails;

import com.example.konta.sketch_loyalityapp.pojo.coupon.Coupon;

import io.reactivex.disposables.Disposable;

public interface CouponDetailsContract {

    interface View {

        void setUpViewWithData(Coupon coupon);
    }

    interface Presenter {

        void requestDataFromServer(int couponId);

        String formatDateString(String dateString);
    }

    interface Model {

        Disposable fetchDataFromServer(int couponId);
    }
}
