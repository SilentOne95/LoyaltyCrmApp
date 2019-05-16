package com.sellger.konta.sketch_loyaltyapp.ui.website;

import android.support.annotation.NonNull;

import com.sellger.konta.sketch_loyaltyapp.data.LoyaltyRepository;

public class WebsitePresenter implements WebsiteContract.Presenter {

    @NonNull
    private WebsiteContract.View view;

    @NonNull
    private LoyaltyRepository loyaltyRepository;

    WebsitePresenter(@NonNull WebsiteContract.View view, @NonNull LoyaltyRepository loyaltyRepository) {
        this.view = view;
        this.loyaltyRepository = loyaltyRepository;
    }

    @Override
    public void requestDataFromServer(int pageId) {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void passDataToView(String websiteUrl) {

    }
}
