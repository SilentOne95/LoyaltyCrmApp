package com.example.konta.sketch_loyalityapp.root;

import android.app.Application;

import java.io.IOException;
import java.io.InputStream;

public class MyApplication extends Application {

    private ApplicationComponent mApplicationComponent;

    private String mJson;

    @Override
    public void onCreate() {
        super.onCreate();

        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent() { return mApplicationComponent; }

    public String readFromAssets(String filename) {
        try {
            InputStream inputStream = getAssets().open(filename);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            mJson = new String(buffer, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return mJson;
    }
}
