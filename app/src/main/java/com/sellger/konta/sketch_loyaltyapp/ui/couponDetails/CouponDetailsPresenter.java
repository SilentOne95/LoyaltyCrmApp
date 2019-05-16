package com.sellger.konta.sketch_loyaltyapp.ui.couponDetails;

import android.support.annotation.NonNull;

import com.sellger.konta.sketch_loyaltyapp.data.entity.Coupon;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class CouponDetailsPresenter implements CouponDetailsContract.Presenter {

    @NonNull
    private CouponDetailsContract.View view;
    private CouponDetailsContract.Model model;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    CouponDetailsPresenter(@NonNull CouponDetailsContract.View view,
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
        view.setUpViewWithData(coupon);
    }

    @Override
    public void hideProgressBar() {
        view.hideProgressBar();
    }
}
