package com.revature.services;

import com.revature.models.Product;
import com.revature.repos.ProductDAO;

public class ProductService {
    private ProductDAO productDAO;

    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public Product registerNewUser(String name, String description, float price, int stock) {

        Product newUser = new Product(name, description, price, stock);
        return productDAO.create(newUser);
    }

    public boolean validatePrice(float price) {
        return price >= 0;
    }
    public boolean validateStock(int stock) {
        return stock >= 0;
    }
    public boolean validateName(String name){
        return name.isBlank();
    }
}
