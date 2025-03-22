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

public class CartItemPostgres implements CartItemDAO{
    int currentIDProduct;
    public CartItemPostgres(){
    }

    @Override
    public boolean removeItem(User user, Product product) {
        return false;
    }

    @Override
    public CartItem updateQuantity(int quantity) {
        return null;
    }

    @Override
    public void loadProduct(Product product) {

    }

    @Override
    public List<CartItem> getAllCartItems(int userID) {
        List<CartItem> allCartItems = new ArrayList<>();
        Connection conn = ConnectionUtil.getConnection();

        String query = "SELECT * FROM cartitems where user_id=?";

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1,userID);
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
    public CartItem create(CartItem obj) {
        CartItem newCartItem = null;
        try (Connection conn = ConnectionUtil.getConnection()) {
            String query = "insert into cartitems (user_id ,product_id,quantity) values (?, ?, ?) RETURNING *";

            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, obj.getUserID());
            preparedStatement.setInt(2, obj.getProductID());
            preparedStatement.setInt(3, obj.getQuantity());

            System.out.println(obj.getUserID());
            System.out.println( obj.getProductID());
            System.out.println( obj.getQuantity());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                newCartItem = new CartItem(resultSet);
                System.out.println(newCartItem.getUserID());
                System.out.println( newCartItem.getProductID());
                System.out.println( newCartItem.getQuantity());
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

    public void setCurrentIDProduct(int currentIDProduct) {
        this.currentIDProduct = currentIDProduct;
    }
}
