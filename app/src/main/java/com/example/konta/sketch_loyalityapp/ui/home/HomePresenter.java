package com.example.konta.sketch_loyalityapp.ui.home;

import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.example.konta.sketch_loyalityapp.pojo.menu.MenuComponent;
import com.example.konta.sketch_loyalityapp.ui.main.MainActivityContract;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

public class HomePresenter implements HomeContract.Presenter {

    @Nullable
    private HomeContract.View view;
    private MainActivityContract.Model model;

    private static PublishSubject<Integer> markerIdSubject = PublishSubject.create();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    HomePresenter(@Nullable HomeContract.View view, MainActivityContract.Model model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void requestDataFromServer() {
        Disposable disposable = model.fetchDataFromServer(this);
        compositeDisposable.add(disposable);
    }

    @Override
    public void passDataToAdapter(SparseArray<MenuComponent> menuComponentList, int numOfColumns) {
        if (view != null && menuComponentList != null) {
            view.setUpAdapter(menuComponentList, numOfColumns);
        }
    }

    @Override
    public void passIdOfSelectedView(int viewId) {
        markerIdSubject.onNext(viewId);
    }

    public static Observable<Integer> getObservableSelectedView() {
        return markerIdSubject;
    }
}
