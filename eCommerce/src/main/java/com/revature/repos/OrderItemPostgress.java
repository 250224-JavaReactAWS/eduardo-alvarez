package com.revature.repos;

import com.revature.models.OrderItem;
import com.revature.models.PastOrder;
import com.revature.services.OrderItemService;
import com.revature.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderItemPostgress implements OrderItemDAO {
    public OrderItemPostgress() {
    }

    @Override
    public OrderItem create(OrderItem obj) {
        OrderItem newOrderItem = null;
        try (Connection conn = ConnectionUtil.getConnection()) {
            String query = "insert into orderitems (order_id ,product_id,quantity,price) values (?, ?, ?,?) RETURNING *";

            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, obj.getOrderID());
            preparedStatement.setInt(2, obj.getProductID());
            preparedStatement.setInt(3, obj.getQuantity());
            preparedStatement.setFloat(4, obj.getPrice());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                newOrderItem = new OrderItem(resultSet);
            }
        } catch (SQLException e) {
            System.out.println("No se pudo crear el cartItem");
            e.printStackTrace();
        }
        return newOrderItem;
    }

    @Override
    public List<OrderItem> getAll() {
        return List.of();
    }

    @Override
    public OrderItem getByID(int id) {
        return null;
    }

    @Override
    public OrderItem update(OrderItem obj) {
        return null;
    }

    @Override
    public boolean deleteById(int id) {
        return false;
    }

    @Override
    public List<OrderItem> getUserPastOrders(int userID) {
        List<OrderItem> pastOrders = new ArrayList<>();
        String query = "select order_item_id, o.order_id, o2.product_id, o2.quantity, o2.price from users u " +
                "join orders o on o.user_id = u.user_id " +
                "join orderitems o2 on o.order_id = o2.order_id  where u.user_id =?";
        try (Connection conn = ConnectionUtil.getConnection()){
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1,userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                OrderItem pastOrder = new OrderItem(resultSet);
                pastOrders.add(pastOrder);
            }
        }catch (SQLException e){
            System.out.println("Algo saldio mal");
            e.printStackTrace();
        }
        return pastOrders;
    }
}
