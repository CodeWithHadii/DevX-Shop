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

@DesignerComponent(
	version = 1,
	versionName = "1.0",
	description = "Extension component for DevxShop. Created using Rush.",
	iconName = "icon.jpeg"
)
public class DevxShop extends AndroidNonvisibleComponent {
    private HashMap<String, Product> products;
    private HashMap<String, String> productNames;
    private HashMap<String, Customer> customers;
    private HashMap<String, String> customerUsernames;

    private int productIdCounter = 1;
    private int customerIdCounter = 1;

    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_DESCRIPTION_LENGTH = 200;

    public DevxShop(ComponentContainer container) {
        super(container.$form());
        products = new HashMap<>();
        customers = new HashMap<>();
        productNames = new HashMap<>();
        customerUsernames = new HashMap<>();
    }

    // ********************* Products Section *********************
    @SimpleFunction(description = "Adds a new product")
public void AddProduct(String name, String description, String image, double price, int stock, String unit, String category) {
    if (name.isEmpty() || price < 0 || stock < 0) {
        InvalidProductDetails(name);
        return;
    }
    if (productNames.containsKey(name)) {
        ProductAlreadyExists(name); // Check by product name
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


    @SimpleEvent(description = "Event triggered when a product is added.")
public void ProductAdded(String id, String name, String description, String image, double price, int stock, String unit, String category) {
    EventDispatcher.dispatchEvent(this, "ProductAdded", id, name, description, image, price, stock, unit, category);
}

@SimpleFunction(description = "Search products by category.")
public String SearchProductsByCategory(String category) {
    JSONArray jsonArray = new JSONArray();
    for (Product product : products.values()) {
        if (category.isEmpty() || product.category.equalsIgnoreCase(category)) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("id", product.id);
                jsonObject.put("name", product.name);
                jsonObject.put("price", product.price);
                jsonObject.put("unit", product.unit);
                jsonObject.put("category", product.category); // Include category
            } catch (Exception e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
        }
    }
    return jsonArray.toString(); // Return JSON representation
}

    @SimpleEvent(description = "Event triggered when a product already exists.")
    public void ProductAlreadyExists(String name) {
        EventDispatcher.dispatchEvent(this, "ProductAlreadyExists", name);
    }

    @SimpleFunction(description = "Update a product by ID.")
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


    @SimpleEvent(description = "Event triggered when a product is not found.")
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


    @SimpleEvent(description = "Event triggered when a product is removed.")
    public void ProductRemoved(String id) {
        EventDispatcher.dispatchEvent(this, "ProductRemoved", id);
    }

    @SimpleFunction(description = "Get details of all products as a JSON string.")
    public String GetAllProducts() {
        JSONArray jsonArray = new JSONArray();

        if (products.isEmpty()) {
            NoProductsFound();
            return jsonArray.toString();  // Return empty JSON array
        } else {
            for (Product product : products.values()) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("id", product.id);
                    jsonObject.put("name", product.name);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                jsonArray.put(jsonObject);
            }
            return jsonArray.toString(); // Return JSON representation
        }
    }

    @SimpleEvent(description = "Event triggered when no products are found.")
    public void NoProductsFound() {
        EventDispatcher.dispatchEvent(this, "NoProductsFound");
    }

    @SimpleFunction(description = "Remove all products.")
    public void RemoveAllProducts() {
        products.clear();
        productNames.clear(); // Clear the product name tracking
        AllProductsRemoved();
    }

    @SimpleEvent(description = "Event triggered when all products are removed.")
    public void AllProductsRemoved() {
        EventDispatcher.dispatchEvent(this, "AllProductsRemoved");
    }
    @SimpleFunction(description = "Search Product by ID")
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
    // Validate inputs
    if (name.isEmpty() || username.isEmpty() || password.length() < 6) {
        InvalidCustomerDetails(username);
        return;
    }
    if (customerUsernames.containsKey(username)) {
        CustomerAlreadyExists(username);
    } else {
        String id = "CUST-" + customerIdCounter++;
        String hashedPassword = hashPassword(password); // Use BCrypt hash for password
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
    for (Product product : products.values()) {
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
        for (Customer customer : customers.values()) {
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
            return jsonArray.toString();  // Return empty JSON array
        } else {
            for (Customer customer : customers.values()) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("id", customer.id);
                    jsonObject.put("name", customer.name);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                jsonArray.put(jsonObject);
            }
            return jsonArray.toString(); // Return JSON representation
        }
    }

