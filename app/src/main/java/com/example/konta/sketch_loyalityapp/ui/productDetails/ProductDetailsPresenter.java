package com.example.konta.sketch_loyalityapp.ui.productDetails;

import android.support.annotation.Nullable;

import com.example.konta.sketch_loyalityapp.base.BaseCallbackListener;
import com.example.konta.sketch_loyalityapp.pojo.product.Product;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;

public class ProductDetailsPresenter implements ProductDetailsContract.Presenter,
        BaseCallbackListener.SingleItemOnFinishListener<Product> {

    @Nullable
    private ProductDetailsContract.View view;
    private ProductDetailsContract.Model model;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    ProductDetailsPresenter(@Nullable ProductDetailsContract.View view,
                            ProductDetailsContract.Model model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void requestDataFromServer(int productId) {
        Disposable disposable = model.fetchDataFromServer(productId);
        compositeDisposable.add(disposable);
    }

    static DisposableSingleObserver<Product> getObserver() {
        return new DisposableSingleObserver<Product>() {
            @Override
            public void onSuccess(Product product) {

            }

            @Override
            public void onError(Throwable e) {

            }
        };
    }

    @Override
    public void onFinished(Product product) {
        if (view != null) {
            view.setUpViewWithData(product);
        }
    }

    @Override
    public void onFailure(Throwable t) {

    }
}
