package com.example.konta.sketch_loyalityapp.ui.productDetails;

import com.example.konta.sketch_loyalityapp.network.Api;
import com.example.konta.sketch_loyalityapp.network.RetrofitClient;
import com.example.konta.sketch_loyalityapp.pojo.product.Product;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ProductDetailsModel implements ProductDetailsContract.Model {

    private ProductDetailsPresenter presenter;

    @Override
    public Disposable fetchDataFromServer(ProductDetailsPresenter presenter, int productId) {
        this.presenter = presenter;
        return getObservable(productId).subscribeWith(getObserver());
    }

    private Single<Product> getObservable(int productId) {
        return RetrofitClient.getInstance().create(Api.class)
                .getSingleProduct(productId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private DisposableSingleObserver<Product> getObserver() {
        return new DisposableSingleObserver<Product>() {
            @Override
            public void onSuccess(Product product) {
                presenter.passDataToView(product);
            }

            @Override
            public void onError(Throwable e) { }
        };
    }
}
