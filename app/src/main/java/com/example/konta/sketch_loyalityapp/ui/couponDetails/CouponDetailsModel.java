package com.example.konta.sketch_loyalityapp.ui.couponDetails;

import com.example.konta.sketch_loyalityapp.network.Api;
import com.example.konta.sketch_loyalityapp.network.RetrofitClient;
import com.example.konta.sketch_loyalityapp.pojo.coupon.Coupon;

import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CouponDetailsModel implements CouponDetailsContract.Model {

    private CouponDetailsPresenter presenter;

    @Override
    public Disposable fetchDataFromServer(CouponDetailsPresenter presenter, int couponId) {
        this.presenter = presenter;

        return getObservableTimer(couponId)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(coupon -> {
                    presenter.hideProgressBar();
                    presenter.passDataToView(coupon);
                }, throwable -> presenter.hideProgressBar());
    }

    private Single<Coupon> getObservableTimer(int couponId) {
        return Single.zip(getObservable(couponId), Single.timer(1000, TimeUnit.MILLISECONDS),
                ((coupon, timer) -> coupon));
    }

    private Single<Coupon> getObservable(int couponId) {
        return RetrofitClient.getInstance().create(Api.class)
                .getSingleCoupon(couponId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
