package com.revature.repos;

import com.revature.models.Order;

import java.util.List;

public interface OrderDAO extends GeneralDAO<Order>{
    Order updateOrderStatus(Order order);
    List<Order> filterOrdersByStatus(String status);
}
