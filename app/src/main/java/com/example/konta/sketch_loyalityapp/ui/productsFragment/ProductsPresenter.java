package com.example.konta.sketch_loyalityapp.ui.productsFragment;

import com.example.konta.sketch_loyalityapp.data.product.Product;

import java.util.List;

public class ProductsPresenter implements ProductsContract.Presenter, ProductsContract.Model.OnFinishedListener {

    private ProductsContract.View view;
    private ProductsContract.Model model;

    ProductsPresenter(ProductsContract.View view, ProductsContract.Model model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void requestDataFromServer() {
        model.fetchDataFromServer(this);
    }

    @Override
    public void onFinished(List<Product> couponList) {
        view.setUpAdapter(couponList);
    }

    @Override
    public void onFailure(Throwable t) {

    }
}
