package com.example.konta.sketch_loyalityapp.data.menu;

public class HelperComponent {
    private String type, title;

    public HelperComponent(String type, String title) {
        this.type = type;
        this.title = title;
    }

    public String getType() { return type; }
    public String getTitle() { return title; }

    public void setType(String type) { this.type = type; }
    public void setTitle(String title) { this.title = title; }
}
