package com.revature.models;

public enum OrderStatus {
    PENDING,
    SHIPPED,
    DELIVERED;

    public static boolean contains(String s) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.name().equals(s)) {
                return true;
            }
        }
        return false;
    }
}
