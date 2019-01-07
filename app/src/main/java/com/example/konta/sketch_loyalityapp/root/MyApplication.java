package com.example.konta.sketch_loyalityapp.root;

import android.app.Application;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;

public class MyApplication extends Application {

    private ApplicationComponent mApplicationComponent;

    private String mJson;

    @Override
    public void onCreate() {
        super.onCreate();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .build();

        Api api = retrofit.create(Api.class);

        api.getTest().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    Log.d("Test test ", response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_LONG).show();
            }
        });


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

    interface Api {
        @GET("/posts")
        Call<ResponseBody> getTest();
    }
}
