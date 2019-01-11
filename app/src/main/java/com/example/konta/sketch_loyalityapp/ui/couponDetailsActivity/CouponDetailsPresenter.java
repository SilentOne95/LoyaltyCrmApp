package com.example.konta.sketch_loyalityapp.ui.couponDetailsActivity;

import android.support.annotation.Nullable;

import com.example.konta.sketch_loyalityapp.data.coupon.Coupon;

public class CouponDetailsPresenter implements CouponDetailsContract.Presenter, CouponDetailsContract.Model.OnFinishedListener {

    @Nullable
    private CouponDetailsContract.View view;
    private CouponDetailsContract.Model model;

    CouponDetailsPresenter(@Nullable CouponDetailsContract.View view,
                           CouponDetailsContract.Model model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void requestDataFromServer(int couponId) {
        model.fetchDataFromServer(this, couponId);
    }

    @Override
    public void onFinished(Coupon coupon) {
        if (view != null) {
            view.setUpViewsWithData(coupon);
        }
    }

    @Override
    public void onFailure(Throwable t) {

    }
}
