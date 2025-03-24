package com.revature.controllers;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.revature.dtos.response.ErrorMessage;
import com.revature.models.*;
import com.revature.repos.OrderDAO;
import com.revature.services.*;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class OrderController {
    private final UserService userService;
    private final OrderService orderService;
    private final ProductService productService;
    private final CartItemService cartItemService;
    private final OrderItemService orderItemService;
    private final Logger logger = LoggerFactory.getLogger(CartItem.class);

    public OrderController(OrderService orderService, ProductService productService, CartItemService cartItemService, OrderItemService orderItemService, UserService userService) {
        this.orderService = orderService;
        this.productService = productService;
        this.cartItemService = cartItemService;
        this.orderItemService = orderItemService;
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
        if (itemsInCart == null || itemsInCart.size() == 0) {
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
        Order registeredOrder = orderService.registerOrder(requestedOrder);
        if (registeredOrder == null) {
            ctx.status(500);
            ctx.json(new ErrorMessage("Something went wrong registering an order for user with ID: " + requestedOrder.getUserID()));
            logger.error("Something went wrong registering an order for user with ID: " + requestedOrder.getUserID());
            return;
        }

        ctx.status(200);
        ctx.json(registeredOrder);
        logger.info("Ordered registered with ID " + registeredOrder.getOrderID() + " for user with ID " + registeredOrder.getUserID());

        for (int i = 0; i < productsInCart.size(); i++){
            OrderItem newOrderItem = new OrderItem(registeredOrder.getOrderID(),itemsInCart.get(i).getProductID(),itemsInCart.get(i).getQuantity(),productsInCart.get(i).getPrice());
            OrderItem registeredOrderItem = orderItemService.registerOrderItem(newOrderItem);
            if(registeredOrderItem==null){
                logger.error("Something went wrong trying to register order item for order: "+registeredOrder.getOrderID()+ " and the product: "+productsInCart.get(i).getProductID());
            }else {
                logger.info("Order item register for the order "+registeredOrderItem.getOrderID()+" and the product with ID: "+registeredOrderItem.getProductID());
            }
        }

        for (CartItem item : itemsInCart) {
            if (cartItemService.removeProduct(item.getProductID(), registeredOrder.getUserID())) {
                logger.info("Product with ID: " + item.getProductID() + " removed successfully after order from user cart with ID " + registeredOrder.getUserID());
            } else {
                logger.error("Product with ID: " + item.getProductID() + " could not be removed after order from user cart with ID " + registeredOrder.getUserID());
            }
        }
    }

    public void getAllOrders(Context ctx) {
        if (ctx.sessionAttribute("userID") == null) {
            ctx.status(400);
            ctx.json("You must be logged to do this action");
            logger.warn("Attempt of show all orders without being logged");
            return;
        }
        User loggedUser = userService.getUserByID(ctx.sessionAttribute("userID"));
        if (loggedUser.getRole() != Role.ADMIN) {
            ctx.status(401);
            ctx.json(new ErrorMessage("You do not have permission to do that action"));
            logger.warn("Attempt of show all orders without permission by user with ID " + loggedUser.getUserID());
            return;
        }
        List<Order> orders = orderService.getAllOrders();

        ctx.status(200);
        ctx.json(orders);
        logger.info("All orders showed to user with ID: " + loggedUser.getUserID());
    }

    public void updateStatus(Context ctx) {
        Order requestOrder = ctx.bodyAsClass(Order.class);
        if (ctx.sessionAttribute("userID") == null) {
            ctx.status(400);
            ctx.json(new ErrorMessage("You must be logged to do that"));
            logger.warn("Attempt of update order status without logging");
            return;
        }
        User loggedUser = userService.getUserByID(ctx.sessionAttribute("userID"));

        if (loggedUser.getRole() != Role.ADMIN) {
            ctx.status(401);
            ctx.json(new ErrorMessage("You do not have permission to do that"));
            logger.warn("Attempt of update order status without permission by user it ID: " + loggedUser.getUserID());
            return;
        }

        if (!orderService.validateStatus(ctx.pathParam("status"))) {
            ctx.status(400);
            ctx.json(new ErrorMessage("Status invalid"));
            logger.warn("Attempt of update order status with invalid status by user with ID: " + ctx.sessionAttribute("userID"));
            return;
        } else {
            requestOrder.setStatus(OrderStatus.valueOf(ctx.pathParam("status")));
        }

        Order updatedOrder = orderService.updateStatus(requestOrder);
        if (updatedOrder == null) {
            ctx.status(400);
            ctx.json(new ErrorMessage("Something went wrong updating the status"));
            logger.error("Something went wrong updating the status for order ID: " + requestOrder.getOrderID());
            return;
        }

        ctx.status(200);
        ctx.json(updatedOrder);
        logger.info("Status of order with ID " + updatedOrder.getOrderID() + " was changed from " + requestOrder.getStatus().name() + " to " + updatedOrder.getStatus().name());
    }

    public void getOrdersByStatus(Context ctx){
        if (ctx.sessionAttribute("userID") == null) {
            ctx.status(400);
            ctx.json(new ErrorMessage("You must be logged to do that"));
            logger.warn("Attempt of show orders without logging");
            return;
        }
        User loggedUser = userService.getUserByID(ctx.sessionAttribute("userID"));

        if (loggedUser.getRole() != Role.ADMIN) {
            ctx.status(401);
            ctx.json(new ErrorMessage("You do not have permission to do that"));
            logger.warn("Attempt of show order status without permission by user it ID: " + loggedUser.getUserID());
            return;
        }
        if (!orderService.validateStatus(ctx.pathParam("status"))) {
            ctx.status(404);
            ctx.json(new ErrorMessage("Status invalid"));
            logger.warn("Attempt of show order status with invalid status by user with ID: " + ctx.sessionAttribute("userID"));
            return;
        }

        List<Order> orders = orderService.getOrderByStatus(ctx.pathParam("status"));
        if (orders == null) {
            ctx.status(400);
            ctx.json(new ErrorMessage("Something went wrong updating the status"));
            logger.error("Something went wrong showing the order status");
            return;
        }

        ctx.status(200);
        ctx.json(orders);
    }
}