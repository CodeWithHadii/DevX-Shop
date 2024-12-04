package com.devxlabs.devxshop;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import org.mindrot.jbcrypt.BCrypt;


import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.util.HashMap;

import com.devxlabs.devxshop.Product;
import com.devxlabs.devxshop.Customer;


@DesignerComponent(
    version = 1,
    versionName = "1.6",
    description = "An E-Commerce Extension for MIT AI2 and its derivatives",
    iconName = "icon.jpeg"
)
public class DevxShop extends AndroidNonvisibleComponent {
    private HashMap < String, Product > products;
    private HashMap < String, String > productNames;
    private HashMap < String, Customer > customers;
    private HashMap < String, String > customerUsernames;
    private HashMap < String, String > wishlist;

    private int productIdCounter = 1;
    private int customerIdCounter = 1;

    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_DESCRIPTION_LENGTH = 200;

    public DevxShop(ComponentContainer container) {
        super(container.$form());
        products = new HashMap < > ();
        customers = new HashMap < > ();
        productNames = new HashMap < > ();
        customerUsernames = new HashMap < > ();
    }

    // ********************* Products Section *********************
    @SimpleFunction(description = "Adds a new product to the shop.")
    public void AddProduct(String name, String description, String image, double price, int stock, String unit, String category) {
        if (name.isEmpty() || price < 0 || stock < 0) {
            InvalidProductDetails(name);
            return;
        }
        if (productNames.containsKey(name)) {
            ProductAlreadyExists(name);
        } else {
            String id = "PROD-" + productIdCounter++;
            products.put(id, new Product(id, name, description, image, price, stock, unit, category));
            productNames.put(name, id);
            ProductAdded(id, name, description, image, price, stock, unit, category);
        }
    }

    @SimpleEvent(description = "Event triggered when invalid product details are provided.")
    public void InvalidProductDetails(String name) {
        EventDispatcher.dispatchEvent(this, "InvalidProductDetails", name);
    }

    @SimpleEvent(description = "Event triggered when a product is added successfully.")
    public void ProductAdded(String id, String name, String description, String image, double price, int stock, String unit, String category) {
        EventDispatcher.dispatchEvent(this, "ProductAdded", id, name, description, image, price, stock, unit, category);
    }

    @SimpleEvent(description = "Event triggered when a product already exists.")
    public void ProductAlreadyExists(String name) {
        EventDispatcher.dispatchEvent(this, "ProductAlreadyExists", name);
    }

