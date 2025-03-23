package com.revature.repos;

import com.revature.models.OrderItem;

import java.util.List;

public interface OrderItemDAO extends GeneralDAO<OrderItem> {
    List<OrderItem> getUserPastOrders(int userID);
}
