import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Scanner;
import user.User;

public class MainClassTest {

    private AppManager appManager;
    private Scanner scanner;

    @BeforeEach
    public void setUp() {
        appManager = new AppManager();
        MainClass.setAppManager(appManager);
        scanner = new Scanner(System.in);
    }
// TODO: Fix this test
    // @Test
    // public void testLogin_ValidUser() {
    //     User testUser = new User("testuser", "password", false);
    //     appManager.getUserManager().addUser(testUser);

    //     assertNotNull(appManager.getUserManager().authenticateUser("testuser", "password"),
    //             "User was not added correctly to the UserManager");

    //     scanner = new Scanner("1\ntestuser\npassword\n");

    //     MainClass.login(scanner);

    //     assertNotNull(appManager.getCurrentUser(), "User was not logged in");
    //     assertEquals("testuser", appManager.getCurrentUser().getUsername(),
    //             "Logged in user does not match the expected user");
    // }

    @Test
    public void testLogin_InvalidUser() {

        scanner = new Scanner("1\ninvaliduser\nwrongpassword\n");

        MainClass.login(scanner);

        assertNull(appManager.getCurrentUser());
    }

    @Test
    public void testSignUp_NewUser() {

        scanner = new Scanner("2\nnewuser\npassword\nN\n");
        MainClass.signUp(scanner);

        assertNotNull(appManager.getUserManager().authenticateUser("newuser", "password"));
        assertEquals("newuser", appManager.getUserManager().authenticateUser("newuser", "password").getUsername());
    }

    @Test
    public void testSignUp_ExistingUser() {

        appManager.getUserManager().addUser(new User("existinguser", "password", false));

        scanner = new Scanner("2\nexistinguser\npassword\nN\n");

        MainClass.signUp(scanner);

        User user = appManager.getUserManager().authenticateUser("existinguser", "password");
        assertNotNull(user);
        assertEquals("existinguser", user.getUsername());
    }

    @Test
public void testLogin_EmptyUsername() {

    scanner = new Scanner("\npassword\n");

    
    MainClass.login(scanner);

    
    assertNull(appManager.getCurrentUser());
}



}
