package com.sellger.konta.sketch_loyaltyapp.ui.coupons;

import android.support.annotation.Nullable;

import com.sellger.konta.sketch_loyaltyapp.pojo.coupon.Coupon;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class CouponsPresenter implements CouponsContract.Presenter {

    @Nullable
    private CouponsContract.View view;
    private CouponsContract.Model model;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    CouponsPresenter(@Nullable CouponsContract.View view, CouponsContract.Model model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void requestDataFromServer() {
        Disposable disposable = model.fetchDataFromServer(this);
        compositeDisposable.add(disposable);
    }

    @Override
    public void passDataToAdapter(List<Coupon> couponList, int numOfColumns) {
        if (view != null) {
            view.setUpAdapter(couponList, numOfColumns);
        }
    }

    @Override
    public void hideProgressBar() {
        if (view != null) {
            view.hideProgressBar();
        }
    }
}