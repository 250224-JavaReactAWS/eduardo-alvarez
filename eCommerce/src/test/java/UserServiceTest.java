import com.revature.repos.UserDAO;
import com.revature.repos.UserDAOPostgres;
import com.revature.services.UserService;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserServiceTest {
    private UserDAO userDAO = new UserDAOPostgres();
    private UserService userService = new UserService(userDAO);

    @Test
    public void validEmail(){
        String email = "MyEmail@gmail.com";

        boolean result = userService.validateEmail(email);

        assertTrue(result);
    }

    @Test
    public void invalidEmail(){
        String email = "MyEmailgmail.com";

        boolean result = userService.validateEmail(email);

        assertFalse(result);
    }
}
