package com.revature;

import com.revature.util.ConnectionUtil;
import com.revature.repos.*;
import com.revature.models.*;
import com.revature.controllers.*;
import com.revature.services.*;
import java.sql.Connection;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

//        Connection conn = ConnectionUtil.getConnection();

        UserDAO userDAO = new UserDAOPostgres();
        UserService userService = new UserService(userDAO);
        Scanner scan = new Scanner(System.in);

        UserController userController = new UserController(userService,scan);

        System.out.println(userService.validateEmail("hsdbfuhybcysr"));
        System.out.println(userService.validateEmail("eduardo@revature.net"));
        System.out.println(userService.validateEmail("eduardo@aragon.unam.mx"));
        System.out.println(userService.validateEmail("eduardo@gmail.com"));

        User createdUser = userController.registerNewUser();
        System.out.println(createdUser.toString());
    }
}