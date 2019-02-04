package com.example.konta.sketch_loyalityapp.ui.products;

import com.example.konta.sketch_loyalityapp.pojo.adapter.ProductData;
import com.example.konta.sketch_loyalityapp.pojo.product.Product;

import java.util.List;

import io.reactivex.disposables.Disposable;

public interface ProductsContract {

    interface View {

        void setUpAdapter(List<Product> productList, int numOfColumns);
        void setUpEmptyStateView(boolean isNeeded);
        void setProgressBarVisibility(boolean isNeeded);
    }

    interface Presenter {

        void requestDataFromServer();
        void passDataToAdapter(List<Product> productList, int numOfColumns);
        void isProgressBarNeeded(boolean isNeeded);
    }

    interface Model {

        Disposable fetchDataFromServer(ProductsPresenter presenter);

        void formatProductsData(ProductData productData);
    }
}
