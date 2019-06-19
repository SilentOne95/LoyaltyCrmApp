package com.sellger.konta.sketch_loyaltyapp.ui.website;

import com.sellger.konta.sketch_loyaltyapp.data.entity.Page;

public interface WebsiteContract {

    interface View {

        void setUpViewWithData(Page page);

        void displayToastMessage(String message);
    }

    interface Presenter {

        void requestDataFromServer(int pageId);
    }
}
