package com.sellger.konta.sketch_loyaltyapp.ui.couponDetails;

import com.sellger.konta.sketch_loyaltyapp.pojo.coupon.Coupon;

import io.reactivex.disposables.Disposable;

public interface CouponDetailsContract {

    interface View {

        void initViews();

        void hideProgressBar();
        void setUpViewWithData(Coupon coupon);
    }

    interface Presenter {

        void requestDataFromServer(int couponId);
        void passDataToView(Coupon coupon);
        void hideProgressBar();
    }

    interface Model {

        Disposable fetchDataFromServer(CouponDetailsPresenter presenter, int couponId);

        void isCouponDataValid(Coupon coupon);
    }
}
