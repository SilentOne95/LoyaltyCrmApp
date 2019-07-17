package com.jemsushi.loyaltyapp.ui.website;

import com.jemsushi.loyaltyapp.data.entity.Page;

public interface WebsiteContract {

    interface View {

        void setUpViewWithData(Page page);

        void displayToastMessage(String message);
    }

    interface Presenter {

        void requestDataFromServer(int pageId);
    }
}
