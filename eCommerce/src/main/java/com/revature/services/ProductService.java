package com.revature.services;

import com.revature.models.Product;
import com.revature.repos.ProductDAOPostgres;

import java.util.List;

public class ProductService {
    private ProductDAOPostgres productDAO;

    public ProductService(ProductDAOPostgres productDAO) {
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

    public List<Product> getAllProducts(){
        return productDAO.getAll();
    }
}
