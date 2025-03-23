package com.revature.controllers;

import com.revature.dtos.response.ErrorMessage;
import com.revature.models.OrderItem;
import com.revature.services.OrderItemService;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class OrderItemController {
    private final OrderItemService orderItemService;
    private final Logger logger = LoggerFactory.getLogger(ProductController.class);

    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    public void getPastOrders(Context ctx) {
        if (ctx.sessionAttribute("userID") == null) {
            ctx.status(400);
            ctx.json(new ErrorMessage("You must be logged to do that"));
            logger.warn("Attempt of see past orders without logging");
            return;
        }

        List<OrderItem> pastOrders = orderItemService.getPastOrders(ctx.sessionAttribute("userID"));

        ctx.status(200);
        ctx.json(pastOrders);
        logger.info("Past orders shown for user with id: " + ctx.sessionAttribute("userID"));
    }
}
