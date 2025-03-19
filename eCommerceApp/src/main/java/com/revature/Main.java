package com.revature;

import com.revature.repos.UserDAO;
import com.revature.repos.UserDAOPostgres;
import com.revature.services.UserService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        UserDAO userDAO = new UserDAOPostgres();
        UserService userService = new UserService(userDAO);
        Scanner scan = new Scanner(System.in);

        UserController userController = new UserController(userService,scan);

        User newUser = userController.registerNewUser();
        System.out.println(newUser.toString());

        User loggedUser = userController.loginUser();

        System.out.println(loggedUser.toString());
    }
}