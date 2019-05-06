package com.sellger.konta.sketch_loyaltyapp.data.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.sellger.konta.sketch_loyaltyapp.data.entity.Test;

import java.util.List;

@Dao
public interface TestDao {

    @Insert
    void insert(Test test);

    @Query("DELETE FROM test_table")
    void deleteAll();

    @Query("SELECT * FROM test_table")
    LiveData<List<Test>> getAllTests();
}
