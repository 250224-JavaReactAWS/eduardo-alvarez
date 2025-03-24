import com.revature.models.Role;
import com.revature.models.User;
import com.revature.repos.UserDAO;
import com.revature.repos.UserDAOPostgres;
import com.revature.services.UserService;
import org.checkerframework.checker.index.qual.PolyUpperBound;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.swing.plaf.PanelUI;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class UserServiceTest {
    private UserDAOPostgres mockDAO;
    private UserService userService;

    @Before
    public void setup() {
        mockDAO = Mockito.mock(UserDAOPostgres.class);
        userService = new UserService(mockDAO);
    }

    @Test
    public void validEmailShouldReturnTrue() {
        String email = "MyEmail@gmail.com";

        boolean result = userService.validateEmail(email);

        assertTrue(result);
    }

    @Test
    public void invalidEmailShouldReturnFalse() {
        String email = "MyEmailgmail.com";
        boolean result = userService.validateEmail(email);
        assertFalse(result);
    }

    @Test
    public void nullPasswordShouldReturnFalse() {
        String password = null;
        assertFalse(userService.validatePassword(password));
    }

    @Test
    public void shortPasswordShouldReturnFalse() {
        String password = "short";
        assertFalse(userService.validatePassword(password));
    }

    @Test
    public void passwordWitNoUpperCaseShouldReturnFalse() {
        String password = "password1234";
        assertFalse(userService.validatePassword(password));
    }

    @Test
    public void passwordWitNoLowerCaseShouldReturnFalse() {
        String password = "PASSWORD124";
        assertFalse(userService.validatePassword(password));
    }

    @Test
    public void validPasswordShouldReturnTrue() {
        String password = "Password124";
        assertTrue(userService.validatePassword(password));
    }

    @Test
    public void loginWithNullUserShouldReturnNull() {
        // Arrange
        String email = "username";
        String password = "password";
        when(mockDAO.getUserByEmail(email)).thenReturn(null);
        // This means the user was not found

        // Act
        User returnedUser = userService.loginUser(email, password);

        // Assert
        Assert.assertNull(returnedUser);
    }

    @Test
    public void incorrectLoginInfoShouldNull() {
        // Arrange
        String email = "username";
        String password = "password";
        User user = new User("Luigi", "Bros", "luigi@nontendo.com", "Mariobros1234");
        when(mockDAO.getUserByEmail(email)).thenReturn(user);
        // This means the user was not found

        // Act
        User returnedUser = userService.loginUser(email, password);

        // Assert
        Assert.assertNull(returnedUser);
    }

    @Test
    public void correctLoginInfoShouldReturnUser() {
        // Arrange
        String email = "ori@ori.org";
        String password = "WOWwow12345";
        User user = new User("Ori", "the dog", "ori@ori.org", "WOWwow12345");
        when(mockDAO.getUserByEmail(email)).thenReturn(user);

        // Act
        User returnedUser = userService.loginUser(email, password);

        // Assert
        Assert.assertEquals(user, returnedUser);
    }

    @Test
    public void registerUserWithTakenEmailShouldReturnNull() {
        User user = new User("Ori", "the dog", "ori@ori.org", "WOWwow12345");
        when(mockDAO.getUserByEmail(user.getEmail())).thenReturn(user);
        User registeredUser = userService.registerNewUser("Ori2", "The cat", "ori@ori.org", "PASSword12");
        assertNull(registeredUser);
    }

    @Test
    public void adminUserShouldReturnTrue(){
        User user = new User(1, "test", "ori@ori.org", "WOWwow12345", "ADMIN");
        user.setRole(Role.ADMIN);
        boolean result = userService.isAdmin(user);
        assertTrue(result);
    }
    @Test
    public void normalUserShouldReturnFalse(){
        User user = new User(1, "test", "ori@ori.org", "WOWwow12345", "ADMIN");
        boolean result = userService.isAdmin(user);
        assertFalse(result);
    }

    @Test
    public void availableEmailShouldReturnTrue(){
        String email = "MyEmail@gmail.com";
        User falseUser = new User(1,"test","test",email,"test");
        when(mockDAO.getUserByEmail(falseUser.getEmail())).thenReturn(null);
        boolean result = userService.isEmailAvailable(email);
        assertTrue(result);
    }
    @Test
    public void unvailableEmailShouldReturnFalse(){
        String email = "MyEmail@gmail.com";
        User falseUser = new User(1,"test","test",email,"test");
        when(mockDAO.getUserByEmail(falseUser.getEmail())).thenReturn(falseUser);
        boolean result = userService.isEmailAvailable(email);
        assertFalse(result);
    }

    @Test
    public void unregisteredUserShouldReturnNull(){
        User falseUser = new User(1,"test","test","email","test");
        when(mockDAO.getByID(1)).thenReturn(null);
        User result = userService.getUserByID(1);
        assertNull(result);
    }

    @Test
    public void registeredUserShouldReturnNotNull(){
        User falseUser = new User(1,"test","test","email","test");
        when(mockDAO.getByID(1)).thenReturn(falseUser);
        User result = userService.getUserByID(1);
        assertNotNull(result);
    }
}
