package com.revature.controllers;

import com.revature.dtos.response.ErrorMessage;
import com.revature.models.*;
import com.revature.repos.OrderDAO;
import com.revature.services.CartItemService;
import com.revature.services.OrderService;
import com.revature.services.ProductService;
import com.revature.services.UserService;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class OrderController {
    private final OrderService orderService;
    private final ProductService productService;
    private final CartItemService cartItemService;
    private final Logger logger = LoggerFactory.getLogger(CartItem.class);
    private final UserService userService;

    public OrderController(OrderService orderService, ProductService productService, CartItemService cartItemService, UserService userService) {
        this.orderService = orderService;
        this.productService = productService;
        this.cartItemService = cartItemService;
        this.userService = userService;
    }

    public void registerOrder(Context ctx) {
        Order requestedOrder = new Order();
        if (ctx.sessionAttribute("userID") == null) {
            ctx.status(400);
            ctx.json(new ErrorMessage("You must be logged to do that action"));
            logger.warn("Attempt of register cart item without logging");
            return;
        } else {
            requestedOrder.setUserID(ctx.sessionAttribute("userID"));
        }

        List<CartItem> itemsInCart = cartItemService.getAllCartItems(ctx.sessionAttribute("userID"));
        if (itemsInCart == null || itemsInCart.size()==0) {
            ctx.status(400);
            ctx.json(new ErrorMessage("There is no items in your cart"));
            logger.warn("Attempt of register an order without item in cart by user with ID: " + ctx.sessionAttribute("userID"));
            return;
        }
        List<Product> productsInCart = new ArrayList<>();
        for (CartItem cartItem : itemsInCart) {
            productsInCart.add(productService.getProductByID(cartItem.getProductID()));
        }
        float totalPrice = 0;
        for (int i = 0; i < productsInCart.size(); i++) {
            if (!orderService.validateStock(productsInCart.get(i), itemsInCart.get(i))) {
                ctx.status(400);
                ctx.json(new ErrorMessage("There is not enough stock for product: " + productsInCart.get(i).getName()));
                logger.warn("Attempt of order a product with not enough stock with ID: " + productsInCart.get(i).getProductID() +
                        "by user with ID:" + requestedOrder.getUserID());
                return;
            }
            totalPrice += ((float) itemsInCart.get(i).getQuantity()) * productsInCart.get(i).getPrice();
        }
        if (totalPrice < 0) {
            ctx.status(400);
            ctx.json(new ErrorMessage("Something went wrong calculating the total price"));
            logger.warn("Negative price calculated for user cart with ID: " + requestedOrder.getUserID());
            return;
        }
        requestedOrder.setTotalPrice(totalPrice);
        Order registeredOrder = orderService.registerOrder(requestedOrder.getUserID(), requestedOrder.getTotalPrice());
        if (registeredOrder == null) {
            ctx.status(500);
            ctx.json(new ErrorMessage("Something went wrong registering an order for user with ID: " + requestedOrder.getUserID()));
            logger.error("Something went wrong registering an order for user with ID: " + requestedOrder.getUserID());
            return;
        }

        ctx.status(200);
        ctx.json(registeredOrder);
        logger.info("Ordered registered with ID " + registeredOrder.getOrderID() + " for user with ID " + registeredOrder.getUserID());

        for (CartItem item : itemsInCart) {
            if (cartItemService.removeProduct(item.getProductID(), registeredOrder.getUserID())) {
                logger.info("Product with ID: " + item.getProductID() + " removed successfully after order from user cart with ID " + registeredOrder.getUserID());
            }else {
                logger.error("Product with ID: " + item.getProductID() + " could not be removed after order from user cart with ID " + registeredOrder.getUserID());
            }
        }
    }

    public void getAllOrders(Context ctx){
        if(ctx.sessionAttribute("userID")==null){
            ctx.status(400);
            ctx.json("You must be logged to do this action");
            logger.warn("Attempt of show all orders without being logged");
            return;
        }
        User loggedUser = userService.getUserByID(ctx.sessionAttribute("userID"));
        if(loggedUser.getRole()!= Role.ADMIN){
            ctx.status(401);
            ctx.json(new ErrorMessage("You do not have permission to do that action"));
            logger.warn("Attempt of show all orders without permission by user with ID "+loggedUser.getUserID());
            return;
        }
        List<Order> orders = orderService.getAllOrders();

        ctx.status(200);
        ctx.json(orders);
        logger.info("All orders showed to user with ID: "+loggedUser.getUserID());
    }
}