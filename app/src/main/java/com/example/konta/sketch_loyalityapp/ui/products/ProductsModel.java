package com.example.konta.sketch_loyalityapp.ui.products;

import android.util.Log;

import com.example.konta.sketch_loyalityapp.network.Api;
import com.example.konta.sketch_loyalityapp.network.RetrofitClient;
import com.example.konta.sketch_loyalityapp.pojo.product.Product;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ProductsModel implements ProductsContract.Model {

    private ProductsPresenter presenter;

    @Override
    public Disposable fetchDataFromServer(ProductsPresenter presenter) {
        this.presenter = presenter;
        return getObservable().subscribeWith(getObserver());
    }

    private Single<List<Product>> getObservable() {
        return RetrofitClient.getInstance().create(Api.class)
                .getAllProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private DisposableSingleObserver<List<Product>> getObserver() {
        return new DisposableSingleObserver<List<Product>>() {
            @Override
            public void onSuccess(List<Product> productList) {
                presenter.passDataToAdapter(productList);
            }

            @Override
            public void onError(Throwable e) {
                Log.d("test", "products fail");
            }
        };
    }
}
