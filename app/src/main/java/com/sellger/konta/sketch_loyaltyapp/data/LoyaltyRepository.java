package com.sellger.konta.sketch_loyaltyapp.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.sellger.konta.sketch_loyaltyapp.data.entity.Test;
import com.sellger.konta.sketch_loyaltyapp.data.local.TestDao;
import com.sellger.konta.sketch_loyaltyapp.data.local.TestRoomDatabase;

import java.util.List;

public class LoyaltyRepository {

    private TestDao mTestDao;
    private LiveData<List<Test>> mAllTests;

    public LoyaltyRepository(Application application) {
        TestRoomDatabase database = TestRoomDatabase.getDatabase(application);
        mTestDao = database.testDao();
        mAllTests = mTestDao.getAllTests();
    }

    public LiveData<List<Test>> getAllTests() {
        return mAllTests;
    }

    public void insert(Test test) {
        new insertAsyncTask(mTestDao).execute(test);
    }

    private static class insertAsyncTask extends AsyncTask<Test, Void, Void> {

        private TestDao mAsyncTaskDao;

        insertAsyncTask(TestDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Test... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
