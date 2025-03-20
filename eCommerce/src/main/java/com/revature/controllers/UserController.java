package com.revature.controllers;

import com.revature.dtos.response.ErrorMessage;
import com.revature.models.User;
import com.revature.services.UserService;

import java.util.Scanner;

import io.javalin.http.Context;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class UserController {
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private Scanner scan;

    public UserController(UserService UserService) {
        this.userService = UserService;
    }

    public void registerNewUser(Context ctx) {
        User requestUser = ctx.bodyAsClass(User.class);

        if (!userService.validateEmail(requestUser.getEmail())) {
            ctx.status(400);
            ctx.json(new ErrorMessage("Email does not have a valid address"));
            return;
        }
        if (!userService.isEmailAvailable(requestUser.getEmail())) {
            ctx.status(400);
            ctx.json(new ErrorMessage("Email already registered"));
            logger.warn("Register attempt made for taken email: "+requestUser.getEmail());
            return;
        }

        if (!userService.validatePassword(requestUser.getPassword())) {
            ctx.status(400);
            ctx.json(new ErrorMessage("Invalid password. It should contains at least 8 characters and an upper and lower case"));
            return;
        }

        User registeredUser = userService.registerNewUser(requestUser.getFirstName(),requestUser.getLastName(), requestUser.getEmail(),requestUser.getPassword());
        if(registeredUser == null){
            ctx.status(500);
            ctx.json(new ErrorMessage("Something went wrong trying to register new user"));
            return;
        }

        logger.info("New user registered with email: "+registeredUser.getEmail());
        ctx.status(201);
        ctx.json(registeredUser);
    }

    public User loginUser() {
        System.out.println("Login");
        System.out.println("email: ");
        String email = scan.nextLine();
        System.out.println("Password:");
        String password = scan.nextLine();

        User loggedUser = userService.loginUser(email, password);
        if (loggedUser == null) {
            System.out.println("email o password incorrectos");
            return null;
        }
        System.out.println("Bienvenido " + loggedUser.getFirstName() + " " + loggedUser.getLastName());
        return loggedUser;
    }

    public User UpdateUser(User user) {
        System.out.println("New firstname:");
        String newFirstName = scan.nextLine();
        System.out.println("New lastname:");
        String newLastName = scan.nextLine();
        System.out.println("New email");
        String newEmail = scan.nextLine();
        while (!userService.validateEmail(newEmail) || !userService.isEmailAvailable(newEmail)) {
            if (!userService.validateEmail(newEmail)) {
                System.out.println("email invalid");
                newEmail = scan.nextLine();
            } else {
                System.out.println("Email ya registrado");
                newEmail = scan.nextLine();
            }
        }
        System.out.println("New password");
        String newPasssword = scan.nextLine();
        while (!userService.validatePassword(newPasssword)) {
            System.out.println("No password. Otra vez");
            newPasssword = scan.nextLine();
        }
        return userService.updateInfo(user.getUserID(), newFirstName, newLastName, newEmail, newPasssword);
    }
}
