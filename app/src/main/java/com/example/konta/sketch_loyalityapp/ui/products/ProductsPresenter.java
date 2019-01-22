package com.example.konta.sketch_loyalityapp.ui.products;

import android.support.annotation.Nullable;

import com.example.konta.sketch_loyalityapp.base.BaseCallbackListener;
import com.example.konta.sketch_loyalityapp.pojo.product.Product;

import java.util.List;

public class ProductsPresenter implements ProductsContract.Presenter,
        BaseCallbackListener.ListItemsOnFinishListener<Product> {

    @Nullable
    private ProductsContract.View view;
    private ProductsContract.Model model;

    ProductsPresenter(@Nullable ProductsContract.View view, ProductsContract.Model model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void requestDataFromServer() {
        model.fetchDataFromServer(this);
    }

    @Override
    public void onFinished(List<Product> productList) {
        if (view != null) {
            view.setUpAdapter(productList);
        }
    }

    @Override
    public void onFailure(Throwable t) {

    }
}
