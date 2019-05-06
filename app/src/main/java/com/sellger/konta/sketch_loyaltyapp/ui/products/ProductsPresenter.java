package com.sellger.konta.sketch_loyaltyapp.ui.products;

import android.support.annotation.Nullable;

import com.sellger.konta.sketch_loyaltyapp.data.entity.Product;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

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
        Disposable disposable = model.fetchDataFromServer(this);
        compositeDisposable.add(disposable);
    }

    @Override
    public void passDataToAdapter(List<Product> productList, int numOfColumns) {
        if (view != null) {
            view.setUpAdapter(productList, numOfColumns);
        }
    }

    @Override
    public void hideProgressBar() {
        if (view != null) {
            view.hideProgressBar();
        }
    }
}
