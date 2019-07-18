package com.ecommercelab.loyaltyapp.ui.terms;

import com.ecommercelab.loyaltyapp.data.entity.Page;

public interface TermsContract {

    interface View {

        void hideProgressBar();

        void setUpViewWithData(Page page);

        void displayToastMessage(String message);
    }

    interface Presenter {

        void requestDataFromServer(int pageId);
    }
}
