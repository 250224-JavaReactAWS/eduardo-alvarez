package com.revature.models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PastOrder {
    private int orderID, productID, quantity;
    private float totalPrice, price;

    public PastOrder(){

    }

    public PastOrder(ResultSet rs) {
        try {
            orderID = rs.getInt("order_id");
            productID = rs.getInt("product_id");
            quantity = rs.getInt("quantity");
            totalPrice = rs.getFloat("total_price");
            price = rs.getFloat("price");
        } catch (SQLException e) {
            System.out.println("Es aqui");
            e.printStackTrace();
        }
    }
}
