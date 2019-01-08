package com.example.konta.sketch_loyalityapp.root;

import android.app.Application;

import com.example.konta.sketch_loyalityapp.modelClasses.data.MenuList;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.konta.sketch_loyalityapp.Constants.BASE_URL;

public class MyApplication extends Application {

    private ApplicationComponent mApplicationComponent;

    static Api api;

    private String mJson;

    @Override
    public void onCreate() {
        super.onCreate();

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        api = retrofit.create(Api.class);

        api.getTest().enqueue(new Callback<List<MenuList>>() {
            @Override
            public void onResponse(Call<List<MenuList>> call, Response<List<MenuList>> response) {

            }

            @Override
            public void onFailure(Call<List<MenuList>> call, Throwable t) {

            }
        });

        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public static Api getApi() { return api; }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

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
