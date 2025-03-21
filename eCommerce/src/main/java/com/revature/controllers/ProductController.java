package com.revature.controllers;

import com.revature.dtos.response.ErrorMessage;
import com.revature.models.Product;
import com.revature.models.Role;
import com.revature.models.User;
import com.revature.services.ProductService;
import com.revature.services.UserService;
import io.javalin.http.Context;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class ProductController {
    private final ProductService productService;
    private final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final UserService userService;

    public ProductController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    public void registerNewProuct(Context ctx) {
        User loggerUser = userService.getUserByID(ctx.sessionAttribute("userID"));
        if (loggerUser.getRole()!= Role.ADMIN){
            ctx.status(401);
            ctx.json(new ErrorMessage("You do not have permission"));
            logger.warn("Register product attempt without permission by user with ID: " + loggerUser.getUserID());
            return;
        }

            Product requestProduct = ctx.bodyAsClass(Product.class);
        System.out.println(requestProduct.getName());
        if (!productService.validatePrice(requestProduct.getPrice())) {
            ctx.status(400);
            ctx.json(new ErrorMessage("Price not valid"));
            logger.warn("Register product attempt with invalid price: " + requestProduct.getPrice());
            return;
        }
        if (!productService.validateStock(requestProduct.getStock())) {
            ctx.status(400);
            ctx.json(new ErrorMessage("Invalid stock amount"));
            logger.warn("Register product attempt with invalid stock amount: " + requestProduct.getStock());
            return;
        }

        if (productService.validateName(requestProduct.getName())) {
            ctx.status(400);
            ctx.json(new ErrorMessage("Invalid name"));
            logger.warn("Register user attempt with invalid product name: " + requestProduct.getName());
            return;
        }

        Product registeredProduct = productService.registerNewUser(requestProduct.getName(), requestProduct.getDescription(), requestProduct.getPrice(), requestProduct.getStock());
        if (registeredProduct == null) {
            ctx.status(500);
            ctx.json(new ErrorMessage("Something went wrong trying to register new product"));
            logger.error("Something went wrong trying to register the product with the info: " + requestProduct.toString());
            return;
        }

        logger.info("New product registered with ID: " + registeredProduct.getProductID() + " and name: " + registeredProduct.getName());
        ctx.status(201);
        ctx.json(registeredProduct);
    }

    public void getAllProducts(Context ctx){
        if(ctx.sessionAttribute("userID")==null){
            ctx.status(401);
            ctx.json(new ErrorMessage("You must be logged to see the products"));
            logger.warn("Attempt of show all products without session");
            return;
        }
        ctx.status(400);
        ctx.json(productService.getAllProducts());
    }
}
