package com.example.konta.sketch_loyalityapp.root;

import com.example.konta.sketch_loyalityapp.modelClasses.data.MenuList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {

    // Get menu components
    @GET("/app_loyalty_app/get_all/0")
    Call<List<MenuList>> getTest();
}
