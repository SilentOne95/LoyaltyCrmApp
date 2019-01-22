package com.example.konta.sketch_loyalityapp.ui.map;

import android.support.annotation.NonNull;

import com.example.konta.sketch_loyalityapp.base.BaseCallbackListener;
import com.example.konta.sketch_loyalityapp.pojo.map.Marker;
import com.example.konta.sketch_loyalityapp.root.MyApplication;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapModel implements MapContract.Model {

    @Override
    public void fetchDataFromServer(final BaseCallbackListener.ListItemsOnFinishListener<Marker> onFinishedListener) {
        MyApplication.getApi().getAllMarkers().enqueue(new Callback<List<Marker>>() {
            @Override
            public void onResponse(@NonNull Call<List<Marker>> call, @NonNull Response<List<Marker>> response) {
                onFinishedListener.onFinished(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<Marker>> call, @NonNull Throwable t) {
                onFinishedListener.onFailure(t);
            }
        });
    }
}
