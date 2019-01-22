package com.example.konta.sketch_loyalityapp.ui.productDetails;

import android.support.annotation.Nullable;

import com.example.konta.sketch_loyalityapp.base.BaseCallbackListener;
import com.example.konta.sketch_loyalityapp.pojo.product.Product;

public class ProductDetailsPresenter implements ProductDetailsContract.Presenter,
        BaseCallbackListener.SingleItemOnFinishListener<Product> {

    @Nullable
    private ProductDetailsContract.View view;
    private ProductDetailsContract.Model model;

    ProductDetailsPresenter(@Nullable ProductDetailsContract.View view,
                            ProductDetailsContract.Model model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void requestDataFromServer(int productId) {
        model.fetchDataFromServer(this, productId);
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
