package com.example.konta.sketch_loyalityapp.ui.coupons;

import android.support.annotation.Nullable;

import com.example.konta.sketch_loyalityapp.pojo.coupon.Coupon;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;

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
        Disposable disposable = model.fetchDataFromServer();
        compositeDisposable.add(disposable);
    }

    static DisposableSingleObserver<List<Coupon>> getObserver() {
        return new DisposableSingleObserver<List<Coupon>>() {
            @Override
            public void onSuccess(List<Coupon> coupons) {

            }

            @Override
            public void onError(Throwable e) {

            }
        };
    }
}
