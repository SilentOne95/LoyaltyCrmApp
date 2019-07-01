package com.sellger.konta.sketch_loyaltyapp.ui.products;

import com.sellger.konta.sketch_loyaltyapp.data.entity.Product;

import java.util.List;

public interface ProductsContract {

    interface View {

        void setUpAdapter(List<Product> productList, int numOfColumns);

        void hideProgressBar();

        void displayToastMessage(String message);
    }

    interface Presenter {

        void requestDataFromServer();
    }
}
