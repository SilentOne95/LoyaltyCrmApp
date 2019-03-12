package com.example.konta.sketch_loyalityapp.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class MobileRepository {

    private MenuDao mMenuDao;
    private LiveData<List<Menu>> mAllMenus;

    public MobileRepository(Application application) {
        MobileRoomDatabase db = MobileRoomDatabase.getDatabase(application);
        mMenuDao = db.menuDao();
        mAllMenus = mMenuDao.getAllMenus();
    }

    LiveData<List<Menu>> getAllMenus() {
        return mAllMenus;
    }

    public void insert(Menu menu) {
        new insertAsyncTask(mMenuDao).execute(menu);
    }

    private static class insertAsyncTask extends AsyncTask<Menu, Void, Void> {

        private MenuDao mAsyncTaskDao;

        insertAsyncTask(MenuDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Menu... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
