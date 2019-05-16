package com.sellger.konta.sketch_loyaltyapp.ui.terms;

import android.support.annotation.NonNull;

import com.sellger.konta.sketch_loyaltyapp.data.LoyaltyDataSource;
import com.sellger.konta.sketch_loyaltyapp.data.LoyaltyRepository;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Page;

import static com.sellger.konta.sketch_loyaltyapp.Constants.TOAST_DATA_ERROR_MESSAGE;

public class TermsPresenter implements TermsContract.Presenter {

    @NonNull
    private TermsContract.View view;

    @NonNull
    private LoyaltyRepository loyaltyRepository;

    TermsPresenter(@NonNull TermsContract.View view, @NonNull LoyaltyRepository loyaltyRepository) {
        this.view = view;
        this.loyaltyRepository = loyaltyRepository;
    }

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
                view.displayToastMessage(TOAST_DATA_ERROR_MESSAGE);
            }
        });
    }

    @Override
    public void hideProgressBar() {
        view.hideProgressBar();
    }

    @Override
    public void passDataToView(Page page) {
        view.setUpViewWithData(page);
    }
}
