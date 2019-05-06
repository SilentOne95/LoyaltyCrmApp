package com.sellger.konta.sketch_loyaltyapp.data.utils;

import com.sellger.konta.sketch_loyaltyapp.data.entity.MenuComponent;

import java.util.ArrayList;

public class HelperMenuArray {

    private ArrayList<MenuComponent> menuArray;
    private ArrayList<MenuComponent> submenuArray;

    public HelperMenuArray(ArrayList<MenuComponent> menuArray, ArrayList<MenuComponent> submenuArray) {
        this.menuArray = menuArray;
        this.submenuArray = submenuArray;
    }

    public ArrayList<MenuComponent> getMenuArray() {
        return menuArray;
    }

    public void setMenuArray(ArrayList<MenuComponent> menuArray) {
        this.menuArray = menuArray;
    }

    public ArrayList<MenuComponent> getSubmenuArray() {
        return submenuArray;
    }

    public void setSubmenuArray(ArrayList<MenuComponent> submenuArray) {
        this.submenuArray = submenuArray;
    }
}
