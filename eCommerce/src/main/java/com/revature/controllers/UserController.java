package com.revature.controllers;

import com.revature.dtos.response.ErrorMessage;
import com.revature.models.User;
import com.revature.services.UserService;

import io.javalin.http.Context;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class UserController {
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService UserService) {
        this.userService = UserService;
    }

    public void registerNewUser(Context ctx) {
        User requestUser = ctx.bodyAsClass(User.class);

        if (!userService.validateEmail(requestUser.getEmail())) {
            ctx.status(400);
            ctx.json(new ErrorMessage("Email does not have a valid address"));
            logger.warn("Register user attempt with invalid email: " + requestUser.getEmail());
            return;
        }
        if (!userService.isEmailAvailable(requestUser.getEmail())) {
            ctx.status(400);
            ctx.json(new ErrorMessage("Email already registered"));
            logger.warn("Register user attempt made for taken email: " + requestUser.getEmail());
            return;
        }

        if (!userService.validatePassword(requestUser.getPassword())) {
            ctx.status(400);
            ctx.json(new ErrorMessage("Invalid password. It should contains at least 8 characters and an upper and lower case"));
            logger.warn("Register user attempt with invalid password: " + requestUser.getPassword());
            return;
        }

        User registeredUser = userService.registerNewUser(requestUser.getFirstName(), requestUser.getLastName(), requestUser.getEmail(), requestUser.getPassword());
        if (registeredUser == null) {
            ctx.status(500);
            ctx.json(new ErrorMessage("Something went wrong trying to register new user"));
            logger.error("Something went wrong trying to register the user with the info: " + requestUser.toString());
            return;
        }

        logger.info("New user registered with email: " + registeredUser.getEmail());
        ctx.status(201);
        ctx.json(registeredUser);
    }

    public void loginUser(Context ctx) {
        User requestUser = ctx.bodyAsClass(User.class);

        User loggedUser = userService.loginUser(requestUser.getEmail(), requestUser.getPassword());
        if (loggedUser == null) {
            ctx.json(new ErrorMessage("Email or password incorrect"));
            ctx.status(400);
            return;
        }

        ctx.status(200);
        ctx.json(loggedUser);

        ctx.sessionAttribute("userID", loggedUser.getUserID());
        ctx.sessionAttribute("role", loggedUser.getRole());

        logger.info("User logged with ID: " + loggedUser.getUserID());
    }

    public void updateUser(Context ctx) {
        User userWithNewInfo = ctx.bodyAsClass(User.class);
        if (ctx.sessionAttribute("userID") == null) {
            ctx.status(401);
            ctx.json(new ErrorMessage("You must be logged to do this actions"));
            logger.warn("Attempt to update user info without login");
            return;
        } else {
            userWithNewInfo.setUserID(ctx.sessionAttribute("userID"));
        }

        if (!userService.validateEmail(userWithNewInfo.getEmail())) {
            ctx.status(400);
            ctx.json(new ErrorMessage("Email address not valid"));
            logger.warn("User with ID: " + userWithNewInfo.getUserID() + " tried to update its email for invalid email: " + userWithNewInfo.getEmail());
        }

        if (!userService.isEmailAvailable(userWithNewInfo.getEmail())) {
            ctx.status(400);
            ctx.json(new ErrorMessage("Email already registered"));
            logger.warn("User with ID: " + userWithNewInfo.getUserID() + " tried to update its email for another already registered: " + userWithNewInfo.getEmail());
        }

        if (userWithNewInfo.getPassword() != null) {
            if (!userService.validatePassword(userWithNewInfo.getPassword())) {
                ctx.status(400);
                ctx.json(new ErrorMessage("Invalid password. It should contains at least 8 characters and an upper and lower case"));
                logger.warn("User update attempt with ID:" + userWithNewInfo.getUserID() + " invalid password: " + userWithNewInfo.getPassword());
            }
        }

        User updatedUser = userService.updateInfo(userWithNewInfo);
        System.out.println("Original user: " + userWithNewInfo.toString());
        System.out.println("updated user: " + updatedUser.toString());
        if (updatedUser == null) {
            ctx.json(new ErrorMessage("User update failed"));
            ctx.status(400);
            return;
        }

        ctx.status(200);
        ctx.json(updatedUser);
    }
}
