package com.sellger.konta.sketch_loyaltyapp.ui.terms;

import android.support.annotation.Nullable;

import com.sellger.konta.sketch_loyaltyapp.data.LoyaltyDataSource;
import com.sellger.konta.sketch_loyaltyapp.data.LoyaltyRepository;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Page;

public class TermsPresenter implements TermsContract.Presenter {

    @Nullable
    private TermsContract.View view;
    private LoyaltyRepository loyaltyRepository;

    TermsPresenter(@Nullable TermsContract.View view, LoyaltyRepository loyaltyRepository) {
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
    public void passDataToView(Page page) {
        if (view != null) {
            view.setUpViewWithData(page);
        }
    }
}
