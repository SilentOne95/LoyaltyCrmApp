package com.example.konta.sketch_loyalityapp.ui.couponDetails;

import com.example.konta.sketch_loyalityapp.network.Api;
import com.example.konta.sketch_loyalityapp.network.RetrofitClient;
import com.example.konta.sketch_loyalityapp.pojo.coupon.Coupon;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class CouponDetailsModel implements CouponDetailsContract.Model {

    private CouponDetailsPresenter presenter;

    @Override
    public Disposable fetchDataFromServer(CouponDetailsPresenter presenter, int couponId) {
        this.presenter = presenter;
        return getObservable(couponId).subscribeWith(getObserver());
    }

    private Single<Coupon> getObservable(int couponId) {
        return RetrofitClient.getInstance().create(Api.class)
                .getSingleCoupon(couponId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private DisposableSingleObserver<Coupon> getObserver() {
        return new DisposableSingleObserver<Coupon>() {
            @Override
            public void onSuccess(Coupon coupon) {
                presenter.passDataToView(coupon);
            }

            @Override
            public void onError(Throwable e) { }
        };
    }
}
