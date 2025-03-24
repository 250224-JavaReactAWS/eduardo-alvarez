package com.revature.models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CartItem {
    private int cartItemID;
    private int userID;
    private int productID;
    private  int quantity;

    public CartItem(int userID, int productID, int quantity){
        this.userID = userID;
        this.productID = productID;
        this.quantity=quantity;
    }

    public CartItem(){
    }

    public CartItem(int cartItemID, int userID, int productID, int quantity) {
        this.cartItemID = cartItemID;
        this.userID = userID;
        this.productID = productID;
        this.quantity = quantity;
    }

    public CartItem(ResultSet rs){
        try {
            userID=rs.getInt("user_id");
            cartItemID=rs.getInt("cart_item_id");
            productID=rs.getInt("product_id");
            quantity = rs.getInt("quantity");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public int getCartItemID() {
        return cartItemID;
    }

    public void setCartItemID(int cartItemID) {
        this.cartItemID = cartItemID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
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
}
