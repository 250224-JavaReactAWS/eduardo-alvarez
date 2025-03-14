package com.revature.repos;

import com.revature.models.User;

public class UserDAOPostgres implements UserDAO{

    @Override
    public  User createUser(User user){
        System.out.println("Soy un usuario nuevo");
        return null;
    }

    @Override
    public User getUserByID(int ID){
        System.out.println("Soy un usuario encontrado por ID");
        return  null;
    }

    @Override
    public User updateUser(User user){
        System.out.println("Se actualizo el usuario");
        return  null;
    }
}
