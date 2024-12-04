
# DevxShop

![Version Badge](https://img.shields.io/badge/Version-1.6-aqua?style=flat-square&logo=github) ![License Badge](https://img.shields.io/badge/License-CC%20BY%20NC%20SA%204.0-blue?style=flat-square&logo=creativecommons) ![Issues Badge](https://img.shields.io/github/issues/CodeWithHadii/DevX-Shop?style=flat-square&color=red&logo=github) ![Forks Badge](https://img.shields.io/github/forks/CodeWithHadii/DevX-Shop?style=social) ![Stars Badge](https://img.shields.io/github/stars/CodeWithHadii/DevX-Shop?style=social&logo=github) ![Contributors Badge](https://img.shields.io/github/contributors/CodeWithHadii/DevX-Shop?style=flat-square&color=green) ![Last Commit Badge](https://img.shields.io/github/last-commit/CodeWithHadii/DevX-Shop?style=flat-square&color=purple&logo=github) ![PRs Badge](https://img.shields.io/github/issues-pr/CodeWithHadii/DevX-Shop?style=flat-square&color=blue) ![Pull Request Badge](https://img.shields.io/github/issues-pr-closed/CodeWithHadii/DevX-Shop?style=flat-square&color=brightgreen)



## Overview

DevxShop is an extension for App Inventor, designed to manage an online store with features like product management, customer registration, login, and wishlist functionality. It offers an easy way to add, update, and remove products, as well as manage customer details and interactions.

## Features

- **Product Management**: Add, update, search, and remove products.
- **Customer Management**: Register, update, search customers, and handle logins.
- **Wishlist**: Add/remove products to/from a customer's wishlist.
- **Password Security**: Hash and verify passwords securely using BCrypt.
- **Category Search**: Search products by category or price range.

## Installation

1. Download the extension file `DevxShop.aix`.
2. Open your App Inventor project.
3. Go to the "Extensions" section and click "Import Extension".
4. Select the `DevxShop.aix` file to add it to your project.

## Functions

- **AddProduct(name, description, image, price, stock, unit, category)**: Add a new product.
- **UpdateProduct(id, name, description, image, price, stock, unit, category)**: Update product details.
- **SearchProductsByCategory(category)**: Search products by category.
- **RegisterCustomer(name, contact, email, address, profilePic, username, password)**: Register a new customer.
- **LoginCustomer(username, password)**: Log in a customer.
- **AddToWishlist(customerId, productId)**: Add a product to the customer's wishlist.

## Events

- **ProductAdded**: Triggered when a product is added.
- **ProductUpdated**: Triggered when a product is updated.
- **CustomerAdded**: Triggered when a customer is registered.
- **CustomerLoggedIn**: Triggered when a customer logs in successfully.
- **ProductAddedToWishlist**: Triggered when a product is added to a customer's wishlist.

**and much mooooore**

## Dependencies

- BCrypt for password hashing.

## License

This project is licensed under the **Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)** License.

- **You are free to**:
  - Share: Copy and redistribute the material in any medium, format, or medium.
  - Adapt: Remix, transform, and build upon the material for any non-commercial purpose.

- **You must**:
  - **Attribution**: You must give appropriate credit, provide a link to the license, and indicate if changes were made. You may do so in any reasonable manner, but not in any way that suggests the licensor endorses you or your use.
  - **Non-commercial use**: You may not use the material for commercial purposes.
  - **Share alike**: If you remix, transform, or build upon the material, you must distribute your contributions under the same license.

For more details, see the full [license](https://creativecommons.org/licenses/by-nc-sa/4.0/).
