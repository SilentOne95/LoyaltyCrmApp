package com.example.konta.sketch_loyalityapp.ui.couponDetails;

import com.example.konta.sketch_loyalityapp.network.Api;
import com.example.konta.sketch_loyalityapp.network.RetrofitClient;
import com.example.konta.sketch_loyalityapp.pojo.coupon.Coupon;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CouponDetailsModel implements CouponDetailsContract.Model {

    @Override
    public Disposable fetchDataFromServer(int couponId) {
        return getObservable(couponId).subscribeWith(CouponDetailsPresenter.getObserver());
    }

    private Single<Coupon> getObservable(int couponId) {
        return RetrofitClient.getInstance().create(Api.class)
                .getSingleCoupon(couponId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
