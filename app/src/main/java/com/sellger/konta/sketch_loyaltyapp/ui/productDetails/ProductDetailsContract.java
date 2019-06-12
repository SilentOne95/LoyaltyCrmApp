package com.sellger.konta.sketch_loyaltyapp.ui.productDetails;

import com.sellger.konta.sketch_loyaltyapp.data.entity.Product;

public interface ProductDetailsContract {

    interface View {

        void initViews();

        void hideProgressBar();

        void setUpViewWithData(Product product);

        String formatPrice(float price);

        void displayToastMessage(String message);
    }

    interface Presenter {

        void requestDataFromServer(int productId);

        void hideProgressBar();

        void passDataToView(Product product);
    }
}
