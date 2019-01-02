package com.example.konta.sketch_loyalityapp.ui.mainActivity;

import android.app.Activity;

import com.example.konta.sketch_loyalityapp.base.BaseFragment;

public interface MainActivityContract {

    interface View {

        void setFragment(BaseFragment fragment);
        void setActivity(Class<? extends Activity> activity);
    }

    interface Presenter {

        void displayHomeScreen();
        void displaySelectedScreen(int groupId, int itemId, String layoutType);
    }

    interface Model {

    }
}