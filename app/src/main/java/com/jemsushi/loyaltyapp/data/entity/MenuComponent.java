package com.jemsushi.loyaltyapp.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "menu_table")
public class MenuComponent {

    @PrimaryKey
    @SerializedName("id")
    @Expose
    @ColumnInfo(name = "id")
    private Integer mId;

    @SerializedName("component_title")
    @Expose
    @ColumnInfo(name = "component_title")
    private String mComponentTitle;

    @SerializedName("type")
    @Expose
    @ColumnInfo(name = "type")
    private String mType;

    @SerializedName("number_of_columns")
    @Expose
    @ColumnInfo(name = "number_of_columns")
    private Integer mNumberOfColumns;

    @SerializedName("id_relation")
    @Expose
    @ColumnInfo(name = "id_relation")
    private Integer mIdRelation;

    @SerializedName("image")
    @Expose
    @ColumnInfo(name = "image")
    private String mImage;

    @SerializedName("icon_name")
    @Expose
    @ColumnInfo(name = "icon_name")
    private String mIconName;

    @SerializedName("is_home_page")
    @Expose
    @ColumnInfo(name = "is_home_page")
    private Integer mIsHomePage;

    @SerializedName("url")
    @Expose
    @ColumnInfo(name = "url")
    private String mUrl;

    @SerializedName("id_component")
    @Expose
    @ColumnInfo(name = "id_component")
    private Integer mIdComponent;

    @SerializedName("list")
    @Expose
    @ColumnInfo(name = "list")
    private String mList;

    @SerializedName("position")
    @Expose
    @ColumnInfo(name = "position")
    private Integer mPosition;

    public Integer getId() {
        return mId;
    }
    public void setId(Integer id) {
        mId = id;
    }

    public String getComponentTitle() {
        return mComponentTitle;
    }
    public void setComponentTitle(String componentTitle) {
        mComponentTitle = componentTitle;
    }

    public String getType() {
        return mType;
    }
    public void setType(String type) {
        mType = type;
    }

    public Integer getNumberOfColumns() {
        return mNumberOfColumns;
    }
    public void setNumberOfColumns(Integer numberOfColumns) {
        mNumberOfColumns = numberOfColumns;
    }

    public Integer getIdRelation() {
        return mIdRelation;
    }
    public void setIdRelation(Integer idRelation) {
        mIdRelation = idRelation;
    }

    public String getImage() {
        return mImage;
    }
    public void setImage(String image) {
        mImage = image;
    }

    public String getIconName() {
        return mIconName;
    }
    public void setIconName(String iconName) {
        mIconName = iconName;
    }

    public Integer getIsHomePage() {
        return mIsHomePage;
    }
    public void setIsHomePage(Integer isHomePage) {
        mIsHomePage = isHomePage;
    }

    public String getUrl() { return mUrl; }
    public void setUrl(String url) { mUrl = url; }

    public Integer getIdComponent() {
        return mIdComponent;
    }
    public void setIdComponent(Integer idComponent) {
        mIdComponent = idComponent;
    }

    public String getList() {
        return mList;
    }
    public void setList(String list) {
        mList = list;
    }

    public Integer getPosition() {
        return mPosition;
    }
    public void setPosition(Integer position) {
        mPosition = position;
    }
}
