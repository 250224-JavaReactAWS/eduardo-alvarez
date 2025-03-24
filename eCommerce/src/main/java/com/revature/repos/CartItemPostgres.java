package com.revature.repos;

import com.revature.models.CartItem;
import com.revature.models.Product;
import com.revature.models.User;
import com.revature.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CartItemPostgres implements CartItemDAO {

    public CartItemPostgres() {
    }

    @Override
    public CartItem updateQuantity(CartItem cartItem) {
        if(cartItem==null){
            return null;
        }

        String query = "update cartitems set quantity=? where product_id=? and user_id=? RETURNING *";
        CartItem updatedCartItem = null;
        try (Connection conn = ConnectionUtil.getConnection()){
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1,cartItem.getQuantity());
            preparedStatement.setInt(2,cartItem.getProductID());
            preparedStatement.setInt(3,cartItem.getUserID());

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                updatedCartItem = new CartItem(resultSet);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return updatedCartItem;
    }

    @Override
    public List<CartItem> getAllCartItems(int userID) {
        List<CartItem> allCartItems = new ArrayList<>();
        Connection conn = ConnectionUtil.getConnection();

        String query = "SELECT * FROM cartitems where user_id=?";

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                CartItem retrieveCartItem = new CartItem(resultSet);
                allCartItems.add(retrieveCartItem);
            }
        } catch (SQLException e) {
            System.out.println("Could not get all CartItems!");
            e.printStackTrace();
        }
        return allCartItems;
    }

    @Override
    public boolean removeItem(int productID, int userID) {
        boolean result = false;
        String query = "delete from cartitems where product_id=? and user_id=? RETURNING *";
        try (Connection conn = ConnectionUtil.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, productID);
            preparedStatement.setInt(2, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                result = true;
            } else {
                result = false;
                System.out.println("Algo salio mal borrando el item");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    @Override
    public CartItem create(CartItem obj) {
        CartItem newCartItem = null;
        try (Connection conn = ConnectionUtil.getConnection()) {
            String query = "insert into cartitems (user_id ,product_id,quantity) values (?, ?, ?) RETURNING *";

            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, obj.getUserID());
            preparedStatement.setInt(2, obj.getProductID());
            preparedStatement.setInt(3, obj.getQuantity());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                newCartItem = new CartItem(resultSet);
            }
        } catch (SQLException e) {
            System.out.println("No se pudo crear el cartItem");
            e.printStackTrace();
        }
        return newCartItem;
    }

    @Override
    public List<CartItem> getAll() {
        return List.of();
    }

    @Override
    public CartItem getByID(int id) {
        return null;
    }

    @Override
    public CartItem update(CartItem obj) {
        return null;
    }

    @Override
    public boolean deleteById(int id) {
        return false;
    }
}
