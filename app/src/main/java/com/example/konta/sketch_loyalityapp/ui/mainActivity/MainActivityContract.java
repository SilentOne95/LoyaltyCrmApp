package com.example.konta.sketch_loyalityapp.ui.mainActivity;

import android.app.Activity;

import com.example.konta.sketch_loyalityapp.base.BaseFragment;

public interface MainActivityContract {

    interface View {

        void setFragment(BaseFragment fragment);
        void setActivity(Class<? extends Activity> activity);

        void setDisplayScreenChecked(String layoutType);
    }

    interface Presenter {

        void displayHomeScreen();
        void displaySelectedScreen(String layoutType);
    }

    interface Model {

    }
}
