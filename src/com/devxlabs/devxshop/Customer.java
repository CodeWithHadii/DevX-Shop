package com.devxlabs.devxshop;

import java.util.HashMap;

public class Customer {
    String id;
    String name;
    String contact;
    String email;
    String address;
    String profilePic;
    String username;
    String password;
    HashMap<String, String> wishlist;

    public Customer(String id, String name, String contact, String email, String address, String profilePic, String username, String password) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.email = email;
        this.address = address;
        this.profilePic = profilePic;
        this.username = username;
        this.password = password;
        this.wishlist = new HashMap<>();
    }

    public boolean validateCustomerDetails() {
        return isValidUsername() && isValidEmail() && isValidPassword();
    }

    private boolean isValidUsername() {
        return username != null && !username.isEmpty();
    }

    private boolean isValidEmail() {
        // Simple email regex check
        return email != null && email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    }

    private boolean isValidPassword() {
        return password != null && password.length() >= 6;
    }

    public void addToWishlist(String productId) {
        if (!wishlist.containsKey(productId)) {
            wishlist.put(productId, "Product Name");
        }
    }

    public boolean removeFromWishlist(String productId) {
        if (wishlist.containsKey(productId)) {
            wishlist.remove(productId);
            return true;
        }
        return false;
    }

    public void updateProfile(String name, String contact, String email, String address, String profilePic, String username, String password) {
        this.name = name;
        this.contact = contact;
        this.email = email;
        this.address = address;
        this.profilePic = profilePic;
        this.username = username;
        this.password = password;
    }
}
