package com.example.konta.sketch_loyalityapp.ui.products;

import com.example.konta.sketch_loyalityapp.base.BaseCallbackListener;
import com.example.konta.sketch_loyalityapp.pojo.product.Product;

import java.util.List;

public interface ProductsContract {

    interface View {
        void setUpAdapter(List<Product> couponList);
    }

    interface Presenter {
        void requestDataFromServer();
    }

    interface Model {

        void fetchDataFromServer(BaseCallbackListener.ListItemsOnFinishListener<Product> onFinishedListener);
    }
}
