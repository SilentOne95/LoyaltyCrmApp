package com.example.konta.sketch_loyalityapp.ui.terms;

import com.example.konta.sketch_loyalityapp.pojo.staticPage.Page;

import io.reactivex.disposables.Disposable;

public interface TermsContract {

    interface View {

        void setUpViewWithData(Page page);
    }

    interface Presenter {

        void requestDataFromServer(int pageId);
        void passDataToView(Page page);
    }

    interface Model {

        Disposable fetchDataFromServer(TermsPresenter presenter, int pageId);
    }
}
