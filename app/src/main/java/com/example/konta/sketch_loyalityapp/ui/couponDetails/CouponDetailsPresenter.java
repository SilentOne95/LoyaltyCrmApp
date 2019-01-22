package com.example.konta.sketch_loyalityapp.ui.couponDetails;

import android.support.annotation.Nullable;

import com.example.konta.sketch_loyalityapp.pojo.coupon.Coupon;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;

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
        Disposable disposable = model.fetchDataFromServer(couponId);
        compositeDisposable.add(disposable);
    }

    static DisposableSingleObserver<Coupon> getObserver() {
        return new DisposableSingleObserver<Coupon>() {
            @Override
            public void onSuccess(Coupon coupon) {

            }

            @Override
            public void onError(Throwable e) {

            }
        };
    }

    @Override
    public String formatDateString(String dateString) {
        String formattedDate = null;

        try {
            Date date = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH).parse(dateString);
            formattedDate = new SimpleDateFormat("dd/MM/yyyy", new Locale("pl", "PL")).format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formattedDate;
    }
}
