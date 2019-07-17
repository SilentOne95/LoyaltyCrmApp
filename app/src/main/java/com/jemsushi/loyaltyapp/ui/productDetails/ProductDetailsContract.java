package com.jemsushi.loyaltyapp.ui.productDetails;

import com.jemsushi.loyaltyapp.data.entity.Product;

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
