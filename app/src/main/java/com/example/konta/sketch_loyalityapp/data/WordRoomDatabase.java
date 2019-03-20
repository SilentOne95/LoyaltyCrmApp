package com.example.konta.sketch_loyalityapp.data;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

@Database(entities = {Word.class}, version = 1)
public abstract class WordRoomDatabase extends RoomDatabase {

    public abstract WordDao testDao();
    private static volatile WordRoomDatabase INSTANCE;

    public static WordRoomDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (WordRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WordRoomDatabase.class, "test_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }

        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new DeleteDbDataAsync(INSTANCE).execute();
        }
    };

    private static class DeleteDbDataAsync extends AsyncTask<Void, Void, Void> {

        private final WordDao mDao;

        DeleteDbDataAsync(WordRoomDatabase database) { mDao = database.testDao(); }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d("test", "onOpen delete");
            mDao.deleteAll();
            return null;
        }


    }
}
