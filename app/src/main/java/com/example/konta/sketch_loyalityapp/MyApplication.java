package com.example.konta.sketch_loyalityapp;

import android.app.Application;

import java.io.IOException;
import java.io.InputStream;

public class MyApplication extends Application {

    private String mJson;

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
