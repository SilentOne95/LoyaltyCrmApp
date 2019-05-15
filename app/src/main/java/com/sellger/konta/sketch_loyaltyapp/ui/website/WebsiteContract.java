package com.sellger.konta.sketch_loyaltyapp.ui.website;

public interface WebsiteContract {

    interface View {

        void initViews();

        void setUpViewWithData(String websiteUrl);
    }

    interface Presenter {

        void requestDataFromServer(int pageId);

        void hideProgressBar();

        void passDataToView(String websiteUrl);
    }
}
