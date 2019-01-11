package com.example.konta.sketch_loyalityapp.ui.productsFragment;

import com.example.konta.sketch_loyalityapp.data.product.Product;

import java.util.List;

public interface ProductsContract {

    interface View {
        void setUpAdapter(List<Product> couponList);
    }

    interface Presenter {
        void requestDataFromServer();
    }

    interface Model {

        interface OnFinishedListener {
            void onFinished(List<Product> productList);
            void onFailure(Throwable t);
        }

        void fetchDataFromServer(OnFinishedListener onFinishedListener);
    }
}
