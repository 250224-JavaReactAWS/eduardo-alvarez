package com.revature.util;

import com.revature.controllers.UserController;
import com.revature.models.User;
import com.revature.repos.UserDAO;
import com.revature.repos.UserDAOPostgres;
import com.revature.services.UserService;
import io.javalin.Javalin;

import static io.javalin.apibuilder.ApiBuilder.*;

public class JavalinUtil {

    public static Javalin create(int port) {
        UserDAO userDAO = new UserDAOPostgres();
        UserService userService = new UserService(userDAO);
        UserController userController = new UserController(userService);

        return Javalin.create(config -> {
                    config.router.apiBuilder(() -> {
                        path("/users", () -> {
                            post("/register", userController::registerNewUser);
                            post("/login", userController::loginUser);
                        });
                    });
                })
                .start(port);
    }
}
