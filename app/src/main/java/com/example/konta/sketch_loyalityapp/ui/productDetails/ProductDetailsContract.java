package com.example.konta.sketch_loyalityapp.ui.productDetails;

import com.example.konta.sketch_loyalityapp.pojo.product.Product;

import io.reactivex.disposables.Disposable;

public interface ProductDetailsContract {

    interface View {

        void setUpViewWithData(Product product);
    }

    interface Presenter {

        void requestDataFromServer(int productId);
        void passDataToView(Product product);
    }

    interface Model {

        Disposable fetchDataFromServer(ProductDetailsPresenter presenter, int productId);
    }
}
