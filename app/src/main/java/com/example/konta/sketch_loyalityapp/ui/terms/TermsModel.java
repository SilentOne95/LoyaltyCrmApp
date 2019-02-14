package com.example.konta.sketch_loyalityapp.ui.terms;

import com.example.konta.sketch_loyalityapp.network.Api;
import com.example.konta.sketch_loyalityapp.network.RetrofitClient;
import com.example.konta.sketch_loyalityapp.pojo.staticPage.Page;

import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TermsModel implements TermsContract.Model {

    private TermsPresenter presenter;

    @Override
    public Disposable fetchDataFromServer(TermsPresenter presenter, int pageId) {
        this.presenter = presenter;
        return getObservableTimer(pageId)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(page -> {
                    presenter.passDataToView(page);
                    presenter.hideProgressBar();
                }, throwable -> presenter.hideProgressBar());
    }

    private Single<Page> getObservableTimer(int pageId) {
        return Single.zip(getObservable(pageId), Single.timer(1000, TimeUnit.MILLISECONDS),
                (page, timer) -> page);
    }

    private Single<Page> getObservable(int pageId) {
        return RetrofitClient.getInstance().create(Api.class)
                .getStaticPage(pageId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
