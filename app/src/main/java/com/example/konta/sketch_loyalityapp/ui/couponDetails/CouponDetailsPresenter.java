package com.example.konta.sketch_loyalityapp.ui.couponDetails;

import android.support.annotation.Nullable;

import com.example.konta.sketch_loyalityapp.data.coupon.Coupon;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CouponDetailsPresenter implements CouponDetailsContract.Presenter, CouponDetailsContract.Model.OnFinishedListener {

    @Nullable
    private CouponDetailsContract.View view;
    private CouponDetailsContract.Model model;

    CouponDetailsPresenter(@Nullable CouponDetailsContract.View view,
                           CouponDetailsContract.Model model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void requestDataFromServer(int couponId) {
        model.fetchDataFromServer(this, couponId);
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

    @Override
    public void onFinished(Coupon coupon) {
        if (view != null) {
            view.setUpViewWithData(coupon);
        }
    }

    @Override
    public void onFailure(Throwable t) {

    }
}
