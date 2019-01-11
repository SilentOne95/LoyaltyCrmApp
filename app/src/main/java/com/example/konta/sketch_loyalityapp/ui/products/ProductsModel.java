package com.example.konta.sketch_loyalityapp.ui.products;

import android.support.annotation.NonNull;

import com.example.konta.sketch_loyalityapp.data.product.Product;
import com.example.konta.sketch_loyalityapp.root.MyApplication;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsModel implements ProductsContract.Model {

    @Override
    public void fetchDataFromServer(final OnFinishedListener onFinishedListener) {
        MyApplication.getApi().getAllProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                onFinishedListener.onFinished(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                onFinishedListener.onFailure(t);
            }
        });
    }
}
