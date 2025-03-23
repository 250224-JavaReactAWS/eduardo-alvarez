package com.revature.util;

import com.revature.controllers.*;
import com.revature.repos.*;
import com.revature.services.*;
import io.javalin.Javalin;

import static io.javalin.apibuilder.ApiBuilder.*;

public class JavalinUtil {

    public static Javalin create(int port) {
        UserDAO userDAO = new UserDAOPostgres();
        UserService userService = new UserService(userDAO);
        UserController userController = new UserController(userService);

        ProductDAOPostgres productDAO = new ProductDAOPostgres();
        ProductService productService = new ProductService(productDAO);
        ProductController productController = new ProductController(productService, userService);

        CartItemDAO cartItemDAO = new CartItemPostgres();
        CartItemService cartItemService = new CartItemService(cartItemDAO);
        CartItemController cartItemController = new CartItemController(userService, productService, cartItemService);

        OrderItemDAO orderItemDAO = new OrderItemPostgress();
        OrderItemService orderItemService = new OrderItemService(orderItemDAO, userService);
        OrderItemController orderItemController = new OrderItemController(orderItemService);

        OrderDAO orderPostgres = new OrderPostgres();
        OrderService orderService = new OrderService(orderPostgres);
        OrderController orderController = new OrderController(orderService, productService, cartItemService, orderItemService, userService);

        return Javalin.create(config -> {
                    config.router.apiBuilder(() -> {
                        path("/users", () -> {
                            post("/register", userController::registerNewUser);
                            post("/login", userController::loginUser);
                            put("/update", userController::updateUser);
                        });
                        path("/products", () -> {
                            post("/register", productController::registerNewProuct);
                            get("/", productController::getAllProducts);
                            get("/{ID}", productController::getProductByID);
                            put("/update", productController::updateProduct);
                            delete("/delete/{ID}", productController::deleteProduct);
                        });
                        path("/cartItem", () -> {
                            post("/register", cartItemController::registerCartItem);
                            delete("/remove", cartItemController::removedItemFromCart);
                            put("/updateQuantity", cartItemController::updateCartItemQuantity);
                        });
                        path("/order", () -> {
                            get("/showAll", orderController::getAllOrders);
                            get("/showAll/{status}", orderController::getOrdersByStatus);
                            get("/pastOrders", orderItemController::getPastOrders);
                            post("/", orderController::registerOrder);
                            put("/update/{status}", orderController::updateStatus);
                        });
                    });
                })
                .start(port);
    }
}
