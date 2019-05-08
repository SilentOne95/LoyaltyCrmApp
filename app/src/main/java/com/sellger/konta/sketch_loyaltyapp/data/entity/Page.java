package com.sellger.konta.sketch_loyaltyapp.data.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "page_table")
public class Page {

    @PrimaryKey
    @SerializedName("id")
    @Expose
    @ColumnInfo(name = "id")
    private Integer mId;

    @SerializedName("title")
    @Expose
    @ColumnInfo(name = "title")
    private String mTitle;

    @SerializedName("body")
    @Expose
    @ColumnInfo(name = "body")
    private String mBody;

    public Integer getId() {
        return mId;
    }
    public void setId(Integer id) { mId = id; }

    public String getTitle() {
        return mTitle;
    }
    public void setTitle(String title) {
        mTitle = title;
    }

    public String getBody() { return mBody; }
    public void setBody(String body) {
        mBody = body;
    }
}