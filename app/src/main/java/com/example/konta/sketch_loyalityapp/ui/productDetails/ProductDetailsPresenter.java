package com.example.konta.sketch_loyalityapp.ui.productDetails;

import android.support.annotation.Nullable;

import com.example.konta.sketch_loyalityapp.pojo.product.Product;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class ProductDetailsPresenter implements ProductDetailsContract.Presenter {

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
        Disposable disposable = model.fetchDataFromServer(this, productId);
        compositeDisposable.add(disposable);
    }

    @Override
    public void passDataToView(Product product) {
        if (view != null) {
            view.setUpViewWithData(product);
        }
    }

    @Override
    public void hideProgressBar() {
        if (view != null) {
            view.hideProgressBar();
        }
    }
}
