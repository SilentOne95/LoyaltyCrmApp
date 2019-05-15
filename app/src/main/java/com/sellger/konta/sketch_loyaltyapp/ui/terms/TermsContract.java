package com.sellger.konta.sketch_loyaltyapp.ui.terms;

import com.sellger.konta.sketch_loyaltyapp.data.entity.Page;

public interface TermsContract {

    interface View {

        void initViews();

        void hideProgressBar();

        void setUpViewWithData(Page page);
    }

    interface Presenter {

        void requestDataFromServer(int pageId);

        void hideProgressBar();

        void passDataToView(Page page);
    }
}
