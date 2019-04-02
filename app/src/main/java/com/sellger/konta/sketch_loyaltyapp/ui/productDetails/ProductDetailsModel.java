package com.sellger.konta.sketch_loyaltyapp.ui.productDetails;

import com.sellger.konta.sketch_loyaltyapp.network.Api;
import com.sellger.konta.sketch_loyaltyapp.network.RetrofitClient;
import com.sellger.konta.sketch_loyaltyapp.pojo.product.Product;

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
