package com.sellger.konta.sketch_loyaltyapp.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "word_table")
public class Word {

    @PrimaryKey(autoGenerate = true)
    private int mId;

    @NonNull
    @ColumnInfo(name = "word")
    private String mWord;

    public Word(int id, @NonNull String word) {
        this.mId = id;
        this.mWord = word;
    }

    public int getId() { return this.mId; }

    public String getWord() { return this.mWord; }
}
