package com.sellger.konta.sketch_loyaltyapp.ui.products;

import android.support.annotation.Nullable;

import com.sellger.konta.sketch_loyaltyapp.data.LoyaltyDataSource;
import com.sellger.konta.sketch_loyaltyapp.data.LoyaltyRepository;
import com.sellger.konta.sketch_loyaltyapp.data.entity.MenuComponent;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Product;

import java.util.List;

import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_TYPE_PRODUCTS;

public class ProductsPresenter implements ProductsContract.Presenter {

    @Nullable
    private ProductsContract.View view;
    private LoyaltyRepository loyaltyRepository;

    ProductsPresenter(@Nullable ProductsContract.View view, LoyaltyRepository loyaltyRepository) {
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
                        for (Object menuComponent : data) {
                            if (((MenuComponent) menuComponent).getType().equals(LAYOUT_TYPE_PRODUCTS)) {
                                hideProgressBar();
                                passDataToAdapter(productList, ((MenuComponent) menuComponent).getNumberOfColumns());
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
    public void hideProgressBar() {
        if (view != null) {
            view.hideProgressBar();
        }
    }

    @Override
    public void passDataToAdapter(List<Product> productList, int numOfColumns) {
        if (view != null) {
            view.setUpAdapter(productList, numOfColumns);
        }
    }
}
