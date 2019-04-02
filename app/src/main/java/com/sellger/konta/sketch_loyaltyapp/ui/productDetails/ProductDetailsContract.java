package com.sellger.konta.sketch_loyaltyapp.ui.productDetails;

import com.sellger.konta.sketch_loyaltyapp.pojo.product.Product;

import io.reactivex.disposables.Disposable;

public interface ProductDetailsContract {

    interface View {

        void hideProgressBar();
        void setUpViewWithData(Product product);
    }

    interface Presenter {

        void requestDataFromServer(int productId);
        void passDataToView(Product product);
        void hideProgressBar();
    }

    interface Model {

        Disposable fetchDataFromServer(ProductDetailsPresenter presenter, int productId);
    }
}
