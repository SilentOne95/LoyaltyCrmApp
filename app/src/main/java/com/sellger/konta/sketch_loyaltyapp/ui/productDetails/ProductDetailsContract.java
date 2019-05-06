package com.sellger.konta.sketch_loyaltyapp.ui.productDetails;

import com.sellger.konta.sketch_loyaltyapp.data.entity.Product;

import io.reactivex.disposables.Disposable;

public interface ProductDetailsContract {

    interface View {

        void initViews();

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
