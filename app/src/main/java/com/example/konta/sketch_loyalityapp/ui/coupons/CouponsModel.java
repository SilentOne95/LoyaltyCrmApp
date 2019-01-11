package com.example.konta.sketch_loyalityapp.ui.coupons;

import android.support.annotation.NonNull;

import com.example.konta.sketch_loyalityapp.data.coupon.Coupon;
import com.example.konta.sketch_loyalityapp.root.MyApplication;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CouponsModel implements CouponsContract.Model {

    @Override
    public void fetchDataFromServer(final OnFinishedListener onFinishedListener) {

        MyApplication.getApi().getAllCoupons().enqueue(new Callback<List<Coupon>>() {
            @Override
            public void onResponse(@NonNull Call<List<Coupon>> call, @NonNull Response<List<Coupon>> response) {
                onFinishedListener.onFinished(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<Coupon>> call, @NonNull Throwable t) {
                onFinishedListener.onFailure(t);
            }
        });
    }
}
