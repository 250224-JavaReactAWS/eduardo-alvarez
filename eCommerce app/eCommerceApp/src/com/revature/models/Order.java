package com.revature.models;

public class Order {
    private int orderID;
    private int userID;
    private  float totalPrice;
    private OrderStatus status;

    public Order(int userID, int totalPrice){
        this.userID = userID;
        this.totalPrice = totalPrice;
        status = OrderStatus.PENDING;
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
