package com.revature;

import com.revature.controllers.UserController;
import com.revature.models.User;
import com.revature.repos.UserDAO;
import com.revature.repos.UserDAOPostgres;
import com.revature.services.UserService;
import com.revature.util.ConnectionUtil;

import java.sql.Connection;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Connection conn = ConnectionUtil.getInstance();

        /* UserDAO userDAO = new UserDAOPostgres();
        UserService userService = new UserService(userDAO);
        Scanner scan = new Scanner(System.in);

        UserController userController = new UserController(userService,scan);

        User newUser = userController.registerNewUser();
        System.out.println(newUser.toString());

        User loggedUser = userController.loginUser();*/
    }
}