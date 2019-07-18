package com.ecommercelab.loyaltyapp.ui.website;

import com.ecommercelab.loyaltyapp.data.entity.Page;

public interface WebsiteContract {

    interface View {

        void setUpViewWithData(Page page);

        void displayToastMessage(String message);
    }

    interface Presenter {

        void requestDataFromServer(int pageId);
    }
}
