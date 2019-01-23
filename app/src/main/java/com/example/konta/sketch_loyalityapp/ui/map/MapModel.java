package com.example.konta.sketch_loyalityapp.ui.map;

import com.example.konta.sketch_loyalityapp.network.Api;
import com.example.konta.sketch_loyalityapp.network.RetrofitClient;
import com.example.konta.sketch_loyalityapp.pojo.map.Marker;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MapModel implements MapContract.Model {

    private MapPresenter presenter;

    @Override
    public Disposable fetchDataFromServer(MapPresenter presenter) {
        this.presenter = presenter;
        return getObservable().subscribeWith(getObserver());
    }

    private Single<List<Marker>> getObservable() {
        return RetrofitClient.getInstance().create(Api.class)
                .getAllMarkers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private DisposableSingleObserver<List<Marker>> getObserver() {
        return new DisposableSingleObserver<List<Marker>>() {
            @Override
            public void onSuccess(List<Marker> markerList) {
                presenter.passDataToCluster(markerList);
            }

            @Override
            public void onError(Throwable e) { }
        };
    }
}
