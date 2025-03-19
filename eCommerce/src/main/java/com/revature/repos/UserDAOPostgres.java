package com.revature.repos;

import com.revature.models.User;
import com.revature.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDAOPostgres implements UserDAO{

    private List<User> testUsers;
    public int usersCount;

    public UserDAOPostgres(){
        User customer1 = new User(1,"Juan","Perez","enail","password");
        User customer2 = new User(2,"Pepe","Gonzales","enail2","password");
        User admin = new User(3,"Pancho","Hernandez","enail3","password123");
        testUsers=new ArrayList<User>();
        testUsers.add(customer1);
        testUsers.add(customer2);
        testUsers.add(admin);
        usersCount = testUsers.size();
    }

    @Override
    public  User create(User user){
        //Connection conn = ConnectionUtil.getConnection();
        return user;
    }

    @Override
    public User getByID(int ID){
        User foundUser = null;
        Connection conn = ConnectionUtil.getConnection();
        String query = "SELECT * FROM users WHERE user_id = "+ID;

        try{
            Statement statement = conn.createStatement();

            ResultSet result = statement.executeQuery(query);
            result.next();

            foundUser = new User(result);
        }catch (SQLException e){
            System.out.println("No se puedo obtener el usuario");
            e.printStackTrace();
        }

        return foundUser;
    }

    @Override
    public User getUserByEmail(String email){
        System.out.println("Soy un usuario encontrado por ID");
        User foundUser=null;
        for(User u: testUsers){
            if(u.getEmail().equals(email)){
                foundUser = u;
                break;
            }
        }
        return foundUser;
    }

    @Override
    public User update(User user){
        System.out.println("Se actualizo el usuario");
        for(User u: testUsers){
            if(u.getUserID()==user.getUserID()){
                u.setFirstName(user.getFirstName());
                u.setEmail(user.getEmail());
                u.setRole(user.getRole());
                u.setLastName(user.getLastName());
                u.setPassword(user.getPassword());
                break;
            }
        }
        return  null;
    }
}