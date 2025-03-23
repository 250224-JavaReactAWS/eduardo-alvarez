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

import java.util.List;

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
        if (loggerUser.getRole() != Role.ADMIN) {
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

    public void getAllProducts(Context ctx) {
        if (ctx.sessionAttribute("userID") == null) {
            ctx.status(401);
            ctx.json(new ErrorMessage("You must be logged to see the products"));
            logger.warn("Attempt of show all products without session");
            return;
        }
        ctx.status(200);
        ctx.json(productService.getAllProducts());
    }

    public void updateProduct(Context ctx) {
        Product productWithNewInfo = ctx.bodyAsClass(Product.class);
        User loggedUser;
        if (ctx.sessionAttribute("userID") == null) {
            ctx.status(401);
            ctx.json(new ErrorMessage("You must be logged to do this actions"));
            logger.warn("Attempt to update product info without login");
            return;
        } else {
            loggedUser = userService.getUserByID(ctx.sessionAttribute("userID"));
        }

        if (loggedUser.getRole() != Role.ADMIN) {
            ctx.status(401);
            ctx.json(new ErrorMessage("You do not have permission to do this action"));
            logger.warn("Attempt of update without permission by user with ID: " + loggedUser.getUserID());
            return;
        }

        if (productWithNewInfo.getProductID() == 0) {
            ctx.status(400);
            ctx.json(new ErrorMessage("Product ID not provided"));
            logger.warn("Attempt of update product without ID");
            return;
        }

        Product updatedProduct = productService.updateProduct(productWithNewInfo);
        if (updatedProduct == null) {
            ctx.json(new ErrorMessage("User update failed"));
            ctx.status(400);
            return;
        }

        ctx.status(200);
        logger.info("Product with ID: " + updatedProduct.getProductID() + " was updated");
        ctx.json(updatedProduct);
    }

    public void getProductByID(Context ctx) {

        if (ctx.sessionAttribute("userID") == null) {
            ctx.status(400);
            ctx.json(new ErrorMessage("You have to be logged to do this action"));
            logger.warn("Attempt of get product by id without logging");
            return;
        } else {
            User loggeUser = userService.getUserByID(ctx.sessionAttribute("userID"));
        }
        Product foundProduct = productService.getProductByID(Integer.parseInt(ctx.pathParam("ID")));
        if (foundProduct == null) {
            ctx.status(404);
            ctx.json(new ErrorMessage("Product not founded"));
            logger.warn("Product with ID " + ctx.pathParam("ID") + " not found");
            return;
        }

        ctx.status(200);
        ctx.json(foundProduct);
    }

    public void deleteProduct(Context ctx) {
        User loggedUser;
        if (ctx.sessionAttribute("userID") == null) {
            ctx.status(400);
            ctx.json(new ErrorMessage("You must be logged to do that"));
            logger.warn("Attempt of delete product without logging");
            return;
        } else {
            loggedUser = userService.getUserByID(ctx.sessionAttribute("userID"));
        }

        if (loggedUser.getRole() != Role.ADMIN) {
            ctx.status(401);
            ctx.json("You do not have permission to do that");
            logger.warn("Attempt of product deletion by user with ID: " + loggedUser.getUserID());
            return;
        }

        Product deletedProduct = productService.getProductByID(Integer.parseInt(ctx.pathParam("ID")));
        if (deletedProduct == null) {
            ctx.status(404);
            ctx.json(new ErrorMessage("Product not found"));
            logger.warn("Attempt of delete a non-existent product with ID " + ctx.pathParam("ID") + " by user with ID " + loggedUser.getUserID());
            return;
        }

        if (productService.deleteProduct(deletedProduct.getProductID())) {
            ctx.status(200);
            ctx.json("Product deleted");
            logger.info("Product " + deletedProduct.getName() + " with ID " + deletedProduct.getProductID() + " was deleted by user with ID " + loggedUser.getUserID());
        } else {
            ctx.status(500);
            ctx.json(new ErrorMessage("Something went wrong deleting the product"));
            logger.error("Something went wrong deleting the product");
        }
    }
}
