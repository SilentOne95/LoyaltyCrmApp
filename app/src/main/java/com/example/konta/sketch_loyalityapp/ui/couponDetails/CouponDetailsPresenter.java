package com.example.konta.sketch_loyalityapp.ui.couponDetails;

import android.support.annotation.Nullable;

import com.example.konta.sketch_loyalityapp.pojo.coupon.Coupon;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class CouponDetailsPresenter implements CouponDetailsContract.Presenter {

    @Nullable
    private CouponDetailsContract.View view;
    private CouponDetailsContract.Model model;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    CouponDetailsPresenter(@Nullable CouponDetailsContract.View view,
                           CouponDetailsContract.Model model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void requestDataFromServer(int couponId) {
        Disposable disposable = model.fetchDataFromServer(this, couponId);
        compositeDisposable.add(disposable);
    }

    @Override
    public void passDataToView(Coupon coupon) {
        if (view != null) {
            view.setUpViewWithData(coupon);
        }
    }

    @Override
    public void hideProgressBar() {
        if (view != null) {
            view.hideProgressBar();
        }
    }
}
