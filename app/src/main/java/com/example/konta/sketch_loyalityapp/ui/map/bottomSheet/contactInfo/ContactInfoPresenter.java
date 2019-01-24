package com.example.konta.sketch_loyalityapp.ui.map.bottomSheet.contactInfo;

import android.support.annotation.Nullable;
import android.util.Log;

import com.example.konta.sketch_loyalityapp.pojo.map.Marker;
import com.example.konta.sketch_loyalityapp.ui.map.MapContract;
import com.example.konta.sketch_loyalityapp.ui.map.MapPresenter;
import com.example.konta.sketch_loyalityapp.ui.map.bottomSheet.BottomSheetContract;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class ContactInfoPresenter implements BottomSheetContract.ContactInfoPresenter {

    @Nullable
    private BottomSheetContract.ContactInfoView view;
    private MapContract.Model model;

    private static final String TAG = "ContactInfoFragment";
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private int markerInt;

    ContactInfoPresenter(@Nullable BottomSheetContract.ContactInfoView view, MapContract.Model model) {
        this.view = view;
        this.model = model;
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
            public void onNext(Integer markerId) {
                Log.d(TAG, "onNext");
                markerInt = markerId;
                getMarkerList();
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

    @Override
    public void getMarkerList() {
        Disposable disposable = model.fetchDataFromServer(this);
        compositeDisposable.add(disposable);
    }

    @Override
    public void passDataToView(List<Marker> markerList) {
        if (view != null) {
            view.setUpViewsWithData(markerList);
        }
    }
}
