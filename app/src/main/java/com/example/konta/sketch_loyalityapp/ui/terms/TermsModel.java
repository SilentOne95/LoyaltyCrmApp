package com.example.konta.sketch_loyalityapp.ui.terms;

import com.example.konta.sketch_loyalityapp.network.Api;
import com.example.konta.sketch_loyalityapp.network.RetrofitClient;
import com.example.konta.sketch_loyalityapp.pojo.staticPage.Page;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TermsModel implements TermsContract.Model {

    @Override
    public Disposable fetchDataFromServer(int pageId) {
        return getObservable(pageId).subscribeWith(TermsPresenter.getObserver());
    }

    private Single<Page> getObservable(int pageId) {
        return RetrofitClient.getInstance().create(Api.class)
                .getStaticPage(pageId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
