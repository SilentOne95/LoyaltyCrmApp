package com.example.konta.sketch_loyalityapp.modelClasses.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MenuList {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("component_title")
    @Expose
    private String componentTitle;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("number_of_columns")
    @Expose
    private Integer numberOfColumns;
    @SerializedName("id_relation")
    @Expose
    private Object idRelation;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("icon_name")
    @Expose
    private String iconName;
    @SerializedName("is_home_page")
    @Expose
    private Integer isHomePage;
    @SerializedName("id_component")
    @Expose
    private Integer idComponent;
    @SerializedName("list")
    @Expose
    private String list;
    @SerializedName("position")
    @Expose
    private Integer position;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getComponentTitle() {
        return componentTitle;
    }

    public void setComponentTitle(String componentTitle) {
        this.componentTitle = componentTitle;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getNumberOfColumns() {
        return numberOfColumns;
    }

    public void setNumberOfColumns(Integer numberOfColumns) {
        this.numberOfColumns = numberOfColumns;
    }

    public Object getIdRelation() {
        return idRelation;
    }

    public void setIdRelation(Object idRelation) {
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

    public Integer getIsHomePage() {
        return isHomePage;
    }

    public void setIsHomePage(Integer isHomePage) {
        this.isHomePage = isHomePage;
    }

    public Integer getIdComponent() {
        return idComponent;
    }

    public void setIdComponent(Integer idComponent) {
        this.idComponent = idComponent;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

}
