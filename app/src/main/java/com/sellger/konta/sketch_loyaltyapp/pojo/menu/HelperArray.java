package com.sellger.konta.sketch_loyaltyapp.pojo.menu;

import java.util.ArrayList;

public class HelperArray {

    private ArrayList<MenuComponent> menuArray;
    private ArrayList<MenuComponent> submenuArray;

    public HelperArray(ArrayList<MenuComponent> menuArray, ArrayList<MenuComponent> submenuArray) {
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
