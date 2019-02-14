package com.example.konta.sketch_loyalityapp.ui.terms;

import android.support.annotation.Nullable;

import com.example.konta.sketch_loyalityapp.pojo.staticPage.Page;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class TermsPresenter implements TermsContract.Presenter {

    @Nullable
    private TermsContract.View view;
    private TermsContract.Model model;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    TermsPresenter(@Nullable TermsContract.View view, TermsContract.Model model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void requestDataFromServer(int pageId) {
        Disposable disposable = model.fetchDataFromServer(this, pageId);
        compositeDisposable.add(disposable);
    }

    @Override
    public void hideProgressBar() {
        if (view != null) {
            view.hideProgressBar();
        }
    }

    @Override
    public void passDataToView(Page page) {
        if (view != null) {
            view.setUpViewWithData(page);
        }
    }
}
