package com.ecommercelab.loyaltyapp.ui.products;

import android.content.Context;

import com.ecommercelab.loyaltyapp.data.entity.Product;

import java.util.List;

public interface ProductsContract {

    interface View {

        void changeVisibilityNoNetworkConnectionView(boolean shouldBeVisible);

        void setUpAdapter(List<Product> productList, int numOfColumns);

        void changeVisibilityProgressBar(boolean shouldBeVisible);

        void displayToastMessage(String message);
    }

    interface Presenter {

        void requestDataFromServer(Context context);

        boolean isNetworkAvailable(Context context);
    }
}
