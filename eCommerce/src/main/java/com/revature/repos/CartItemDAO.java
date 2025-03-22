package com.revature.repos;

import com.revature.models.CartItem;
import com.revature.models.Product;
import com.revature.models.User;

import java.util.List;

public interface CartItemDAO extends GeneralDAO<CartItem>{
   CartItem updateQuantity(CartItem cartItem);
   List<CartItem> getAllCartItems(int userID);
   public boolean removeItem(int productID, int userID);
}
