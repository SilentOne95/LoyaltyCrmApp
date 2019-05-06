package com.sellger.konta.sketch_loyaltyapp.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.sellger.konta.sketch_loyaltyapp.data.entity.Test;

@Database(entities = {Test.class}, version = 1)
public abstract class TestRoomDatabase extends RoomDatabase {

    public abstract TestDao testDao();
    private static volatile TestRoomDatabase INSTANCE;

    public static TestRoomDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (TestRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TestRoomDatabase.class, "test_database")
                            .build();
                }
            }
        }

        return INSTANCE;
    }
}
