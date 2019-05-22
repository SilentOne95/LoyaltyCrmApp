package com.sellger.konta.sketch_loyaltyapp.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "product_table")
public class Product {

    @PrimaryKey
    @SerializedName("id")
    @Expose
    @ColumnInfo(name = "id")
    private Integer mId;

    @SerializedName("title")
    @Expose
    @ColumnInfo(name = "title")
    private String mTitle;

    @SerializedName("image")
    @Expose
    @ColumnInfo(name = "image")
    private String mImage;

    @SerializedName("price")
    @Expose
    @ColumnInfo(name = "price")
    private Float mPrice;

    @SerializedName("description")
    @Expose
    @ColumnInfo(name = "description")
    private String mDescription;

    @SerializedName("short_description")
    @Expose
    @ColumnInfo(name = "short_description")
    private String mShortDescription;

    public Integer getId() {
        return mId;
    }
    public void setId(Integer id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }
    public void setTitle(String title) {
        mTitle = title;
    }

    public String getImage() {
        return mImage;
    }
    public void setImage(String image) {
        mImage = image;
    }

    public Float getPrice() {
        return mPrice;
    }
    public void setPrice(Float price) {
        mPrice = price;
    }

    public String getDescription() {
        return mDescription;
    }
    public void setDescription(String description) {
        mDescription = description;
    }

    public String getShortDescription() {
        return mShortDescription;
    }
    public void setShortDescription(String shortDescription) {
        mShortDescription = shortDescription;
    }
}
