package com.revature.models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Product {
    private int productID;
    private String name;
    private String description;
    private float price;
    private int stock;

    public Product() {
    }

    public Product(int productID, String name, String description, float price, int stock) {
        this.productID = productID;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
    }

    public Product(String productName, String productDescription, float productPrice, int stock) {
        name = productName;
        description = productDescription;
        price = productPrice;
        this.stock = stock;
    }


    public Product(ResultSet rs) {
        try {
            productID=rs.getInt("product_id");
            name = rs.getString("name");
            description = rs.getString("description");
            price = rs.getFloat("price");
            stock = rs.getInt("stock");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Algo salio mal al crear product con set result");
        }
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

}
