package com.revature.repos;

import com.revature.models.User;

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
        System.out.println("Soy un usuario nuevo");
        testUsers.add(user);
        usersCount=testUsers.size();
        return user;
    }

    @Override
    public User getByID(int ID){
        System.out.println("Soy un usuario encontrado por ID");
        User foundUser=null;
        for(User u: testUsers){
            if(u.getUserID()==ID){
                foundUser = u;
                break;
            }
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