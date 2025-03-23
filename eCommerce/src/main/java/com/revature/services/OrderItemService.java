package com.revature.services;

import com.revature.models.OrderItem;
import com.revature.models.PastOrder;
import com.revature.repos.OrderItemDAO;

import java.util.List;

public class OrderItemService {
    private final OrderItemDAO orderItemDAO;
    private final UserService userService;

    public OrderItemService(OrderItemDAO orderItemDAO, UserService userService) {
        this.orderItemDAO = orderItemDAO;
        this.userService = userService;
    }

    public OrderItem registerOrderItem(OrderItem orderItem){
        return orderItemDAO.create(orderItem);
    }

    public List<OrderItem> getPastOrders(int userID){
        return orderItemDAO.getUserPastOrders(userID);
    }
}
