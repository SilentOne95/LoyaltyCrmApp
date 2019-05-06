package com.sellger.konta.sketch_loyaltyapp.data.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "test_table")
public class Test {

    @PrimaryKey(autoGenerate = true)
    private int mId;

    @NonNull
    @ColumnInfo(name = "test")
    private String mWord;

    public Test(int id, @NonNull String word) {
        this.mId = id;
        this.mWord = word;
    }

    public int getId() { return this.mId; }

    public String getWord() { return this.mWord; }
}
