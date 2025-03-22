package com.revature.util;

import com.revature.controllers.CartItemController;
import com.revature.controllers.OrderController;
import com.revature.controllers.ProductController;
import com.revature.controllers.UserController;
import com.revature.repos.*;
import com.revature.services.CartItemService;
import com.revature.services.OrderService;
import com.revature.services.ProductService;
import com.revature.services.UserService;
import io.javalin.Javalin;

import static io.javalin.apibuilder.ApiBuilder.*;

public class JavalinUtil {

    public static Javalin create(int port) {
        UserDAO userDAO = new UserDAOPostgres();
        UserService userService = new UserService(userDAO);
        UserController userController = new UserController(userService);

        ProductDAOPostgres productDAO = new ProductDAOPostgres();
        ProductService productService = new ProductService(productDAO);
        ProductController productController = new ProductController(productService,userService);

        CartItemDAO cartItemDAO = new CartItemPostgres();
        CartItemService cartItemService = new CartItemService(cartItemDAO);
        CartItemController cartItemController = new CartItemController(userService,productService,cartItemService);

        OrderDAO orderPostgres = new OrderPostgres();
        OrderService orderService = new OrderService(orderPostgres);
        OrderController orderController = new OrderController(orderService,productService,cartItemService,userService);

        return Javalin.create(config -> {
                    config.router.apiBuilder(() -> {
                        path("/users", () -> {
                            post("/register", userController::registerNewUser);
                            post("/login", userController::loginUser);
                            put("/update",userController::updateUser);
                        });
                        path("/products",()->{
                            post("/register", productController::registerNewProuct);
                            get("/",productController::getAllProducts);
                            get("/{ID}",productController::getProductByID);
                            put("/update", productController::updateProduct);
                            delete("/delete/{ID}", productController::deleteProduct);
                        });
                        path("/cartItem",()->{
                            post("/register",cartItemController::registerCartItem);
                            delete("/remove",cartItemController::removedItemFromCart);
                            put("/updateQuantity",cartItemController::updateCartItemQuantity);
                        });
                        path("/order",()->{
                            get("/showAll",orderController::getAllOrders);
                            post("/",orderController::registerOrder);
                        });
                    });
                })
                .start(port);
    }
}
