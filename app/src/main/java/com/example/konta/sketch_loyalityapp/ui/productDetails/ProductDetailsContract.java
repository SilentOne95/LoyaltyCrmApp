package com.example.konta.sketch_loyalityapp.ui.productDetails;

import com.example.konta.sketch_loyalityapp.base.BaseCallbackListener;
import com.example.konta.sketch_loyalityapp.pojo.product.Product;

public interface ProductDetailsContract {

    interface View {
        void setUpViewWithData(Product product);
    }

    interface Presenter {
        void requestDataFromServer(int productId);
    }

    interface Model {

        void fetchDataFromServer(BaseCallbackListener.SingleItemOnFinishListener<Product> onFinishedListener,
                                 int productId);
    }
}
