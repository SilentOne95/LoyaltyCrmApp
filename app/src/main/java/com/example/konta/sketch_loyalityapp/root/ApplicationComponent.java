package com.example.konta.sketch_loyalityapp.root;

import com.example.konta.sketch_loyalityapp.main.MainActivity;
import com.example.konta.sketch_loyalityapp.main.MainActivityModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, MainActivityModule.class})
public interface ApplicationComponent {

    void inject(MainActivity mainActivity);
}
