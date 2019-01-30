package com.example.konta.sketch_loyalityapp.pojo.adapter;

import com.example.konta.sketch_loyalityapp.pojo.menu.MenuComponent;
import com.example.konta.sketch_loyalityapp.pojo.product.Product;

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
