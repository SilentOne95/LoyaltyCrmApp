package com.sellger.konta.sketch_loyaltyapp.ui.home;

import android.support.annotation.Nullable;

import com.sellger.konta.sketch_loyaltyapp.data.LoyaltyDataSource;
import com.sellger.konta.sketch_loyaltyapp.data.LoyaltyRepository;
import com.sellger.konta.sketch_loyaltyapp.data.entity.MenuComponent;
import com.sellger.konta.sketch_loyaltyapp.data.utils.HelperMenuArray;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

import static com.sellger.konta.sketch_loyaltyapp.Constants.NAV_DRAWER_TYPE_MENU;
import static com.sellger.konta.sketch_loyaltyapp.Constants.NAV_DRAWER_TYPE_SUBMENU;

public class HomePresenter implements HomeContract.Presenter {

    @Nullable
    private HomeContract.View view;
    private LoyaltyRepository loyaltyRepository;

    private static PublishSubject<Integer> mMarkerIdSubject = PublishSubject.create();
    private ArrayList<MenuComponent> mNavDrawerArray = new ArrayList<>();

    HomePresenter(@Nullable HomeContract.View view, LoyaltyRepository loyaltyRepository) {
        this.view = view;
        this.loyaltyRepository = loyaltyRepository;
    }

    @Override
    public void requestDataFromServer() {
        loyaltyRepository.getMenu(new LoyaltyDataSource.LoadDataCallback() {
            @Override
            public void onDataLoaded(List<?> data) {
                hideProgressBar();
                refactorFetchedData((List<MenuComponent>) data);
            }

            @Override
            public void onDataNotAvailable() {
                hideProgressBar();
            }
        });
    }

    @Override
    public void hideProgressBar() {
        if (view != null) {
            view.hideProgressBar();
        }
    }

    @Override
    public void refactorFetchedData(List<MenuComponent> listOfItems) {
        int numOfColumns = 2;
        HelperMenuArray helperMenuArray = sortMenuDataList(listOfItems);
        mNavDrawerArray = helperMenuArray.getMenuArray();
        mNavDrawerArray.addAll(helperMenuArray.getSubmenuArray());

        for (int i = 0; i < mNavDrawerArray.size(); i++) {
            if (mNavDrawerArray.get(i).getIsHomePage().equals(1)) {
                numOfColumns = mNavDrawerArray.get(i).getNumberOfColumns();
                mNavDrawerArray.remove(i);
                break;
            }
        }

        passDataToAdapter(mNavDrawerArray, numOfColumns);
    }

    private HelperMenuArray sortMenuDataList(List<MenuComponent> listOfItems) {
        String menuType;
        ArrayList<MenuComponent> menuLocalArray = new ArrayList<>();
        ArrayList<MenuComponent> submenuLocalArray = new ArrayList<>();
        ArrayList<MenuComponent> sortedMenuArray = new ArrayList<>();
        ArrayList<MenuComponent> sortedSubmenuArray = new ArrayList<>();

        for (int i = 0; i < listOfItems.size(); i++) {
            menuType = listOfItems.get(i).getList();

            switch (menuType) {
                case NAV_DRAWER_TYPE_MENU:
                    menuLocalArray.add(listOfItems.get(i));
                    break;
                case NAV_DRAWER_TYPE_SUBMENU:
                    submenuLocalArray.add(listOfItems.get(i));
                    break;
            }
        }

        int index = 0;
        int position = 1;

        do {

            if (menuLocalArray.get(index).getPosition() == position) {
                sortedMenuArray.add(menuLocalArray.get(index));

                position++;
                index = 0;
            } else {
                index++;
            }

        } while (sortedMenuArray.size() < menuLocalArray.size());

        index = 0;
        position = 1;

        do {

            if (submenuLocalArray.get(index).getPosition() == position) {
                sortedSubmenuArray.add(submenuLocalArray.get(index));

                position ++;
                index = 0;
            } else {
                index++;
            }


        } while (sortedSubmenuArray.size() < submenuLocalArray.size());

        return new HelperMenuArray(sortedMenuArray, sortedSubmenuArray);
    }

    @Override
    public void passDataToAdapter(ArrayList<MenuComponent> menuComponentList, int numOfColumns) {
        if (view != null && menuComponentList != null) {
            view.setUpAdapter(menuComponentList, numOfColumns);
        }
    }

    @Override
    public void passIdOfSelectedView(int viewId) {
        mMarkerIdSubject.onNext(viewId);
    }

    public static Observable<Integer> getObservableSelectedView() {
        return mMarkerIdSubject;
    }
}
