package com.revature.models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Order {
    private int orderID;
    private int userID;
    private  float totalPrice;
    private OrderStatus status;

    public Order(int userID, float totalPrice){
        this.userID = userID;
        this.totalPrice = totalPrice;
        status = OrderStatus.PENDING;
    }

    public Order(){
    }

    public Order(ResultSet rs){
        try{
            orderID=rs.getInt("order_id");
            userID=rs.getInt("user_id");
            totalPrice=rs.getFloat("total_price");
            status=OrderStatus.valueOf(rs.getString("status"));
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
