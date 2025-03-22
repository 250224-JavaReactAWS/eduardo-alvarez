package com.revature.repos;

import com.revature.models.CartItem;
import com.revature.models.Product;
import com.revature.models.User;

import java.util.List;

public interface CartItemDAO extends GeneralDAO<CartItem>{
   boolean removeItem(User user, Product product);
   CartItem updateQuantity(int quantity);
   void loadProduct(Product product);
   List<CartItem> getAllCartItems(int userID);
}