    @SimpleEvent(description = "Event triggered when no customers are found.")
    public void NoCustomersFound() {
        EventDispatcher.dispatchEvent(this, "NoCustomersFound");
    }

    @SimpleFunction(description = "Update customer profile by ID.")
public void UpdateCustomerProfile(String id, String name, String contact, String email, String address, String profilePic, String username, String password) {
    if (customers.containsKey(id)) {
        String hashedPassword = hashPassword(password); // Hash the password before updating
        customers.get(id).updateProfile(name, contact, email, address, profilePic, username, hashedPassword);
        CustomerProfileUpdated(id, name, contact, email, address, username, hashedPassword);
    } else {
        CustomerNotFound(id);
    }
}


    @SimpleEvent(description = "Event triggered when customer profile is updated.")
    public void CustomerProfileUpdated(String id, String name, String contact, String email, String address, String username, String Password) {
        EventDispatcher.dispatchEvent(this, "CustomerProfileUpdated", id, name, contact, email, address, username, Password);
    }

    @SimpleEvent(description = "Event triggered when customer is not found.")
    public void CustomerNotFound(String id) {
        EventDispatcher.dispatchEvent(this, "CustomerNotFound", id);
    }

    // ********************* Password Hashing with LZ4 and XxHash *********************

    private String hashPassword(String password) {
    return BCrypt.hashpw(password, BCrypt.gensalt()); // Hash the password using BCrypt
}

private boolean verifyPassword(String password, String storedPassword) {
    return BCrypt.checkpw(password, storedPassword); // Compare the entered password with stored hash
}

    // Product class
    // Product class
private class Product {
    String id;
    String name;
    String description;
    String image;
    double price;
    int stock;
    String unit;
    String category; // New category field

    // Constructor to initialize Product fields
    Product(String id, String name, String description, String image, double price, int stock, String unit, String category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.price = price;
        this.stock = stock;
        this.unit = unit;
        this.category = category; // Set category during creation
    }

    // Update product details
    void updateProduct(String name, String description, String image, double price, int stock, String unit, String category) {
    this.name = name;
    this.description = description;
    this.image = image;
    this.price = price;
    this.stock = stock;
    this.unit = unit;
    this.category = category; // Update category
}

    // Validation for product fields
    boolean isValidProduct() {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Product name cannot be empty.");
            return false;
        }
        if (description == null || description.length() > 200) {
            System.out.println("Product description is either empty or exceeds the maximum allowed length.");
            return false;
        }
        if (price <= 0) {
            System.out.println("Product price must be a positive number.");
            return false;
        }
        if (stock < 0) {
            System.out.println("Product stock cannot be negative.");
            return false;
        }
        if (category == null || category.trim().isEmpty()) {
            System.out.println("Product category cannot be empty.");
            return false;
        }
        return true;
    }
}

// Customer class
private class Customer {
    String id;
    String name;
    String contact;
    String email;
    String address;
    String profilePic;
    String username;
    String password; // Store hashed password

    // Constructor to initialize Customer fields
    Customer(String id, String name, String contact, String email, String address, String profilePic, String username, String password) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.email = email;
        this.address = address;
        this.profilePic = profilePic;
        this.username = username;
        this.password = password;
    }

    // Update customer profile details
    void updateProfile(String name, String contact, String email, String address, String profilePic, String username, String password) {
        this.name = name;
        this.contact = contact;
        this.email = email;
        this.address = address;
        this.profilePic = profilePic;
        this.username = username;
        this.password = password;
    }

    // Validation for customer fields
    boolean isValidCustomer() {
        if (username == null || username.trim().isEmpty()) {
            System.out.println("Username cannot be empty.");
            return false;
        }
        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
            System.out.println("Invalid email.");
            return false;
        }
        if (password == null || password.length() < 8) {
            System.out.println("Password must be at least 8 characters long.");
            return false;
        }
        return true;
    }

    // Password hashing (BCrypt) for secure storage
    void setPassword(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // Password validation
    boolean checkPassword(String password) {
        return BCrypt.checkpw(password, this.password);
    }
}
}
