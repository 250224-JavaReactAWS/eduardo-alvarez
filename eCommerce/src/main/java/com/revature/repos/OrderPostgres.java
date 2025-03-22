package com.revature.repos;

import com.revature.models.CartItem;
import com.revature.models.Order;
import com.revature.models.OrderItem;
import com.revature.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        return List.of();
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
}