    @SimpleFunction(description = "Search products by category.")
    public String SearchProductsByCategory(String category) {
        JSONArray jsonArray = new JSONArray();
        for (Product product: products.values()) {
            if (category.isEmpty() || product.category.equalsIgnoreCase(category)) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("id", product.id);
                    jsonObject.put("name", product.name);
                    jsonObject.put("price", product.price);
                    jsonObject.put("unit", product.unit);
                    jsonObject.put("category", product.category);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                jsonArray.put(jsonObject);
            }
        }
        return jsonArray.toString();
    }

    @SimpleFunction(description = "Update product details by ID.")
    public void UpdateProduct(String id, String name, String description, String image, double price, int stock, String unit, String category) {
        if (products.containsKey(id)) {
            products.get(id).updateProduct(name, description, image, price, stock, unit, category);
            ProductUpdated(id, name, description, image, price, stock, unit, category);
        } else {
            ProductNotFound(id);
        }
    }

    @SimpleEvent(description = "Event triggered when a product is updated.")
    public void ProductUpdated(String id, String name, String description, String image, double price, int stock, String unit, String category) {
        EventDispatcher.dispatchEvent(this, "ProductUpdated", id, name, description, image, price, stock, unit, category);
    }

    @SimpleEvent(description = "Event triggered when a product is not found by ID.")
    public void ProductNotFound(String id) {
        EventDispatcher.dispatchEvent(this, "ProductNotFound", id);
    }

    @SimpleFunction(description = "Remove a product by ID.")
    public void RemoveProduct(String id) {
        if (products.containsKey(id)) {
            productNames.remove(products.get(id).name);
            products.remove(id);
            ProductRemoved(id);
        } else {
            ProductNotFound(id);
        }
    }

    @SimpleEvent(description = "Event triggered when a product is removed successfully.")
    public void ProductRemoved(String id) {
        EventDispatcher.dispatchEvent(this, "ProductRemoved", id);
    }

    @SimpleFunction(description = "Get details of all products as a JSON string.")
    public String GetAllProducts() {
        JSONArray jsonArray = new JSONArray();

        if (products.isEmpty()) {
            NoProductsFound();
            return jsonArray.toString();
        } else {
            for (Product product: products.values()) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("id", product.id);
                    jsonObject.put("name", product.name);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                jsonArray.put(jsonObject);
            }
            return jsonArray.toString();
        }
    }

    @SimpleEvent(description = "Event triggered when no products are found.")
    public void NoProductsFound() {
        EventDispatcher.dispatchEvent(this, "NoProductsFound");
    }

    @SimpleFunction(description = "Remove all products from the shop.")
    public void RemoveAllProducts() {
        products.clear();
        productNames.clear();
        AllProductsRemoved();
    }

    @SimpleEvent(description = "Event triggered when all products are removed.")
    public void AllProductsRemoved() {
        EventDispatcher.dispatchEvent(this, "AllProductsRemoved");
    }

    @SimpleFunction(description = "Search product by ID.")
    public void SearchProduct(String id) {
        if (products.containsKey(id)) {
            Product product = products.get(id);
            ProductFound(product.id, product.name, product.description, product.image, product.price, product.stock, product.unit);
        } else {
            ProductNotFound(id);
        }
    }

    @SimpleEvent(description = "Event triggered when a product is found.")
    public void ProductFound(String id, String name, String description, String image, double price, int stock, String unit) {
        EventDispatcher.dispatchEvent(this, "ProductFound", id, name, description, image, price, stock, unit);
    }

    // ********************* Customers Section *********************
    @SimpleFunction(description = "Adds a new Customer")
    public void RegisterCustomer(String name, String contact, String email, String address, String profilePic, String username, String password) {
        if (name.isEmpty() || username.isEmpty() || password.length() < 6) {
            InvalidCustomerDetails(username);
            return;
        }
        if (customerUsernames.containsKey(username)) {
            CustomerAlreadyExists(username);
        } else {
            String id = "CUST-" + customerIdCounter++;
            String hashedPassword = hashPassword(password);
            customers.put(id, new Customer(id, name, contact, email, address, profilePic, username, hashedPassword));
            customerUsernames.put(username, id);
            CustomerAdded(id, name, contact, email, address, username, hashedPassword);
        }
    }

    @SimpleEvent(description = "Event triggered when invalid customer details are provided.")
    public void InvalidCustomerDetails(String username) {
        EventDispatcher.dispatchEvent(this, "InvalidCustomerDetails", username);
    }

    @SimpleFunction(description = "Search for a customer by ID.")
    public void SearchCustomer(String id) {
        if (customers.containsKey(id)) {
            Customer customer = customers.get(id);
            CustomerFound(customer.id, customer.name, customer.contact, customer.email, customer.address, customer.profilePic, customer.username);
        } else {
            CustomerNotFound(id);
        }
    }

    @SimpleEvent(description = "Event triggered when a customer is found.")
    public void CustomerFound(String id, String name, String contact, String email, String address, String profilePic, String username) {
        EventDispatcher.dispatchEvent(this, "CustomerFound", id, name, contact, email, address, profilePic, username);
    }


    @SimpleFunction(description = "Search products by category and price range.")
    public String SearchProducts(String category, double minPrice, double maxPrice) {
        JSONArray jsonArray = new JSONArray();
        for (Product product: products.values()) {
            if ((category.isEmpty() || product.unit.equalsIgnoreCase(category)) &&
                product.price >= minPrice && product.price <= maxPrice) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("id", product.id);
                    jsonObject.put("name", product.name);
                    jsonObject.put("price", product.price);
                    jsonObject.put("unit", product.unit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                jsonArray.put(jsonObject);
            }
        }
        return jsonArray.toString();
    }


    @SimpleEvent(description = "Event triggered when a customer is added.")
    public void CustomerAdded(String id, String name, String contact, String email, String address, String username, String password) {
        EventDispatcher.dispatchEvent(this, "CustomerAdded", id, name, contact, email, address, username, password);
    }

    @SimpleEvent(description = "Event triggered when a customer already exists.")
    public void CustomerAlreadyExists(String username) {
        EventDispatcher.dispatchEvent(this, "CustomerAlreadyExists", username);
    }

    @SimpleFunction(description = "Login a customer by username and password.")
    public void LoginCustomer(String username, String password) {
        for (Customer customer: customers.values()) {
            if (customer.username.equals(username) && verifyPassword(password, customer.password)) {
                CustomerLoggedIn(customer.id, username);
                return;
            }
        }
        CustomerLoginFailed(username);
    }

    @SimpleEvent(description = "Event triggered when a customer logs in successfully.")
    public void CustomerLoggedIn(String id, String username) {
        EventDispatcher.dispatchEvent(this, "CustomerLoggedIn", id, username);
    }

    @SimpleEvent(description = "Event triggered when customer login fails.")
    public void CustomerLoginFailed(String username) {
        EventDispatcher.dispatchEvent(this, "CustomerLoginFailed", username);
    }

    @SimpleFunction(description = "Get details of all customers as a JSON string.")
    public String GetAllCustomers() {
        JSONArray jsonArray = new JSONArray();

        if (customers.isEmpty()) {
            NoCustomersFound();
            return jsonArray.toString();
        } else {
            for (Customer customer: customers.values()) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("id", customer.id);
                    jsonObject.put("name", customer.name);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                jsonArray.put(jsonObject);
            }
            return jsonArray.toString();
        }
    }

    @SimpleEvent(description = "Event triggered when no customers are found.")
    public void NoCustomersFound() {
        EventDispatcher.dispatchEvent(this, "NoCustomersFound");
    }

    @SimpleFunction(description = "Update customer profile by ID.")
    public void UpdateCustomerProfile(String id, String name, String contact, String email, String address, String profilePic, String username, String password) {
        if (customers.containsKey(id)) {
            String hashedPassword = hashPassword(password);
            customers.get(id).updateProfile(name, contact, email, address, profilePic, username, hashedPassword);
            ProfileUpdated(id, name, contact, email, address, username, hashedPassword);
        } else {
            CustomerNotFound(id);
        }
    }


    @SimpleEvent(description = "Event triggered when customer profile is updated.")
    public void ProfileUpdated(String id, String name, String contact, String email, String address, String username, String Password) {
        EventDispatcher.dispatchEvent(this, "ProfileUpdated", id, name, contact, email, address, username, Password);
    }

    @SimpleEvent(description = "Event triggered when customer is not found.")
    public void CustomerNotFound(String id) {
        EventDispatcher.dispatchEvent(this, "CustomerNotFound", id);
    }

    // ********************* Password Hashing with BCrypt *********************

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    private boolean verifyPassword(String password, String storedPassword) {
        return BCrypt.checkpw(password, storedPassword);
    }
    // ********************* Wishlist Options *********************

    @SimpleFunction(description = "Add a product to the customer's wishlist")
    public void AddToWishlist(String customerId, String productId) {
        if (!customers.containsKey(customerId)) {
            CustomerNotFound(customerId);
            return;
        }

        Customer customer = customers.get(customerId);
        if (products.containsKey(productId)) {
            customer.addToWishlist(productId);
            AddedToWishlist(customerId, productId);
        } else {
            ProductNotFound(productId);
        }
    }

    @SimpleEvent(description = "Event triggered when a product is added to the customer's wishlist.")
    public void AddedToWishlist(String customerId, String productId) {
        EventDispatcher.dispatchEvent(this, "ProductAddedToWishlist", customerId, productId);
    }

    @SimpleFunction(description = "Remove a product from the customer's wishlist")
    public void RemoveFromWishlist(String customerId, String productId) {
        if (!customers.containsKey(customerId)) {
            CustomerNotFound(customerId);
            return;
        }

        Customer customer = customers.get(customerId);
        if (customer.removeFromWishlist(productId)) {
            RemovedFromWishlist(customerId, productId);
        } else {
            NotFoundInWishlist(productId);
        }
    }

    @SimpleEvent(description = "Event triggered when a product is removed from the customer's wishlist.")
    public void RemovedFromWishlist(String customerId, String productId) {
        EventDispatcher.dispatchEvent(this, "ProductRemovedFromWishlist", customerId, productId);
    }

    @SimpleEvent(description = "Event triggered when a product is not found in the wishlist.")
    public void NotFoundInWishlist(String productId) {
        EventDispatcher.dispatchEvent(this, "NotFoundInWishlist", productId);
    }

    @SimpleFunction(description = "Get all items in the customer's wishlist")
    public String GetCustomerWishlist(String customerId) {
        if (!customers.containsKey(customerId)) {
            CustomerNotFound(customerId);
            return "[]";
        }
        Customer customer = customers.get(customerId);
        JSONArray jsonArray = new JSONArray();
        for (String productId: customer.wishlist.keySet()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("productId", productId);
            jsonObject.put("productName", customer.wishlist.get(productId));
            jsonArray.put(jsonObject);
        }
        return jsonArray.toString();
    }

    // ********************* Cart Options *********************

    @SimpleFunction(description = "Add a product to the customer's shopping cart")
    public void AddToCart(String customerId, String productId, int quantity) {
        if (!customers.containsKey(customerId)) {
            CustomerNotFound(customerId);
            return;
        }

        if (!products.containsKey(productId)) {
            ProductNotFound(productId);
            return;
        }

        Product product = products.get(productId);

        if (quantity <= 0) {
            InvalidQuantity(quantity);
            return;
        }

        if (quantity > product.stock) {
            InsufficientStock(productId, product.stock);
            return;
        }

        Customer customer = customers.get(customerId);

        customer.cart.put(productId, customer.cart.getOrDefault(productId, 0) + quantity);
        ProductAddedToCart(customerId, productId, quantity);
    }

    @SimpleEvent(description = "Event triggered when there is insufficient stock for the product.")
    public void InsufficientStock(String productId, int availableStock) {
        EventDispatcher.dispatchEvent(this, "InsufficientStock", productId, availableStock);
    }


    @SimpleEvent(description = "Event triggered when a product is added to the shopping cart.")
    public void ProductAddedToCart(String customerId, String productId, int quantity) {
        EventDispatcher.dispatchEvent(this, "ProductAddedToCart", customerId, productId, quantity);
    }

    @SimpleFunction(description = "Remove a product from the customer's shopping cart")
    public void RemoveFromCart(String customerId, String productId) {
        if (!customers.containsKey(customerId)) {
            CustomerNotFound(customerId);
            return;
        }

        Customer customer = customers.get(customerId);

        if (customer.cart.containsKey(productId)) {
            customer.cart.remove(productId);
            ProductRemovedFromCart(customerId, productId);
        } else {
            ProductNotInCart(productId);
        }
    }

    @SimpleEvent(description = "Event triggered when a product is removed from the shopping cart.")
    public void ProductRemovedFromCart(String customerId, String productId) {
        EventDispatcher.dispatchEvent(this, "ProductRemovedFromCart", customerId, productId);
    }

    @SimpleEvent(description = "Event triggered when a product is not found in the cart.")
    public void ProductNotInCart(String productId) {
        EventDispatcher.dispatchEvent(this, "ProductNotInCart", productId);
    }

    @SimpleFunction(description = "View all products in the customer's shopping cart")
    public String ViewCart(String customerId) {
        if (!customers.containsKey(customerId)) {
            CustomerNotFound(customerId);
            return "[]";
        }

        Customer customer = customers.get(customerId);
        JSONArray jsonArray = new JSONArray();

        if (customer.cart.isEmpty()) {
            CartEmpty(customerId);
            return jsonArray.toString();
        }

        for (String productId: customer.cart.keySet()) {
            Product product = products.get(productId);
            int quantity = customer.cart.get(productId);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("productId", product.id);
                jsonObject.put("productName", product.name);
                jsonObject.put("quantity", quantity);
                jsonObject.put("price", product.price);
                jsonObject.put("totalPrice", product.price * quantity);
            } catch (Exception e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
        }

        return jsonArray.toString();
    }

    @SimpleEvent(description = "Event triggered when a customer's shopping cart is empty.")
    public void CartEmpty(String customerId) {
        EventDispatcher.dispatchEvent(this, "CartEmpty", customerId);
    }

    @SimpleFunction(description = "Clear all products from the customer's shopping cart")
    public void ClearCart(String customerId) {
        if (!customers.containsKey(customerId)) {
            CustomerNotFound(customerId);
            return;
        }

        Customer customer = customers.get(customerId);
        customer.cart.clear();
        CartCleared(customerId);
    }

    @SimpleEvent(description = "Event triggered when a customer's shopping cart is cleared.")
    public void CartCleared(String customerId) {
        EventDispatcher.dispatchEvent(this, "CartCleared", customerId);
    }

    @SimpleEvent(description = "Event triggered when an invalid quantity is provided.")
    public void InvalidQuantity(int quantity) {
        EventDispatcher.dispatchEvent(this, "InvalidQuantity", quantity);
    }
}
