package com.sellger.konta.sketch_loyaltyapp.data.utils;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sellger.konta.sketch_loyaltyapp.data.entity.OpenHour;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class OpenHourTypeConverter {

    private static Gson gson = new Gson();

    @TypeConverter
    public static List<OpenHour> stringToList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<OpenHour>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String listToString(List<OpenHour> openHourList) {
        return gson.toJson(openHourList);
    }
}
