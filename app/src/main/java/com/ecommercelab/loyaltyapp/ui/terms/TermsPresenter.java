package com.ecommercelab.loyaltyapp.ui.terms;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.ecommercelab.loyaltyapp.data.LoyaltyDataSource;
import com.ecommercelab.loyaltyapp.data.LoyaltyRepository;
import com.ecommercelab.loyaltyapp.data.entity.Page;

import static com.ecommercelab.loyaltyapp.Constants.TOAST_ERROR;

public class TermsPresenter implements TermsContract.Presenter {

    private static final String TAG = TermsPresenter.class.getSimpleName();

    @NonNull
    private TermsContract.View view;

    @NonNull
    private LoyaltyRepository loyaltyRepository;

    TermsPresenter(@NonNull TermsContract.View view, @NonNull LoyaltyRepository loyaltyRepository) {
        this.view = view;
        this.loyaltyRepository = loyaltyRepository;
    }

    /**
     * Called from {@link TermsFragment#onCreate(Bundle)} to fetch required data from {@link LoyaltyRepository}.
     *
     * @param pageId of the item which info is going to be fetched
     */
    @Override
    public void requestDataFromServer(int pageId) {
        loyaltyRepository.getSinglePage(pageId, new LoyaltyDataSource.GetSingleDataCallback() {
            @Override
            public void onDataLoaded(Object object) {
                hideProgressBar();
                passDataToView((Page) object);
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
     * @param page item containing all details, refer {@link Page}
     */
    private void passDataToView(Page page) {
        view.setUpViewWithData(page);
    }
}
