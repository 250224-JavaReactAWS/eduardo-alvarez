import com.revature.models.Product;
import com.revature.repos.ProductDAOPostgres;
import com.revature.services.ProductService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

public class ProductServiceTest {
    private ProductDAOPostgres mockDAO;
    private ProductService productService;

    @Before
    public void setup() {
        mockDAO = Mockito.mock(ProductDAOPostgres.class);
        productService = new ProductService(mockDAO);
    }

    @Test
    public void negativePriceShouldReturnFalse() {
        float price = -0.1f;
        boolean result = productService.validatePrice(price);
        Assert.assertFalse(result);
    }

    @Test
    public void positivePriceShouldReturnTrue() {
        float price = 0.1f;
        boolean result = productService.validatePrice(price);
        Assert.assertTrue(result);
    }

    @Test
    public void zeroPriceShouldReturnTrue() {
        float price = 0;
        boolean result = productService.validatePrice(price);
        Assert.assertTrue(result);
    }
    @Test
    public void negativeStockShouldReturnFalse() {
        int stock = -1;
        boolean result = productService.validateStock(stock);
        Assert.assertFalse(result);
    }

    @Test
    public void positiveStockShouldReturnTrue() {
        int stock = 1;
        boolean result = productService.validateStock(stock);
        Assert.assertTrue(result);
    }

    @Test
    public void zeroStockShouldReturnTrue() {
        int stock = 0;
        boolean result = productService.validateStock(stock);
        Assert.assertTrue(result);
    }

    @Test
    public void emptyStringShouldReturnTrue(){
        String s="";
        boolean result = productService.validateName(s);
        Assert.assertTrue(result);
    }
    @Test
    public void blankStringShouldReturnTrue(){
        String s="     ";
        boolean result = productService.validateName(s);
        Assert.assertTrue(result);
    }
    @Test
    public void filledStringShouldReturnFalse(){
        String s="HOla";
        boolean result = productService.validateName(s);
        Assert.assertFalse(result);
    }
    @Test
    public void getAllShouldReturnAList(){
        List<Product> fakeProducts = new ArrayList<>();
        for(int i =0; i<10;i++){
            Product fakeProduct = new Product("test","test",5f,5);
            fakeProducts.add(fakeProduct);
        }
        when(mockDAO.getAll()).thenReturn(fakeProducts);

        List<Product> products = productService.getAllProducts();

        Assert.assertEquals(products,fakeProducts);
    }

    @Test
    public void existingProductShouldReturnNotNull(){
        Product fakeRegisteredProduct = new Product("test","test",5f,5);
        fakeRegisteredProduct.setProductID(1);
        when(mockDAO.getByID(1)).thenReturn(fakeRegisteredProduct);

        Product foundProduct = productService.getProductByID(1);
        Assert.assertNotNull(foundProduct);
    }

    @Test
    public void nonexistingProductShouldReturnNull(){
        Product fakeRegisteredProduct = new Product("test","test",5f,5);
        fakeRegisteredProduct.setProductID(1);
        when(mockDAO.getByID(1)).thenReturn(null);

        Product foundProduct = productService.getProductByID(1);
        Assert.assertNull(foundProduct);
    }

    @Test
    public void returnTrueWhenDelete(){
        when(mockDAO.deleteById(1)).thenReturn(true);
        boolean result = productService.deleteProduct(1);
        Assert.assertTrue(result);
    }

    @Test
    public void registerProductWithValidDataShouldReturnNotNull(){
        Product fakeRequestProduct = new Product("test","test",5f,5);
        Product fakeRegisteredProduct = new Product("test","test",5f,5);
        fakeRegisteredProduct.setProductID(1);
        when(mockDAO.create(fakeRegisteredProduct)).thenReturn(fakeRegisteredProduct);
    }

    @Test
    public void registerValidUserShouldReturnNotNull(){
        int productID = 0;
        String name = "MyProduct";
        String description = "A product";
        float price = 5f;
        int stock = 140;
        Product mockProduct = new Product(productID,name,description,price,stock);
        Product requestProduct = new Product(name,description,price,stock);

        when(mockDAO.create(requestProduct)).thenReturn(mockProduct);

        Product registerProduct = productService.registerNewProduct(requestProduct);
        Assert.assertNotNull(registerProduct);
    }
}
