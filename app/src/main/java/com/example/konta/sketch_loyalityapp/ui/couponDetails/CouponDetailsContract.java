package com.example.konta.sketch_loyalityapp.ui.couponDetails;

import com.example.konta.sketch_loyalityapp.pojo.coupon.Coupon;

import io.reactivex.disposables.Disposable;

public interface CouponDetailsContract {

    interface View {

        void hideProgressBar();
        void setUpViewWithData(Coupon coupon);
    }

    interface Presenter {

        void requestDataFromServer(int couponId);
        void passDataToView(Coupon coupon);
        void hideProgressBar();

        String formatDateString(String dateString);
    }

    interface Model {

        Disposable fetchDataFromServer(CouponDetailsPresenter presenter, int couponId);
    }
}
