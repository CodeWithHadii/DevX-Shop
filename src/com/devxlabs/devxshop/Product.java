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
    int likes;
    int views;

    public Product(String id, String name, String description, String image, double price, int stock, String unit, String category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.price = price;
        this.stock = stock;
        this.unit = unit;
        this.category = category;
        this.likes = 0;
        this.views = 0;
    }

    public boolean validateProductDetails() {
        return isValidName() && isValidPrice() && isValidStock() && isValidCategory();
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

    private boolean isValidCategory() {
        return category != null && !category.isEmpty();
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

    public void incrementViews() {
        this.views++;
    }

    public void incrementLikes() {
        this.likes++;
    }

    public String getProductDetails() {
        return String.format("Product ID: %s\nName: %s\nDescription: %s\nImage: %s\nPrice: %.2f\nStock: %d\nUnit: %s\nCategory: %s\nViews: %d\nLikes: %d",
                id, name, description, image, price, stock, unit, category, views, likes);
    }

    @Override
    public String toString() {
        return String.format("Product{id='%s', name='%s', price=%.2f, stock=%d, category='%s'}", id, name, price, stock, category);
    }
}
