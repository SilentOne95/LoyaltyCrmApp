package com.example.konta.sketch_loyalityapp.ui.map;

import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.util.Log;
import android.view.View;

import com.example.konta.sketch_loyalityapp.pojo.map.Marker;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

public class MapPresenter implements MapContract.Presenter {

    @Nullable
    private MapContract.View view;
    private MapContract.Model model;

    private static final String TAG = "MapFragment";
    private List<Marker> markerList = null;

    private static PublishSubject<Integer> markerIdSubject = PublishSubject.create();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    MapPresenter(@Nullable MapContract.View view, MapContract.Model model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void requestDataFromServer() {
        Disposable disposable = model.fetchDataFromServer(this);
        compositeDisposable.add(disposable);
    }

    @Override
    public void passDataToCluster(List<Marker> markerList) {
        this.markerList = markerList;
        if (view != null) {
            view.setUpCluster(markerList);
        }
    }

    @Override
    public void switchBottomSheetState(Object object) {

        if (view != null){
            if (object instanceof Marker) {
                if (view.getBottomSheetState() != BottomSheetBehavior.STATE_COLLAPSED) {
                    view.setBottomSheetState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            } else if (object instanceof LatLng) {
                if (view.getBottomSheetState() != BottomSheetBehavior.STATE_HIDDEN) {
                    view.setBottomSheetState(BottomSheetBehavior.STATE_HIDDEN);
                }
            } else if (object instanceof View){
                if (view.getBottomSheetState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    view.setBottomSheetState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    view.setBottomSheetState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        }
    }

    @Override
    public void passDataToBottomSheet(int markerId) {
        markerIdSubject.onNext(markerId);
    }

    public static Observable<Integer> getObservable() {
        return markerIdSubject;
    }

    @Override
    public void setUpObservable() {

        Observable<Integer> observable = getObservable();
        Observer<Integer> observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe");
            }

            @Override
            public void onNext(Integer markerId) {
                Log.d(TAG, "onNext" + String.valueOf(markerId));
                passDataToView(markerId);
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

        observable.subscribe(observer);
    }

    @Override
    public void passDataToView(int markerId) {
        int markerPosition = 0;

        for (int i = 0; i < markerList.size(); i++) {
            if (markerList.get(i).getId().equals(markerId)) {
                markerPosition = i;
                break;
            }
        }

        if (view != null) {
            view.setUpBottomSheetPanelWithData(markerList.get(markerPosition));
        }
    }
}
