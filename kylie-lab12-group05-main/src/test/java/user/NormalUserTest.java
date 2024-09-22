package user;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class NormalUserTest {

    @Test
    public void testIsNormalUser() {
        NormalUser normalUser = new NormalUser("normalUser", "password");
        assertTrue(normalUser.isNormalUser());
    }
}
