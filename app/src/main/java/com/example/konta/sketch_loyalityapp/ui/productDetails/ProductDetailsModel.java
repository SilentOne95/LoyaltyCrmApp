package com.example.konta.sketch_loyalityapp.ui.productDetails;

import com.example.konta.sketch_loyalityapp.network.Api;
import com.example.konta.sketch_loyalityapp.network.RetrofitClient;
import com.example.konta.sketch_loyalityapp.pojo.product.Product;

import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ProductDetailsModel implements ProductDetailsContract.Model {

    private ProductDetailsPresenter presenter;

    @Override
    public Disposable fetchDataFromServer(ProductDetailsPresenter presenter, int productId) {
        this.presenter = presenter;

        return getObservableTimer(productId)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(product -> {
                    presenter.hideProgressBar();
                    presenter.passDataToView(product);
                }, throwable -> presenter.hideProgressBar());
    }

    private Single<Product> getObservableTimer(int productId) {
        return Single.zip(getObservable(productId), Single.timer(1000, TimeUnit.MILLISECONDS),
                ((product, timer) -> product));
    }

    private Single<Product> getObservable(int productId) {
        return RetrofitClient.getInstance().create(Api.class)
                .getSingleProduct(productId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
