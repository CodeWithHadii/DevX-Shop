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
    HashMap<String, Integer> cart;

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
        this.cart = new HashMap<>();
    }

    public boolean validateCustomerDetails() {
        return isValidUsername() && isValidEmail() && isValidPassword();
    }

    private boolean isValidUsername() {
        return username != null && !username.isEmpty();
    }

    private boolean isValidEmail() {
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

    public void addToCart(String productId) {
        cart.put(productId, cart.getOrDefault(productId, 0) + 1);
    }

    public boolean removeFromCart(String productId) {
        if (cart.containsKey(productId)) {
            int quantity = cart.get(productId);
            if (quantity > 1) {
                cart.put(productId, quantity - 1);
            } else {
                cart.remove(productId);
            }
            return true;
        }
        return false;
    }

    public String viewCart() {
        StringBuilder cartDetails = new StringBuilder();
        if (cart.isEmpty()) {
            return "Your cart is empty.";
        } else {
            for (String productId : cart.keySet()) {
                cartDetails.append("Product ID: ").append(productId)
                           .append(", Quantity: ").append(cart.get(productId))
                           .append("\n");
            }
        }
        return cartDetails.toString();
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
