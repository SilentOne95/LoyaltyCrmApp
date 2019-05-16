package com.sellger.konta.sketch_loyaltyapp.ui.products;

import android.support.annotation.NonNull;

import com.sellger.konta.sketch_loyaltyapp.data.LoyaltyDataSource;
import com.sellger.konta.sketch_loyaltyapp.data.LoyaltyRepository;
import com.sellger.konta.sketch_loyaltyapp.data.entity.MenuComponent;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Product;

import java.util.List;

import static com.sellger.konta.sketch_loyaltyapp.Constants.DEFAULT_NUM_OF_COLUMNS;
import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_TYPE_PRODUCTS;

public class ProductsPresenter implements ProductsContract.Presenter {

    @NonNull
    private ProductsContract.View view;

    @NonNull
    private LoyaltyRepository loyaltyRepository;

    ProductsPresenter(@NonNull ProductsContract.View view, @NonNull LoyaltyRepository loyaltyRepository) {
        this.view = view;
        this.loyaltyRepository = loyaltyRepository;
    }

    @Override
    public void requestDataFromServer() {
        loyaltyRepository.getAllProducts(new LoyaltyDataSource.LoadDataCallback() {
            @Override
            public void onDataLoaded(List<?> data) {
                List<Product> productList = (List<Product>) data;

                loyaltyRepository.getMenu(new LoyaltyDataSource.LoadDataCallback() {
                    @Override
                    public void onDataLoaded(List<?> data) {
                        for (Object object : data) {
                            if (((MenuComponent) object).getType().equals(LAYOUT_TYPE_PRODUCTS)) {
                                refactorFetchedData(productList, ((MenuComponent) object).getNumberOfColumns());
                            }
                        }
                    }

                    @Override
                    public void onDataNotAvailable() {
                        hideProgressBar();
                    }
                });
            }

            @Override
            public void onDataNotAvailable() {
                hideProgressBar();
            }
        });
    }

    @Override
    public void refactorFetchedData(List<Product> productList, int numOfColumns) {
        if (numOfColumns < 1 || numOfColumns > 3) {
            numOfColumns = DEFAULT_NUM_OF_COLUMNS;
        }

        hideProgressBar();
        passDataToAdapter(productList, numOfColumns);
    }

    @Override
    public void hideProgressBar() {
        view.hideProgressBar();
    }

    @Override
    public void passDataToAdapter(List<Product> productList, int numOfColumns) {
        view.setUpAdapter(productList, numOfColumns);
    }
}
