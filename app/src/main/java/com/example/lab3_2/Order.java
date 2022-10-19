package com.example.lab3_2;

import java.util.Hashtable;

public class Order {

    public static Hashtable<String, Double> oilPrices;

    public String oilType;
    public double liters;
    private double price;

    public Order(String oilType, double liters) {
        if(oilPrices.keySet().contains(oilType)){
            this.oilType = oilType;
            this.liters = liters;
            this.price = liters*oilPrices.get(oilType);
        }
    }

    public double getPrice() {
        return price;
    }
}
