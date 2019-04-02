package com.sellger.konta.sketch_loyaltyapp.root;

import com.sellger.konta.sketch_loyaltyapp.ui.main.MainActivity;
import com.sellger.konta.sketch_loyaltyapp.ui.main.MainActivityModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, MainActivityModule.class})
public interface ApplicationComponent {

    void inject(MainActivity mainActivity);
}
