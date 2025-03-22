package com.revature.controllers;

import com.revature.dtos.response.ErrorMessage;
import com.revature.models.CartItem;
import com.revature.models.Product;
import com.revature.models.User;
import com.revature.services.CartItemService;
import com.revature.services.ProductService;
import com.revature.services.UserService;
import io.javalin.http.Context;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;


public class CartItemController {
    private final UserService userService;
    private final ProductService productService;
    private final CartItemService cartItemService;
    private final Logger logger = LoggerFactory.getLogger(CartItem.class);

    public CartItemController(UserService userService, ProductService productService, CartItemService cartItemService) {
        this.userService = userService;
        this.cartItemService = cartItemService;
        this.productService = productService;
    }

    public void registerCartItem(Context ctx) {
        CartItem requestCartItem = ctx.bodyAsClass(CartItem.class);
        User loggedUser;
        if (ctx.sessionAttribute("userID") == null) {
            ctx.status();
            ctx.json(new ErrorMessage("You must be logged to do that action"));
            logger.warn("Attempt of register cart item without logging");
            return;
        } else {
            loggedUser = userService.getUserByID(ctx.sessionAttribute("userID"));
            requestCartItem.setUserID(loggedUser.getUserID());
        }

        Product requestProduct = productService.getProductByID(requestCartItem.getProductID());
        if (requestProduct == null) {
            ctx.status(404);
            ctx.json(new ErrorMessage("Product not found"));
            return;
        }

        if (!cartItemService.validateItem(requestProduct.getProductID(), loggedUser.getUserID())) {
            ctx.status(400);
            ctx.json(new ErrorMessage("Item already in the cart"));
            logger.warn("Attempt of add repeated products to the cart");
            return;
        }

        if (requestCartItem.getQuantity() <= 0) {
            ctx.status(400);
            ctx.json(new ErrorMessage("Invalid quantity"));
            logger.warn("Attempt of register Cart item with invalid quantity");
            return;
        }

        System.out.println("User ID: " + loggedUser.getUserID() + " Product ID: " + requestCartItem.getProductID() + " Quantity: " + requestCartItem.getQuantity());
        CartItem registeredCartItem = cartItemService.registerCartItem(loggedUser.getUserID(), requestCartItem.getProductID(), requestCartItem.getQuantity());
        if (registeredCartItem == null) {
            ctx.status(500);
            ctx.json(new ErrorMessage("Something went wrong trying to register new cart item"));
            return;
        }

        ctx.status(201);
        ctx.json(registeredCartItem);
    }

    public void removedItemFromCart(Context ctx) {
        CartItem requestCartItem = ctx.bodyAsClass(CartItem.class);
        if (ctx.sessionAttribute("userID") == null) {
            ctx.status(400);
            ctx.json(new ErrorMessage("You must be logged to do that"));
            logger.warn("Attempt of delete cart item without logging");
            return;
        } else {
            requestCartItem.setUserID(ctx.sessionAttribute("userID"));
        }
        if (cartItemService.removeProduct(requestCartItem.getProductID(), requestCartItem.getUserID())) {
            ctx.status(200);
            ctx.json("Item removed");
            logger.info("Item with ID: " + requestCartItem.getProductID() + " removed from user cart with ID: " + requestCartItem.getUserID() + " was deleted by user with ID " + requestCartItem.getUserID());
        } else {
            ctx.status(500);
            ctx.json(new ErrorMessage("Something went wrong removing the product from cart"));
            logger.error("Something went wrong removing the product from cart");
        }
    }

    public void updateCartItemQuantity(Context ctx) {
        CartItem requestCartItem = ctx.bodyAsClass(CartItem.class);
        if (ctx.sessionAttribute("userID") == null) {
            ctx.status(400);
            ctx.json(new ErrorMessage("You must be logged to do that"));
            logger.warn("Attempt of update cart item quantity without logging");
            return;
        } else {
            requestCartItem.setUserID(ctx.sessionAttribute("userID"));
        }

        if (requestCartItem.getQuantity() <= 0) {
            ctx.status(400);
            ctx.json(new ErrorMessage("Invalid quantity"));
            logger.warn("Attempt of update invalid cart item with invalid quantity by user with ID: " + ctx.sessionAttribute("userID"));
            return;
        }

        CartItem updatedCartItem = cartItemService.updateQuantity(requestCartItem);
        if (updatedCartItem == null) {
            ctx.status(500);
            ctx.json(new ErrorMessage("Something went wrong"));
            logger.error("Something went wrong updating the quantity of item with ID: " + requestCartItem.getProductID() + " for the user cart: " + requestCartItem.getUserID());
            return;
        }
        ctx.status(200);
        ctx.json(updatedCartItem);
        logger.info("Cart item quantity updated for product ID: " + updatedCartItem.getProductID() + " for user: " + updatedCartItem.getUserID());
    }
}
