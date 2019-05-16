package com.sellger.konta.sketch_loyaltyapp.ui.productDetails;

import android.support.annotation.NonNull;

import com.sellger.konta.sketch_loyaltyapp.data.LoyaltyDataSource;
import com.sellger.konta.sketch_loyaltyapp.data.LoyaltyRepository;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Product;

import static com.sellger.konta.sketch_loyaltyapp.Constants.TOAST_DATA_ERROR_MESSAGE;

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
                view.displayToastMessage(TOAST_DATA_ERROR_MESSAGE);
            }
        });
    }

    @Override
    public void hideProgressBar() {
        view.hideProgressBar();
    }

    @Override
    public void passDataToView(Product product) {
        view.setUpViewWithData(product);
    }
}
