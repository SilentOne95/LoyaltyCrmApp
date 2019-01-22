package com.example.konta.sketch_loyalityapp.ui.terms;

import android.support.annotation.Nullable;

import com.example.konta.sketch_loyalityapp.base.BaseCallbackListener;
import com.example.konta.sketch_loyalityapp.pojo.staticPage.Page;

public class TermsPresenter implements TermsContract.Presenter,
        BaseCallbackListener.SingleItemOnFinishListener<Page> {

    @Nullable
    private TermsContract.View view;
    private TermsContract.Model model;

    TermsPresenter(@Nullable TermsContract.View view, TermsContract.Model model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void requestDataFromServer(int pageId) {
        model.fetchDataFromServer(this, pageId);
    }

    @Override
    public void onFinished(Page page) {
        if (view != null) {
            view.setUpViewWithData(page);
        }
    }

    @Override
    public void onFailure(Throwable t) {
        if (view != null) {
            view.onResponseFailure();
        }
    }
}
