package com.jemsushi.loyaltyapp.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.jemsushi.loyaltyapp.adapter.HomeAdapter;
import com.jemsushi.loyaltyapp.data.LoyaltyDataSource;
import com.jemsushi.loyaltyapp.data.LoyaltyRepository;
import com.jemsushi.loyaltyapp.data.entity.MenuComponent;
import com.jemsushi.loyaltyapp.data.utils.HelperMenuArray;
import com.jemsushi.loyaltyapp.ui.main.MainActivityPresenter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

import static com.jemsushi.loyaltyapp.Constants.NAV_DRAWER_TYPE_MENU;
import static com.jemsushi.loyaltyapp.Constants.NAV_DRAWER_TYPE_SUBMENU;
import static com.jemsushi.loyaltyapp.Constants.TOAST_ERROR;

public class HomePresenter implements HomeContract.Presenter {

    private static final String TAG = HomePresenter.class.getSimpleName();

    @NonNull
    private HomeContract.View view;

    @NonNull
    private LoyaltyRepository loyaltyRepository;

    private static PublishSubject<Integer> mItemIdSubject = PublishSubject.create();
    private ArrayList<MenuComponent> mNavDrawerArray = new ArrayList<>();

    HomePresenter(@NonNull HomeContract.View view, @NonNull LoyaltyRepository loyaltyRepository) {
        this.view = view;
        this.loyaltyRepository = loyaltyRepository;
    }

    /**
     * Called from {@link HomeFragment#onCreate(Bundle)} to fetch required data from {@link LoyaltyRepository}.
     */
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
                view.displayToastMessage(TOAST_ERROR);
            }
        });
    }

    /**
     * Called from {@link #requestDataFromServer()} to hide progress bar when data is fetched or not.
     */
    private void hideProgressBar() {
        view.hideProgressBar();
    }

    /**
     * Called from {@link #requestDataFromServer()} to refactor fetched data and pass it to {@link HomeAdapter}.
     *
     * @param listOfItems of fetched data of {@link MenuComponent}
     */
    private void refactorFetchedData(List<MenuComponent> listOfItems) {
        int numOfColumns = 2;

        // Sort fetched data and pass to an array
        HelperMenuArray helperMenuArray = sortMenuDataList(listOfItems);
        mNavDrawerArray = helperMenuArray.getMenuArray();
        mNavDrawerArray.addAll(helperMenuArray.getSubmenuArray());

        // Get num of columns in which data should be displayed and remove 'home' screen from this list
        // as it shouldn't be accessible from itself
        for (int i = 0; i < mNavDrawerArray.size(); i++) {
            if (mNavDrawerArray.get(i).getIsHomePage().equals(1)) {
                numOfColumns = mNavDrawerArray.get(i).getNumberOfColumns();
                mNavDrawerArray.remove(i);
                break;
            }
        }

        passDataToAdapter(mNavDrawerArray, numOfColumns);
    }

    /**
     * Called from {@link #refactorFetchedData(List)} to sort all items based on menu section
     * (first or second) and target position in related menu section.
     *
     * @param listOfItems of {@link MenuComponent}
     * @return {@link HelperMenuArray} with two sorted menu arrays
     */
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

                position++;
                index = 0;
            } else {
                index++;
            }
        } while (sortedSubmenuArray.size() < submenuLocalArray.size());

        return new HelperMenuArray(sortedMenuArray, sortedSubmenuArray);
    }

    /**
     * Called from {@link #refactorFetchedData(List)} to pass refactored data to adapter.
     *
     * @param menuComponentList of items are going to be displayed using adapter
     * @param numOfColumns      that data is going to be displayed in
     */
    private void passDataToAdapter(ArrayList<MenuComponent> menuComponentList, int numOfColumns) {
        if (menuComponentList != null) {
            view.setUpAdapter(menuComponentList, numOfColumns);
        }
    }

    /**
     * Called from callback listener implemented in {@link HomeFragment} to notify Observer set up
     * in {@link MainActivityPresenter#setUpObservableHomeAdapter()} which item was clicked.
     *
     * @param viewId represents position of item that is going to be set as checked
     */
    @Override
    public void passIdOfSelectedView(int viewId) {
        mItemIdSubject.onNext(viewId);
    }

    /**
     * Public static observable set up for Observer in {@link MainActivityPresenter#setUpObservableHomeAdapter()}.
     *
     * @return id of selected item
     */
    public static Observable<Integer> getObservableSelectedView() {
        return mItemIdSubject;
    }
}
