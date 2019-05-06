package com.sellger.konta.sketch_loyaltyapp.ui.couponDetails;

import com.sellger.konta.sketch_loyaltyapp.network.Api;
import com.sellger.konta.sketch_loyaltyapp.network.RetrofitClient;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Coupon;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
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
                    isCouponDataValid(coupon);
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

    @Override
    public void isCouponDataValid(Coupon coupon) {
        SimpleDateFormat newDateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("pl", "PL"));
        String formattedDate = "Not specified";

        try {
            Date date = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH).parse(coupon.getFreshTime());
            formattedDate = newDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        coupon.setFreshTime(formattedDate);

        presenter.passDataToView(coupon);
    }
}
