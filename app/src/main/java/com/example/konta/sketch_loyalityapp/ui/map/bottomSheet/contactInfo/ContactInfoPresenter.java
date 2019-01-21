package com.example.konta.sketch_loyalityapp.ui.map.bottomSheet.contactInfo;

import android.support.annotation.Nullable;
import android.util.Log;

import com.example.konta.sketch_loyalityapp.ui.map.MapPresenter;
import com.example.konta.sketch_loyalityapp.ui.map.bottomSheet.BottomSheetContract;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ContactInfoPresenter implements BottomSheetContract.ContactInfoPresenter {

    @Nullable
    private BottomSheetContract.ContactInfoView view;

    private static final String TAG = "ContactInfoFragment";

    ContactInfoPresenter(@Nullable BottomSheetContract.ContactInfoView view) {
        this.view = view;
    }

    @Override
    public void setUpObservable() {

        Observable<Integer> observable = MapPresenter.getObservable();
        Observer<Integer> onMarkerClickObserver = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe");
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "onNext" + String.valueOf(integer));
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete");
            }
        };

        observable.subscribe(onMarkerClickObserver);
    }
}
