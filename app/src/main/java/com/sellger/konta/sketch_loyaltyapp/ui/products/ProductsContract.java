package com.sellger.konta.sketch_loyaltyapp.ui.products;

import android.content.Context;

import com.sellger.konta.sketch_loyaltyapp.data.entity.Product;

import java.util.List;

public interface ProductsContract {

    interface View {

        void changeVisibilityNoNetworkConnectionView(boolean shouldBeVisible);

        void setUpAdapter(List<Product> productList, int numOfColumns);

        void changeVisibilityProgressBar(boolean shouldBeVisible);

        void displayToastMessage(String message);
    }

    interface Presenter {

        void requestDataFromServer();

        boolean isNetworkAvailable(Context context);
    }
}
