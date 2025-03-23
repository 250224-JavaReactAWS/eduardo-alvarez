package com.revature;

import com.revature.util.ConnectionUtil;
import com.revature.repos.*;
import com.revature.models.*;
import com.revature.controllers.*;
import com.revature.services.*;
import com.revature.util.JavalinUtil;

import java.sql.Connection;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

//        Connection conn = ConnectionUtil.getConnection();

       JavalinUtil.create(7070);

        /*UserDAO userDAO = new UserDAOPostgres();
        UserService userService = new UserService(userDAO);
        Scanner scan = new Scanner(System.in);

        UserController userController = new UserController(userService,scan);

        User loggedUser = userController.loginUser();
        System.out.println("Antes del update");
        System.out.println(loggedUser.toString());
        loggedUser = userController.UpdateUser(loggedUser);
        System.out.println("despues del update");
        System.out.println(loggedUser.toString());*/
    }
}