import com.revature.models.CartItem;
import com.revature.models.Order;
import com.revature.models.Product;
import com.revature.repos.OrderPostgres;
import com.revature.services.OrderService;
import net.bytebuddy.implementation.auxiliary.MethodCallProxy;
import org.checkerframework.checker.units.qual.A;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Or;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

public class OrderServiceTest {
    private OrderService orderService;
    private OrderPostgres mockDAO;

    @Before
    public void setup(){
        mockDAO = Mockito.mock(OrderPostgres.class);
        orderService = new OrderService(mockDAO);
    }
    @Test
    public void negativeStockShouldReturnFalse(){
        int stock = -1;
        Product product = new Product("test","test",5f,5);
        CartItem cartItem = new CartItem(1,1,10);
        boolean result = orderService.validateStock(product,cartItem);
        Assert.assertFalse(result);
    }
    @Test
    public void positiveStockShouldReturnTrue(){
        int stock = -1;
        Product product = new Product("test","test",5f,5);
        CartItem cartItem = new CartItem(1,1,4);
        boolean result = orderService.validateStock(product,cartItem);
        Assert.assertTrue(result);
    }
    @Test
    public void zeroStockShouldReturnTrue(){
        int stock = -1;
        Product product = new Product("test","test",5f,5);
        CartItem cartItem = new CartItem(1,1,5);
        boolean result = orderService.validateStock(product,cartItem);
        Assert.assertTrue(result);
    }

    @Test
    public void showAllOrdersShouldReturnListNotNull(){
        List<Order> fakeOrders = new ArrayList<>();
        for(int i =0; i<10;i++){
            Order fakeOrder = new Order(1,50f);
            fakeOrders.add(fakeOrder);
        }
        when(mockDAO.getAll()).thenReturn(fakeOrders);

        List<Order> orders= orderService.getAllOrders();

        Assert.assertEquals(orders,fakeOrders);
    }

    @Test
    public void validStatusShouldReturnTrue(){
        String s = "DELIVERED";
        boolean result  = orderService.validateStatus(s);
        Assert.assertTrue(result);
    }
    @Test
    public void invalidStatusShouldReturnFalse(){
        String s = "Not a real status";
        boolean result  = orderService.validateStatus(s);
        Assert.assertFalse(result);
    }
}