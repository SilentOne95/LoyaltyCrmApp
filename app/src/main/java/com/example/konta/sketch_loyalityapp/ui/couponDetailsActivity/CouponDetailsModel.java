package com.example.konta.sketch_loyalityapp.ui.couponDetailsActivity;

import android.support.annotation.NonNull;

import com.example.konta.sketch_loyalityapp.data.coupon.Coupon;
import com.example.konta.sketch_loyalityapp.root.MyApplication;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CouponDetailsModel implements CouponDetailsContract.Model {

    @Override
    public void fetchDataFromServer(final OnFinishedListener onFinishedListener, int couponId) {
        MyApplication.getApi().getSingleCoupon(couponId).enqueue(new Callback<Coupon>() {
            @Override
            public void onResponse(@NonNull Call<Coupon> call, @NonNull Response<Coupon> response) {
                onFinishedListener.onFinished(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Coupon> call, @NonNull Throwable t) {
                onFinishedListener.onFailure(t);
            }
        });
    }
}
