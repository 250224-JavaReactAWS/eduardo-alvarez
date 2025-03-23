package com.revature.services;

import com.revature.models.OrderItem;
import com.revature.repos.OrderItemDAO;

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
}
