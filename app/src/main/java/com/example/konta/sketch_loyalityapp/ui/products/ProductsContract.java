package com.example.konta.sketch_loyalityapp.ui.products;

import com.example.konta.sketch_loyalityapp.pojo.product.Product;

import java.util.List;

import io.reactivex.disposables.Disposable;

public interface ProductsContract {

    interface View {
        void setUpAdapter(List<Product> couponList);
    }

    interface Presenter {
        void requestDataFromServer();
    }

    interface Model {

        Disposable fetchDataFromServer();
    }
}
