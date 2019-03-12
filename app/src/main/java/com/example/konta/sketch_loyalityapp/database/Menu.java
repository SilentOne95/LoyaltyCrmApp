package com.example.konta.sketch_loyalityapp.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "menu_table")
public class Menu {

    @PrimaryKey
    @NonNull
    private String id;

    private String title;

    private String type;

    @ColumnInfo(name = "num_of_columns")
    private String numOfColumns;

    @ColumnInfo(name = "id_relation")
    private int idRelation;

    private String image;

    @ColumnInfo(name = "ic_name")
    private String iconName;

    @ColumnInfo(name = "is_home_page")
    private int isHomePage;

    private String url;

    @ColumnInfo(name = "id_component")
    private int idComponent;

    private String list;

    private int position;

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumOfColumns() {
        return numOfColumns;
    }

    public void setNumOfColumns(String numOfColumns) {
        this.numOfColumns = numOfColumns;
    }

    public int getIdRelation() {
        return idRelation;
    }

    public void setIdRelation(int idRelation) {
        this.idRelation = idRelation;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public int getIsHomePage() {
        return isHomePage;
    }

    public void setIsHomePage(int isHomePage) {
        this.isHomePage = isHomePage;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getIdComponent() {
        return idComponent;
    }

    public void setIdComponent(int idComponent) {
        this.idComponent = idComponent;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
