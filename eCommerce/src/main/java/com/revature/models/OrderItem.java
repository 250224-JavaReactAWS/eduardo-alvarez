package com.revature.models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderItem {
    private int orderItemID;
    private int orderID;
    private int productID;
    private int quantity;
    private float price;

    public OrderItem(int orderID, int productID, int quantity, float price){
        this.orderID = orderID;
        this.productID = productID;
        this.quantity = quantity;
        this.price = price;
    }

    public OrderItem(){}

    public OrderItem(ResultSet rs){
        try {
            orderItemID = rs.getInt("order_item_id");
            orderID = rs.getInt("order_id");
            productID = rs.getInt("product_id");
            quantity = rs.getInt("quantity");
            price = rs.getFloat("price");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getOrderItemID() {
        return orderItemID;
    }

    public void setOrderItemID(int orderItemID) {
        this.orderItemID = orderItemID;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
