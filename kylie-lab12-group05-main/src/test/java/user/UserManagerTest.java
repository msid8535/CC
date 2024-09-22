package user;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserManagerTest {

    private UserManager userManager;

    @BeforeEach
    public void setUp() {
        userManager = new UserManager();
    }

    @Test
    public void testAddUser() {
        User user = new User("testUser", "password", false);
        userManager.addUser(user);
        assertTrue(userManager.userExists("testUser"));
    }

    @Test
    public void testAuthenticateUser() {
        User user = new User("testUser", "password", false);
        userManager.addUser(user);
        User authenticatedUser = userManager.authenticateUser("testUser", "password");
        assertNotNull(authenticatedUser);
        assertEquals("testUser", authenticatedUser.getUsername());
    }

}
