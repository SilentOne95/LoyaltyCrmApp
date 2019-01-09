package com.example.konta.sketch_loyalityapp.data.menu;

public class HelperComponent {
    private String valOne, valTwo;

    public HelperComponent(String v1, String v2) {
        valOne = v1;
        valTwo = v2;
    }

    public String getValOne() { return valOne; }
    public String getValTwo() { return valTwo; }

    public void setValOne(String v1) { valOne = v1; }
    public void setValTwo(String v2) { valTwo = v2; }
}
