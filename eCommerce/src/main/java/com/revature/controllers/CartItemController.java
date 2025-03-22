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
    private  final ProductService productService;
    private final CartItemService cartItemService;
    private final Logger logger = LoggerFactory.getLogger(CartItem.class);
    public CartItemController(UserService userService, ProductService productService,CartItemService cartItemService){
        this.userService = userService;
        this.cartItemService = cartItemService;
        this.productService=productService;
    }

    public void registerCartItem(Context ctx){
        CartItem requestCartItem = ctx.bodyAsClass(CartItem.class);
        User loggedUser;
        if(ctx.sessionAttribute("userID")==null){
            ctx.status();
            ctx.json(new ErrorMessage("You must be logged to do that action"));
            logger.warn("Attempt of register cart item without logging");
            return;
        }else {
            loggedUser = userService.getUserByID(ctx.sessionAttribute("userID"));
            requestCartItem.setUserID(loggedUser.getUserID());
        }

        Product requestProduct = productService.getProductByID(requestCartItem.getProductID());
        if(requestProduct==null){
            ctx.status(404);
            ctx.json(new ErrorMessage("Product not found"));
            return;
        }

        if(!cartItemService.validateItem(requestProduct.getProductID(),loggedUser.getUserID())){
            ctx.status(400);
            ctx.json(new ErrorMessage("Item already in the cart"));
            logger.warn("Attempt of add repeated products to the cart");
            return;
        }

        if(requestCartItem.getQuantity()<=0){
            ctx.status(400);
            ctx.json(new ErrorMessage("Invalid quantity"));
            logger.warn("Attempt of register Cart item with invalid quantity");
            return;
        }

        System.out.println("User ID: "+loggedUser.getUserID()+" Product ID: "+requestCartItem.getProductID()+" Quantity: "+requestCartItem.getQuantity());
        CartItem registeredCartItem = cartItemService.registerCartItem(loggedUser.getUserID(), requestCartItem.getProductID(), requestCartItem.getQuantity());
        if (registeredCartItem == null) {
            ctx.status(500);
            ctx.json(new ErrorMessage("Something went wrong trying to register new cart item"));
            return;
        }

        ctx.status(201);
        ctx.json(registeredCartItem);
    }
}
