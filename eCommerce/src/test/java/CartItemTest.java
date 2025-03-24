import com.revature.models.CartItem;
import com.revature.models.Order;
import com.revature.repos.CartItemPostgres;
import com.revature.services.CartItemService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

public class CartItemTest {
    private CartItemService cartItemService;
    private CartItemPostgres mockDAO;

    @Before
    public void setup() {
        mockDAO = Mockito.mock(CartItemPostgres.class);
        cartItemService = new CartItemService(mockDAO);
    }

    @Test
    public void getCartItemShouldReturnNotNull() {
        List<CartItem> allItems = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            CartItem item = new CartItem(1, 1, 5);
            allItems.add(item);
        }

        when(mockDAO.getAllCartItems(1)).thenReturn(allItems);

        List<CartItem> orders = cartItemService.getAllCartItems(1);

        Assert.assertNotNull(orders);
    }

    @Test
    public void itemAlreadyInCartShouldReturnFalse() {
        int productID = 1;
        int userID = 1;
        List<CartItem> itemsInCart = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            CartItem cartItem = new CartItem(i, i, i, i);
            itemsInCart.add(cartItem);
        }
        when(mockDAO.getAllCartItems(userID)).thenReturn(itemsInCart);

        boolean result = cartItemService.validateItem(productID, userID);
        Assert.assertFalse(result);
    }

    @Test
    public void itemNotInCartShouldReturnTrue() {
        int productID = 5000;
        int userID = 1;
        List<CartItem> itemsInCart = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            CartItem cartItem = new CartItem(i, i, i, i);
            itemsInCart.add(cartItem);
        }
        when(mockDAO.getAllCartItems(userID)).thenReturn(itemsInCart);

        boolean result = cartItemService.validateItem(productID, userID);
        Assert.assertTrue(result);
    }

    @Test
    public void deleteShouldReturnTrue() {
        int productID = 1;
        int userID = 1;
        when(mockDAO.removeItem(productID, userID)).thenReturn(true);
        boolean result = cartItemService.removeProduct(productID, userID);
        Assert.assertTrue(result);
    }

    @Test
    public void updateQuantity() {
        int id = 1;
        int productID = 1;
        int userID = 1;
        int quantity = 2;
        CartItem requestUpdate = new CartItem(id, userID, productID, quantity);
        CartItem fakeUpdate = new CartItem(id, userID, productID, quantity);
        when(mockDAO.updateQuantity(requestUpdate)).thenReturn(fakeUpdate);

        CartItem updatedCartItem = cartItemService.updateQuantity(requestUpdate);
        Assert.assertEquals(fakeUpdate,updatedCartItem);
    }
}
