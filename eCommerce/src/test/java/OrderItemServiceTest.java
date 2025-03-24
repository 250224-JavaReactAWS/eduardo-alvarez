import com.revature.models.OrderItem;
import com.revature.repos.OrderItemPostgress;
import com.revature.services.OrderItemService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

public class OrderItemServiceTest {
    private OrderItemService orderItemService;
    private OrderItemPostgress mockDAO;

    @Before
    public void setup() {
        mockDAO = Mockito.mock(OrderItemPostgress.class);
        orderItemService = new OrderItemService(mockDAO);
    }

    @Test
    public void registerOrderItemShouldReturnNotNull() {
        int orderItemID = 1;
        int orderID = 2;
        int productID = 3;
        int quantity = 4;
        float price = 5.5f;
        OrderItem mockRegister = new OrderItem(orderID, orderItemID, productID, quantity, price);
        OrderItem requestRegister = new OrderItem(orderItemID, productID, quantity, price);
        when(mockDAO.create(requestRegister)).thenReturn(mockRegister);

        OrderItem registeredOrderItem = orderItemService.registerOrderItem(requestRegister);
        Assert.assertNotNull(registeredOrderItem);
    }

    @Test
    public void getPastOrders() {
        List<OrderItem> pastOrders = new ArrayList<>();
        int userID = 0;
        int orderItemID = 1;
        int orderID = 2;
        int productID = 3;
        int quantity = 4;
        float price = 5.5f;
        for (int i = 0; i < 5; i++) {
            OrderItem mockRegister = new OrderItem(orderID, orderItemID, productID, quantity, price);
            pastOrders.add(mockRegister);
        }
        when(mockDAO.getUserPastOrders(userID)).thenReturn(pastOrders);

        List<OrderItem> orderItems = orderItemService.getPastOrders(userID);
        Assert.assertEquals(pastOrders, orderItems);
    }
}
