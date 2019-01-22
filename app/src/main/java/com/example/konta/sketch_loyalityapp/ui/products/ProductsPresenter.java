package com.example.konta.sketch_loyalityapp.ui.products;

import android.support.annotation.Nullable;

import com.example.konta.sketch_loyalityapp.pojo.product.Product;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;

public class ProductsPresenter implements ProductsContract.Presenter {

    @Nullable
    private ProductsContract.View view;
    private ProductsContract.Model model;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    ProductsPresenter(@Nullable ProductsContract.View view, ProductsContract.Model model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void requestDataFromServer() {
        Disposable disposable = model.fetchDataFromServer();
        compositeDisposable.add(disposable);
    }

    static DisposableSingleObserver<List<Product>> getObserver() {
        return new DisposableSingleObserver<List<Product>>() {
            @Override
            public void onSuccess(List<Product> products) {

            }

            @Override
            public void onError(Throwable e) {

            }
        };
    }
}
