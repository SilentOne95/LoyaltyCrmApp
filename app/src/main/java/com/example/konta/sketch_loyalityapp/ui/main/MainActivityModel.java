package com.example.konta.sketch_loyalityapp.ui.main;

import android.support.annotation.NonNull;

import com.example.konta.sketch_loyalityapp.data.menu.MenuComponent;
import com.example.konta.sketch_loyalityapp.root.MyApplication;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityModel implements MainActivityContract.Model {

    @Override
    public void fetchDataFromServer(final OnFinishedListener onFinishedListener) {

        MyApplication.getApi().getMenuComponents().enqueue(new Callback<List<MenuComponent>>() {
            @Override
            public void onResponse(@NonNull Call<List<MenuComponent>> call, @NonNull Response<List<MenuComponent>> response) {
                onFinishedListener.onFinished(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<MenuComponent>> call, @NonNull Throwable t) {
                onFinishedListener.onFailure(t);
            }
        });
    }
}
