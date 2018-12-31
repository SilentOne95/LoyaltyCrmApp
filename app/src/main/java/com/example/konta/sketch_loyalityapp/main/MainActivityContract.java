package com.example.konta.sketch_loyalityapp.main;

import android.app.Activity;

import com.example.konta.sketch_loyalityapp.baseFragment.BaseFragment;

public interface MainActivityContract {

    interface View {

        void setFragment(BaseFragment fragment);
        void setActivity(Class<? extends Activity> activity);
    }

    interface Presenter {

        void setView(MainActivityContract.View view);

        void displayHomeScreen();
        void displaySelectedScreen(int groupId, int itemId, String layoutType);
    }

    interface Model {

    }
}
