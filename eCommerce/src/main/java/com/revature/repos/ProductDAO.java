package com.revature.repos;

import com.revature.models.Product;
import com.revature.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductDAO implements ShowDAO{
    public ProductDAO(){}


    @Override
    public Product create(Product product) {
        Product newProduct = null;
        try (Connection conn = ConnectionUtil.getConnection()) {
            String query = "insert into products (name ,description,price,stock) values (?,?, ?, ?) RETURNING *";

            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getDescription());
            preparedStatement.setFloat(3, product.getPrice());
            preparedStatement.setInt(4, product.getStock());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                newProduct = new Product(resultSet);
            }
        } catch (SQLException e) {
            System.out.println("No se pudo crear el producto");
            e.printStackTrace();
        }
        return newProduct;
    }


    @Override
    public Product getByID(int userID) {
        return null;
    }

    @Override
    public Product update(Product obj) {
        return null;
    }
}
