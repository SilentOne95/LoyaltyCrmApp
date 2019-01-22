package com.example.konta.sketch_loyalityapp.ui.map;

import com.example.konta.sketch_loyalityapp.network.Api;
import com.example.konta.sketch_loyalityapp.network.RetrofitClient;
import com.example.konta.sketch_loyalityapp.pojo.map.Marker;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MapModel implements MapContract.Model {

    @Override
    public Disposable fetchDataFromServer() {
        return getObservable().subscribeWith(MapPresenter.getObserver());
    }

    private Single<List<Marker>> getObservable() {
        return RetrofitClient.getInstance().create(Api.class)
                .getAllMarkers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
