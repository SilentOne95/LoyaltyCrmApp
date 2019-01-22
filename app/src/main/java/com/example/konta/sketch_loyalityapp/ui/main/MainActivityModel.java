package com.example.konta.sketch_loyalityapp.ui.main;

import com.example.konta.sketch_loyalityapp.network.RetrofitClient;
import com.example.konta.sketch_loyalityapp.pojo.menu.MenuComponent;
import com.example.konta.sketch_loyalityapp.network.Api;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivityModel implements MainActivityContract.Model {

    @Override
    public Disposable fetchDataFromServer() {

        return getObservable().subscribeWith(MainActivityPresenter.getObserver());
    }

    private Single<List<MenuComponent>> getObservable() {
        return RetrofitClient.getInstance().create(Api.class)
                .getMenuComponents()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
