package com.revature.repos;

import com.revature.models.User;
import com.revature.util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOPostgres implements UserDAO {

    public int usersCount;

    public UserDAOPostgres() {
    }

    @Override
    public User create(User user) {
        User newUser = null;
        try (Connection conn = ConnectionUtil.getConnection()) {
            String query = "INSERT INTO users (first_name, last_name, email, password) VALUES (?,?,?,?) RETURNING *";

            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getPassword());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                newUser = new User(resultSet);
            }
        } catch (SQLException e) {
            System.out.println("No se pudo crear al usuario");
            e.printStackTrace();
        }
        return newUser;
    }

    @Override
    public User getByID(int ID) {
        User foundUser = null;
        String query = "SELECT * FROM users WHERE user_id = ?";

        try (Connection conn = ConnectionUtil.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, ID);

            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                foundUser = new User(result);
            } else {
                System.out.println("No se encontro usuario");
            }
        } catch (SQLException e) {
            System.out.println("No se puedo obtener el usuario");
            e.printStackTrace();
        }

        return foundUser;
    }

    @Override
    public User getUserByEmail(String email) {
        User foundUser = null;
        String query = "SELECT * FROM users WHERE email = ?";

        try (Connection conn = ConnectionUtil.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, email);

            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                foundUser = new User(result);
            } else {
                System.out.println("No se encontro usuario");
            }
        } catch (SQLException e) {
            System.out.println("No se puedo obtener el usuario");
            e.printStackTrace();
        }

        return foundUser;
    }

    @Override
    public User update(User updatedUser) {
        if (updatedUser == null) {
            return null;
        } else {
            String query = "UPDATE users set first_name = ?, last_name = ?, email = ?, password = ? WHERE user_id = ? RETURNING *";


            try (Connection conn = ConnectionUtil.getConnection()) {
                PreparedStatement preparedStatement = conn.prepareStatement(query);
                preparedStatement.setString(1, updatedUser.getFirstName());
                preparedStatement.setString(2, updatedUser.getLastName());
                preparedStatement.setString(3, updatedUser.getEmail());
                preparedStatement.setString(4, updatedUser.getPassword());
                preparedStatement.setInt(5, updatedUser.getUserID());

                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    updatedUser = new User(resultSet);
                    System.out.println("Se actualizo el usuario");
                } else {
                    System.out.println("Algo salio mal en la actualizada");
                    updatedUser = null;
                }

            } catch (SQLException e) {
                System.out.println("No se pudo encontrar al usuario para actualizar");
                e.printStackTrace();
            }


        }
        return updatedUser;
    }
}