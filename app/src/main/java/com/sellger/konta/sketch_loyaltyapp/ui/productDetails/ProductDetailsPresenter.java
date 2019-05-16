package com.sellger.konta.sketch_loyaltyapp.ui.productDetails;

import android.support.annotation.NonNull;

import com.sellger.konta.sketch_loyaltyapp.data.entity.Product;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class ProductDetailsPresenter implements ProductDetailsContract.Presenter {

    @NonNull
    private ProductDetailsContract.View view;
    private ProductDetailsContract.Model model;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    ProductDetailsPresenter(@NonNull ProductDetailsContract.View view,
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
        view.setUpViewWithData(product);
    }

    @Override
    public void hideProgressBar() {
        view.hideProgressBar();
    }
}
