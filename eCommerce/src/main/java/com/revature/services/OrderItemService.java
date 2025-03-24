package com.revature.services;

import com.revature.models.OrderItem;
import com.revature.repos.OrderItemDAO;

import java.util.List;

public class OrderItemService {
    private final OrderItemDAO orderItemDAO;

    public OrderItemService(OrderItemDAO orderItemDAO) {
        this.orderItemDAO = orderItemDAO;
    }

    public OrderItem registerOrderItem(OrderItem orderItem){
        return orderItemDAO.create(orderItem);
    }

    public List<OrderItem> getPastOrders(int userID){
        return orderItemDAO.getUserPastOrders(userID);
    }
}
