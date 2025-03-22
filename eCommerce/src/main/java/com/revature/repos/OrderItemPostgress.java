package com.revature.repos;

import com.revature.models.CartItem;
import com.revature.models.OrderItem;
import com.revature.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OrderItemPostgress implements OrderItemDAO{
    public OrderItemPostgress(){
    }

    @Override
    public OrderItem create(OrderItem obj) {
       /* OrderItem newOrderItem = null;
        try (Connection conn = ConnectionUtil.getConnection()) {
            String query = "insert into orderitems (user_id ,product_id,quantity,price) values (?, ?, ?,?) RETURNING *";

            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, obj.getUserID());
            preparedStatement.setInt(2, obj.getProductID());
            preparedStatement.setInt(3, obj.getQuantity());

            System.out.println(obj.getUserID());
            System.out.println(obj.getProductID());
            System.out.println(obj.getQuantity());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                newCartItem = new CartItem(resultSet);
                System.out.println(newCartItem.getUserID());
                System.out.println(newCartItem.getProductID());
                System.out.println(newCartItem.getQuantity());
            }
        } catch (SQLException e) {
            System.out.println("No se pudo crear el cartItem");
            e.printStackTrace();
        }
        return newCartItem;*/
        return null;
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
}
