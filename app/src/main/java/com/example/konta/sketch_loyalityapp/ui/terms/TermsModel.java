package com.example.konta.sketch_loyalityapp.ui.terms;

import android.support.annotation.NonNull;

import com.example.konta.sketch_loyalityapp.base.BaseCallbackListener;
import com.example.konta.sketch_loyalityapp.pojo.staticPage.Page;
import com.example.konta.sketch_loyalityapp.root.MyApplication;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TermsModel implements TermsContract.Model {

    @Override
    public void fetchDataFromServer(final BaseCallbackListener.SingleItemOnFinishListener<Page> onFinishedListener,
                                    int pageId) {
        MyApplication.getApi().getStaticPage(pageId).enqueue(new Callback<Page>() {
            @Override
            public void onResponse(@NonNull Call<Page> call, @NonNull Response<Page> response) {
                onFinishedListener.onFinished(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Page> call, @NonNull Throwable t) {
                onFinishedListener.onFailure(t);
            }
        });
    }
}
