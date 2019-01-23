package com.example.konta.sketch_loyalityapp.ui.terms;

import com.example.konta.sketch_loyalityapp.network.Api;
import com.example.konta.sketch_loyalityapp.network.RetrofitClient;
import com.example.konta.sketch_loyalityapp.pojo.staticPage.Page;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class TermsModel implements TermsContract.Model {

    private TermsPresenter presenter;

    @Override
    public Disposable fetchDataFromServer(TermsPresenter presenter, int pageId) {
        this.presenter = presenter;
        return getObservable(pageId).subscribeWith(getObserver());
    }

    private Single<Page> getObservable(int pageId) {
        return RetrofitClient.getInstance().create(Api.class)
                .getStaticPage(pageId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private DisposableSingleObserver<Page> getObserver() {
        return new DisposableSingleObserver<Page>() {
            @Override
            public void onSuccess(Page page) {
                presenter.passDataToView(page);
            }

            @Override
            public void onError(Throwable e) { }
        };
    }
}
