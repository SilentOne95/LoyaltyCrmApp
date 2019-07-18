package com.ecommercelab.loyaltyapp.ui.productDetails;

import com.ecommercelab.loyaltyapp.data.entity.Product;

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
