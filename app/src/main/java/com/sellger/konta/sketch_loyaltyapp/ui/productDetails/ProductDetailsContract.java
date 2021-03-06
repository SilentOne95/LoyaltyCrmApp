package com.sellger.konta.sketch_loyaltyapp.ui.productDetails;

import com.sellger.konta.sketch_loyaltyapp.data.entity.Product;

public interface ProductDetailsContract {

    interface View {

        void hideProgressBar();

        void setUpViewWithData(Product product);

        void displayToastMessage(String message);
    }

    interface Presenter {

        void requestDataFromServer(int productId);
    }
}
