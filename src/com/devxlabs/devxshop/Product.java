package com.devxlabs.devxshop;

public class Product {
    String id;
    String name;
    String description;
    String image;
    double price;
    int stock;
    String unit;
    String category;

    public Product(String id, String name, String description, String image, double price, int stock, String unit, String category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.price = price;
        this.stock = stock;
        this.unit = unit;
        this.category = category;
    }

    public boolean validateProductDetails() {
        return isValidName() && isValidPrice() && isValidStock();
    }

    private boolean isValidName() {
        return name != null && !name.isEmpty();
    }

    private boolean isValidPrice() {
        return price >= 0;
    }

    private boolean isValidStock() {
        return stock >= 0;
    }

    void updateProduct(String name, String description, String image, double price, int stock, String unit, String category) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.price = price;
        this.stock = stock;
        this.unit = unit;
        this.category = category;
    }
}
