package com.revature.models;

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
}
