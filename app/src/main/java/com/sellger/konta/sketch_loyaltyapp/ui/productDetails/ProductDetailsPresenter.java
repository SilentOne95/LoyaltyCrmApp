package com.sellger.konta.sketch_loyaltyapp.ui.productDetails;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.sellger.konta.sketch_loyaltyapp.data.LoyaltyDataSource;
import com.sellger.konta.sketch_loyaltyapp.data.LoyaltyRepository;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Product;

import static com.sellger.konta.sketch_loyaltyapp.Constants.TOAST_ERROR;

public class ProductDetailsPresenter implements ProductDetailsContract.Presenter {

    private static final String TAG = ProductDetailsPresenter.class.getSimpleName();

    @NonNull
    private ProductDetailsContract.View view;

    @NonNull
    private LoyaltyRepository loyaltyRepository;

    ProductDetailsPresenter(@NonNull ProductDetailsContract.View view, @NonNull LoyaltyRepository loyaltyRepository) {
        this.view = view;
        this.loyaltyRepository = loyaltyRepository;
    }

    /**
     * Called from {@link ProductDetailsActivity#onCreate(Bundle)} to fetch required data from {@link LoyaltyRepository}.
     *
     * @param productId of the item which info is going to be fetched
     */
    @Override
    public void requestDataFromServer(int productId) {
        loyaltyRepository.getSingleProduct(productId, new LoyaltyDataSource.GetSingleDataCallback() {
            @Override
            public void onDataLoaded(Object object) {
                hideProgressBar();
                passDataToView((Product) object);
            }

            @Override
            public void onDataNotAvailable() {
                hideProgressBar();
                view.displayToastMessage(TOAST_ERROR);
            }
        });
    }

    /**
     * Called from {@link #requestDataFromServer(int)} to hide progress bar when data is fetched or not.
     */
    private void hideProgressBar() {
        view.hideProgressBar();
    }

    /**
     * Called from {@link #requestDataFromServer(int)} to pass refactored data to view.
     *
     * @param product item containing all details, refer {@link Product}
     */
    private void passDataToView(Product product) {
        view.setUpViewWithData(product);
    }
}
