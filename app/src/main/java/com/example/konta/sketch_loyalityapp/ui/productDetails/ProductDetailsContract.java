package com.example.konta.sketch_loyalityapp.ui.productDetails;

import com.example.konta.sketch_loyalityapp.data.product.Product;

public interface ProductDetailsContract {

    interface View {
        void setUpViewWithData(Product product);
    }

    interface Presenter {
        void requestDataFromServer(int productId);
    }

    interface Model {

        interface OnFinishedListener {
            void onFinished(Product product);
            void onFailure(Throwable t);
        }

        void fetchDataFromServer(OnFinishedListener onFinishedListener, int productId);
    }
}
