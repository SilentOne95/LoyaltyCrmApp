package com.sellger.konta.sketch_loyaltyapp.ui.products;

import com.sellger.konta.sketch_loyaltyapp.pojo.adapter.ProductData;
import com.sellger.konta.sketch_loyaltyapp.pojo.product.Product;

import java.util.List;

import io.reactivex.disposables.Disposable;

public interface ProductsContract {

    interface View {

        void setUpAdapter(List<Product> productList, int numOfColumns);
        void setUpEmptyStateView(boolean isNeeded);
        void hideProgressBar();
    }

    interface Presenter {

        void requestDataFromServer();
        void passDataToAdapter(List<Product> productList, int numOfColumns);
        void hideProgressBar();
    }

    interface Model {

        Disposable fetchDataFromServer(ProductsPresenter presenter);

        void formatProductsData(ProductData productData);
    }
}
