package com.example.konta.sketch_loyalityapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Menu.class}, version = 1)
public abstract class MobileRoomDatabase extends RoomDatabase {

    public abstract MenuDao menuDao();

    private static volatile MobileRoomDatabase INSTANCE;

    static MobileRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MobileRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MobileRoomDatabase.class, "db_mobile")
                            .build();
                }
            }
        }

        return INSTANCE;
    }
}
