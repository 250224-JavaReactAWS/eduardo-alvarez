package com.revature.controllers;

import com.revature.services.UserService;

import java.util.Scanner;

public class UserController {
    private UserService userService;
    private Scanner scan;

    public UserController(UserService UserService, Scanner scan){
        this.userService = UserService;
        this.scan =scan;
    }

    public User registerNewUser(){
        System.out.println("First name: ");
        String firstName = scan.nextLine();
        System.out.println("Last name: ");
        String lastName = scan.nextLine();
        System.out.println("email: ");
        String email = scan.nextLine();
        while (!userService.validateEmail(email) && !userService.isEmailAvailable(email)){
            if(!userService.validateEmail(email)){
                System.out.println("Password invalid");
                email = scan.nextLine();
            }
            else{
                System.out.println("Email ya registrado");
                email = scan.nextLine();
            }
        }

        System.out.println("password: ");
        String password = scan.nextLine();

        while (!userService.validatePassword(password)){
            System.out.println("No password. Otra vez");
            password = scan.nextLine();
        }
        System.out.println("Se registro con exito");
        return userService.registerNewUser(firstName,lastName,email,password);
    }

    public User loginUser(){
        System.out.println("Login");
        System.out.println("email: ");
        String email = scan.nextLine();
        System.out.println("Password:");
        String password = scan.nextLine();

        User loggedUser = userService.loginUser(email,password);
        if(loggedUser ==  null){
            System.out.println("email o password incorrectos");
            return  null;
        }
        System.out.println("Bienvenido "+loggedUser.getFirstName()+" "+loggedUser.getLastName());
        return loggedUser;
    }
}