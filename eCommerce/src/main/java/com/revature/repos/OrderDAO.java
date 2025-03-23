package com.revature.repos;

import com.revature.models.Order;

public interface OrderDAO extends GeneralDAO<Order>{
    Order updateOrderStatus(Order order);
}
