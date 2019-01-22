package com.example.konta.sketch_loyalityapp.ui.productDetails;

import com.example.konta.sketch_loyalityapp.network.Api;
import com.example.konta.sketch_loyalityapp.network.RetrofitClient;
import com.example.konta.sketch_loyalityapp.pojo.product.Product;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ProductDetailsModel implements ProductDetailsContract.Model {

    @Override
    public Disposable fetchDataFromServer(int productId) {
        return getObservable(productId).subscribeWith(ProductDetailsPresenter.getObserver());
    }

    private Single<Product> getObservable(int productId) {
        return RetrofitClient.getInstance().create(Api.class)
                .getSingleProduct(productId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
