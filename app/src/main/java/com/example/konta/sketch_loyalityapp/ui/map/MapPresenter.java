package com.example.konta.sketch_loyalityapp.ui.map;

import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.view.View;

import com.example.konta.sketch_loyalityapp.pojo.map.Marker;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

public class MapPresenter implements MapContract.Presenter {

    @Nullable
    private MapContract.View view;
    private MapContract.Model model;

    private static PublishSubject<Integer> data = PublishSubject.create();
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
        data.onNext(markerId);
        data.onComplete();
    }

    public static Observable<Integer> getObservable() {
        return data;
    }
}
