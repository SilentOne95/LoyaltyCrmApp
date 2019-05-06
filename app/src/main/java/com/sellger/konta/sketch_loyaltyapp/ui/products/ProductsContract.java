package com.sellger.konta.sketch_loyaltyapp.ui.products;

import com.sellger.konta.sketch_loyaltyapp.data.utils.ProductData;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Product;

import java.util.List;

import io.reactivex.disposables.Disposable;

public interface ProductsContract {

    interface View {

        void initViews();

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
