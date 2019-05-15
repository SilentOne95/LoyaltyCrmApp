package com.sellger.konta.sketch_loyaltyapp.ui.website;

import com.sellger.konta.sketch_loyaltyapp.data.LoyaltyRepository;

import javax.annotation.Nullable;

public class WebsitePresenter implements WebsiteContract.Presenter {

    @Nullable
    private WebsiteContract.View view;
    private LoyaltyRepository loyaltyRepository;

    WebsitePresenter(@Nullable WebsiteContract.View view, LoyaltyRepository loyaltyRepository) {
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
