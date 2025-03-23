package com.revature.repos;

import com.revature.models.*;
import com.revature.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderPostgres implements OrderDAO {
    public OrderPostgres(){
    }

    @Override
    public Order create(Order obj) {
        Order newOrder = null;
        try (Connection conn = ConnectionUtil.getConnection()) {
            String query = "insert into orders (user_id, total_price) values (?, ?) RETURNING *";

            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, obj.getUserID());
            preparedStatement.setFloat(2, obj.getTotalPrice());

            System.out.println(obj.getUserID());
            System.out.println(obj.getTotalPrice());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                newOrder = new Order(resultSet);
                System.out.println(newOrder.getUserID());
                System.out.println(newOrder.getTotalPrice());
                System.out.println(newOrder.getStatus());
            }
        } catch (SQLException e) {
            System.out.println("No se pudo crear el cartItem");
            e.printStackTrace();
        }
        return newOrder;
    }

    @Override
    public List<Order> getAll() {
        List<Order> allOrders = new ArrayList<>();
        Connection conn = ConnectionUtil.getConnection();

        String query = "SELECT * FROM orders order by status";

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Order retrievedOrder = new Order(resultSet);
                allOrders.add(retrievedOrder);
            }
        } catch (SQLException e) {
            System.out.println("Could not get all orders");
            e.printStackTrace();
        }
        return allOrders;
    }

    @Override
    public Order getByID(int id) {
        return null;
    }

    @Override
    public Order update(Order obj) {
        return null;
    }

    @Override
    public boolean deleteById(int id) {
        return false;
    }

    @Override
    public Order updateOrderStatus(Order order) {
        if(order==null){
            return null;
        }
        String query = "UPDATE orders SET status=? where order_id=? RETURNING *";
        Order updatedOrder = null;
        try(Connection conn = ConnectionUtil.getConnection()){
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1,order.getStatus().name());
            preparedStatement.setInt(2,order.getOrderID());

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                updatedOrder = new Order(resultSet);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return updatedOrder;
    }

    @Override
    public List<Order> filterOrdersByStatus(String status) {
        List<Order> allOrders = new ArrayList<>();
        Connection conn = ConnectionUtil.getConnection();

        String query = "SELECT * FROM orders where status=?";

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1,status);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Order retrievedOrder = new Order(resultSet);
                allOrders.add(retrievedOrder);
            }
        } catch (SQLException e) {
            System.out.println("Could not get all orders");
            e.printStackTrace();
        }
        return allOrders;
    }
}
