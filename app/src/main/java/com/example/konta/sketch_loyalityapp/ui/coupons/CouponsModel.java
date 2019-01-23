package com.example.konta.sketch_loyalityapp.ui.coupons;

import com.example.konta.sketch_loyalityapp.network.Api;
import com.example.konta.sketch_loyalityapp.network.RetrofitClient;
import com.example.konta.sketch_loyalityapp.pojo.coupon.Coupon;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class CouponsModel implements CouponsContract.Model {

    private CouponsPresenter presenter;

    @Override
    public Disposable fetchDataFromServer(CouponsPresenter presenter) {
        this.presenter = presenter;
        return getObservable().subscribeWith(getObserver());
    }

    private Single<List<Coupon>> getObservable() {
        return RetrofitClient.getInstance().create(Api.class)
                .getAllCoupons()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private DisposableSingleObserver<List<Coupon>> getObserver() {
        return new DisposableSingleObserver<List<Coupon>>() {
            @Override
            public void onSuccess(List<Coupon> couponList) {
                presenter.passDataToAdapter(couponList);
            }

            @Override
            public void onError(Throwable e) { }
        };
    }
}
