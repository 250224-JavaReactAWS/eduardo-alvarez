package com.revature.services;

import com.revature.models.CartItem;
import com.revature.models.Order;
import com.revature.models.OrderStatus;
import com.revature.models.Product;
import com.revature.repos.OrderDAO;
import jdk.jshell.Snippet;

import java.util.List;

public class OrderService {
    private final OrderDAO orderDAO;

    public OrderService(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    public Order registerOrder(int userID, float totalPrice) {
        if (totalPrice < 0) {
            return null;
        }
        Order newOrder = new Order(userID, totalPrice);
        return orderDAO.create(newOrder);
    }

    public boolean validateStock(Product product, CartItem cartItem) {
        int remainStock = product.getStock() - cartItem.getQuantity();
        return remainStock >= 0;
    }

    public List<Order> getAllOrders() {
        return orderDAO.getAll();
    }

    public Order updateStatus(Order order) {
        return orderDAO.updateOrderStatus(order);
    }

    public List<Order> getOrderByStatus(String s){
       return orderDAO.filterOrdersByStatus(s);
    }

    public boolean validateStatus(String s) {
        if (!OrderStatus.contains(s)) {
            System.out.println("Status no valido");
            return false;
        }
        return true;
    }
}
