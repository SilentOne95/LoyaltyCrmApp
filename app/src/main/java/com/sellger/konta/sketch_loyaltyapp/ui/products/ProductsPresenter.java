package com.sellger.konta.sketch_loyaltyapp.ui.products;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.sellger.konta.sketch_loyaltyapp.data.LoyaltyDataSource;
import com.sellger.konta.sketch_loyaltyapp.data.LoyaltyRepository;
import com.sellger.konta.sketch_loyaltyapp.data.entity.MenuComponent;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Product;

import java.util.List;

import static com.sellger.konta.sketch_loyaltyapp.Constants.DEFAULT_NUM_OF_COLUMNS;
import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_TYPE_PRODUCTS;
import static com.sellger.konta.sketch_loyaltyapp.Constants.TOAST_ERROR;

public class ProductsPresenter implements ProductsContract.Presenter {

    private static final String TAG = ProductsPresenter.class.getSimpleName();

    @NonNull
    private ProductsContract.View view;

    @NonNull
    private LoyaltyRepository loyaltyRepository;

    ProductsPresenter(@NonNull ProductsContract.View view, @NonNull LoyaltyRepository loyaltyRepository) {
        this.view = view;
        this.loyaltyRepository = loyaltyRepository;
    }

    /**
     * Called from {@link ProductsFragment#onCreate(Bundle)} to fetch required data from {@link LoyaltyRepository}.
     */
    @Override
    public void requestDataFromServer(Context context) {
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
                        manageViewsDataNotAvailable(context);
                    }
                });
            }

            @Override
            public void onDataNotAvailable() {
                hideProgressBar();
                manageViewsDataNotAvailable(context);
            }
        });
    }

    /**
     * Called from {@link #requestDataFromServer(Context)} whenever data is not available and need to
     * notify user about this.
     *
     * @param context of the app
     */
    private void manageViewsDataNotAvailable(Context context) {
        if (!isNetworkAvailable(context)) {
            view.changeVisibilityNoNetworkConnectionView(true);
        } else {
            view.displayToastMessage(TOAST_ERROR);
        }
    }

    /**
     * Called from {@link #requestDataFromServer()} to refactor fetched data.
     *
     * @param productList  of fetched items of {@link Product}
     * @param numOfColumns that data is going to be displayed in
     */
    private void refactorFetchedData(List<Product> productList, int numOfColumns) {
        if (numOfColumns < 1 || numOfColumns > 3) {
            numOfColumns = DEFAULT_NUM_OF_COLUMNS;
        }

        hideProgressBar();
        passDataToAdapter(productList, numOfColumns);
    }

    /**
     * Called from {@link #requestDataFromServer()} to hide progress bar when data is fetched or not.
     */
    private void hideProgressBar() {
        view.changeVisibilityProgressBar(false);
    }

    /**
     * Called from {@link #refactorFetchedData(List, int)} to pass refactored data to adapter,
     *
     * @param productList  of items are going to be displayed using adapter
     * @param numOfColumns that data is going to be displayed in
     */
    private void passDataToAdapter(List<Product> productList, int numOfColumns) {
        view.setUpAdapter(productList, numOfColumns);
    }

    /**
     * Called from {@link ProductsFragment#checkIfNetworkIsAvailableAndGetData()} to check
     * if network connection is active.
     *
     * @param context of the app
     * @return boolean depends on network status
     */
    @Override
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
