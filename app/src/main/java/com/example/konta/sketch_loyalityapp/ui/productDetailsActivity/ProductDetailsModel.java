package com.example.konta.sketch_loyalityapp.ui.productDetailsActivity;

import android.support.annotation.NonNull;

import com.example.konta.sketch_loyalityapp.data.product.Product;
import com.example.konta.sketch_loyalityapp.root.MyApplication;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailsModel implements ProductDetailsContract.Model {

    @Override
    public void fetchDataFromServer(final OnFinishedListener onFinishedListener, int productId) {
        MyApplication.getApi().getSingleProduct(productId).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(@NonNull Call<Product> call, @NonNull Response<Product> response) {
                onFinishedListener.onFinished(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Product> call, @NonNull Throwable t) {
                onFinishedListener.onFailure(t);
            }
        });
    }
}
