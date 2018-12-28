package com.example.konta.sketch_loyalityapp.main;

import dagger.Module;
import dagger.Provides;

@Module
public class MainActivityModule {

    @Provides
    MainActivityContract.Presenter provideMainActivityPresenter() {
        return new MainActivityPresenter();
    }
}
