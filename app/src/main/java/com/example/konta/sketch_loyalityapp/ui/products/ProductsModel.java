package com.example.konta.sketch_loyalityapp.ui.products;

import com.example.konta.sketch_loyalityapp.network.Api;
import com.example.konta.sketch_loyalityapp.network.RetrofitClient;
import com.example.konta.sketch_loyalityapp.pojo.product.Product;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ProductsModel implements ProductsContract.Model {

    @Override
    public Disposable fetchDataFromServer() {
        return getObservable().subscribeWith(ProductsPresenter.getObserver());
    }

    private Single<List<Product>> getObservable() {
        return RetrofitClient.getInstance().create(Api.class)
                .getAllProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
