package com.revature.util;

import com.revature.controllers.ProductController;
import com.revature.controllers.UserController;
import com.revature.models.Product;
import com.revature.models.User;
import com.revature.repos.ProductDAO;
import com.revature.repos.UserDAO;
import com.revature.repos.UserDAOPostgres;
import com.revature.services.ProductService;
import com.revature.services.UserService;
import io.javalin.Javalin;

import static io.javalin.apibuilder.ApiBuilder.*;

public class JavalinUtil {

    public static Javalin create(int port) {
        UserDAO userDAO = new UserDAOPostgres();
        UserService userService = new UserService(userDAO);
        UserController userController = new UserController(userService);

        ProductDAO productDAO = new ProductDAO();
        ProductService productService = new ProductService(productDAO);
        ProductController productController = new ProductController(productService,userService);

        return Javalin.create(config -> {
                    config.router.apiBuilder(() -> {
                        path("/users", () -> {
                            post("/register", userController::registerNewUser);
                            post("/login", userController::loginUser);
                            put("/update",userController::updateUser);
                        });
                        path("/products",()->{
                            post("/register", productController::registerNewProuct);
                        });
                    });
                })
                .start(port);
    }
}
