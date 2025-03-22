package com.revature.services;

import com.revature.models.CartItem;
import com.revature.models.Order;
import com.revature.models.Product;
import com.revature.models.User;
import com.revature.repos.CartItemDAO;

import java.util.List;

public class CartItemService {
    private final CartItemDAO cartItemDAO;

    public CartItemService(CartItemDAO cartItemDAO){
        this.cartItemDAO = cartItemDAO;
    }

    public CartItem registerCartItem(int user_id, int product_id, int quantity) {
        CartItem newCartItem = new CartItem(user_id, product_id, quantity);
        return cartItemDAO.create(newCartItem);
    }

    public List<CartItem> getAllCartItems(int userID){
        return cartItemDAO.getAllCartItems(userID);
    }

    public boolean validateItem(int productID, int userID) {
        boolean result = true;
        List<CartItem> currentItemsInCart = cartItemDAO.getAllCartItems(userID);

        if (currentItemsInCart != null) {
            for (CartItem item : currentItemsInCart) {
                if (item.getProductID() == productID) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    public boolean removeProduct(int productID, int userID){
        return cartItemDAO.removeItem(productID,userID);
    }

    public CartItem updateQuantity(CartItem cartItem){
        if(cartItem==null){
            return null;
        }
        if(cartItem.getQuantity()<=0){
            System.out.println("Cantidad invalida");
            return null;
        }
        return cartItemDAO.updateQuantity(cartItem);
    }
}
