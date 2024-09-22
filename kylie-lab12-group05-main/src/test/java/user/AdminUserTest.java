package user;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class AdminUserTest {

    @Test
    public void testIsAdmin() {
        AdminUser admin = new AdminUser("adminUser", "password");
        assertTrue(admin.isAdmin());
    }
}
