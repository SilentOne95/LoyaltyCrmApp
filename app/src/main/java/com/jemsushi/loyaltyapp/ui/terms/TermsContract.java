package com.jemsushi.loyaltyapp.ui.terms;

import com.jemsushi.loyaltyapp.data.entity.Page;

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
