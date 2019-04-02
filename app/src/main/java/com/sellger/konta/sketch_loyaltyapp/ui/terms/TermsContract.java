package com.sellger.konta.sketch_loyaltyapp.ui.terms;

import com.sellger.konta.sketch_loyaltyapp.pojo.staticPage.Page;

import io.reactivex.disposables.Disposable;

public interface TermsContract {

    interface View {

        void hideProgressBar();
        void setUpViewWithData(Page page);
    }

    interface Presenter {

        void requestDataFromServer(int pageId);
        void hideProgressBar();
        void passDataToView(Page page);
    }

    interface Model {

        Disposable fetchDataFromServer(TermsPresenter presenter, int pageId);
    }
}
