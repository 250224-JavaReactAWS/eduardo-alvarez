package com.revature.services;

import com.revature.models.Product;
import com.revature.repos.ProductDAOPostgres;

import java.util.List;

public class ProductService {
    private ProductDAOPostgres productDAO;

    public ProductService(ProductDAOPostgres productDAO) {
        this.productDAO = productDAO;
    }

    public Product registerNewProduct(Product requestProduct) {
        return productDAO.create(requestProduct);
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

    public Product updateProduct(Product requestProduct){
        Product updatedProduct = new Product();
        Product currentUser = productDAO.getByID(requestProduct.getProductID());
        updatedProduct.setProductID(currentUser.getProductID());
        //Name
        if (requestProduct.getName() == null || requestProduct.getName().isBlank()) {
            updatedProduct.setName(currentUser.getName());
        }
        else {
            updatedProduct.setName(requestProduct.getName());
        }
        //Price
        if (requestProduct.getPrice()<0) {
            updatedProduct.setPrice(currentUser.getPrice());
        }
        else {
            updatedProduct.setPrice(requestProduct.getPrice());
        }
        //Stock
        if (requestProduct.getStock()<0) {
            updatedProduct.setStock(currentUser.getStock());
        }
        else {
            updatedProduct.setStock(requestProduct.getStock());
        }
        updatedProduct = productDAO.update(updatedProduct);
        return updatedProduct;
    }

    public Product getProductByID(int ID){
        return productDAO.getByID(ID);
    }

    public boolean deleteProduct(int id){
        return productDAO.deleteById(id);
    }
}
