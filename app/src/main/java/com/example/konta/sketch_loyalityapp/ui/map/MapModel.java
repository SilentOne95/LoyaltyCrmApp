package com.example.konta.sketch_loyalityapp.ui.map;

import com.example.konta.sketch_loyalityapp.network.Api;
import com.example.konta.sketch_loyalityapp.network.RetrofitClient;
import com.example.konta.sketch_loyalityapp.pojo.map.Marker;
import com.example.konta.sketch_loyalityapp.ui.map.bottomSheet.contactInfo.ContactInfoPresenter;
import com.example.konta.sketch_loyalityapp.ui.map.bottomSheet.openingHours.OpeningHoursPresenter;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MapModel implements MapContract.Model {

    private MapPresenter presenter;
    private OpeningHoursPresenter openingHoursPresenter;
    private ContactInfoPresenter contactInfoPresenter;

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

    private DisposableSingleObserver<List<Marker>> getObserverOpening() {
        return new DisposableSingleObserver<List<Marker>>() {
            @Override
            public void onSuccess(List<Marker> markerList) {
                openingHoursPresenter.formatOpenHoursData(markerList);
            }

            @Override
            public void onError(Throwable e) { }
        };
    }

    private DisposableSingleObserver<List<Marker>> getObserverContact() {
        return new DisposableSingleObserver<List<Marker>>() {
            @Override
            public void onSuccess(List<Marker> markerList) {
                contactInfoPresenter.formatContactInfoData(markerList);
            }

            @Override
            public void onError(Throwable e) { }
        };
    }

    @Override
    public Disposable fetchDataFromServer(OpeningHoursPresenter openingHoursPresenter) {
        this.openingHoursPresenter = openingHoursPresenter;
        return getObservable().subscribeWith(getObserverOpening());
    }

    @Override
    public Disposable fetchDataFromServer(ContactInfoPresenter contactInfoPresenter) {
        this.contactInfoPresenter = contactInfoPresenter;
        return getObservable().subscribeWith(getObserverContact());
    }
}