package com.sellger.konta.sketch_loyaltyapp.data.utils;

import com.sellger.konta.sketch_loyaltyapp.data.entity.MenuComponent;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Product;

import java.util.List;

public class ProductData {

    private List<MenuComponent> components;
    private List<Product> products;

    public ProductData(List<MenuComponent> componentList, List<Product> productList) {
        components = componentList;
        products = productList;
    }

    public void setComponents(List<MenuComponent> components) { this.components = components; }
    public void setProducts(List<Product> products) { this.products = products; }

    public List<MenuComponent> getComponents() { return components; }
    public List<Product> getProducts() { return products; }
}
