package com.revature.repos;

import com.revature.models.Product;
import com.revature.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOPostgres implements ProductDAO {
    public ProductDAOPostgres() {
    }


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
    public List<Product> getAll() {
        List<Product> allProducts = new ArrayList<>();
        Connection conn = ConnectionUtil.getConnection();

        String query = "SELECT * FROM products";

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Product retrieveProduct = new Product(resultSet);
                allProducts.add(retrieveProduct);
            }
        } catch (SQLException e) {
            System.out.println("Could not get all users!");
            e.printStackTrace();
        }
        return allProducts;
    }


    @Override
    public Product getByID(int productID) {
        Product foundUser = null;
        String query = "SELECT * FROM products WHERE product_id = ?";

        try (Connection conn = ConnectionUtil.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, productID);

            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                foundUser = new Product(result);
            } else {
                System.out.println("No se encontro product");
            }
        } catch (SQLException e) {
            System.out.println("No se puedo obtener el product");
            e.printStackTrace();
        }
        return foundUser;
    }

    @Override
    public Product update(Product updatedProduct) {
        if (updatedProduct == null) {
            return null;
        } else {
            String query = "update products set name = ?, description=?, price=?, stock = ? where products.product_id =? RETURNING *";


            try (Connection conn = ConnectionUtil.getConnection()) {
                PreparedStatement preparedStatement = conn.prepareStatement(query);
                preparedStatement.setString(1, updatedProduct.getName());
                preparedStatement.setString(2, updatedProduct.getDescription());
                preparedStatement.setFloat(3, updatedProduct.getPrice());
                preparedStatement.setInt(4, updatedProduct.getStock());
                preparedStatement.setInt(5, updatedProduct.getProductID());

                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    updatedProduct = new Product(resultSet);
                    System.out.println("Se actualizo el producto");
                } else {
                    System.out.println("Algo salio mal en la actualizada del producto");
                    updatedProduct = null;
                }

            } catch (SQLException e) {
                System.out.println("No se pudo encontrar al producto para actualizar");
                e.printStackTrace();
            }
        }
        return updatedProduct;
    }

    @Override
    public boolean deleteById(int id) {
        Product product = getByID(id);
        boolean result = false;
        if (product == null) {
            result = false;
        } else {
            String query = "delete from products where product_id=? RETURNING *";
            try (Connection conn = ConnectionUtil.getConnection()) {
                PreparedStatement preparedStatement = conn.prepareStatement(query);
                preparedStatement.setInt(1, product.getProductID());
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    result = true;
                } else {
                    result = false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                result = false;
            }
        }
        return result;
    }
}
