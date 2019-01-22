package com.example.konta.sketch_loyalityapp.ui.terms;

import android.support.annotation.Nullable;

import com.example.konta.sketch_loyalityapp.pojo.staticPage.Page;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;

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
        Disposable disposable = model.fetchDataFromServer(pageId);
        compositeDisposable.add(disposable);
    }

    static DisposableSingleObserver<Page> getObserver() {
        return new DisposableSingleObserver<Page>() {
            @Override
            public void onSuccess(Page page) {

            }

            @Override
            public void onError(Throwable e) {

            }
        };
    }
}
