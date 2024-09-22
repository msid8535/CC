import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import user.User;

import java.util.ArrayList;
import java.util.List;

public class AppManagerTest {

    private AppManager appManager;

    @BeforeEach
    public void setUp() {
        appManager = new AppManager();
    }

    @Test
    public void testGetCurrentUser() {
        User user = new User("testUser", "password", false);
        appManager.setCurrentUser(user);
        assertEquals("testUser", appManager.getCurrentUser().getUsername());
    }

    @Test
    public void testAddExchangeRate() {
        appManager.addExchangeRate("XXX", "YYY", 1.12f);
        assertTrue(appManager.getRates().containsKey("XXX/YYY") && appManager.getExchangeRate("XXX", "YYY") == 1.12f);
    }

    @Test
    public void testSetCommonCurrencies() {
        List<String> commonCurrencies = new ArrayList<>();
        commonCurrencies.add("EUR");
        commonCurrencies.add("USD");
        commonCurrencies.add("GBP");
        commonCurrencies.add("CHF");
        appManager.setCommonCurrencies(commonCurrencies);
        assertEquals(commonCurrencies, appManager.getCommonCurrencies());
    }

    @Test
    public void testAddCurrency() {
        String name = "US Dollar";
        String code = "USD";
        String symbol = "$";
        appManager.addCurrency(name, code, symbol);
        assertEquals("USD", appManager.getCurrencies().get("US Dollar").getCode());
    }

    @Test
    public void testHasExchangeRate() {
        assertFalse(appManager.hasExchangeRate("ABC", "DEF"));
    }

    @Test
    public void testCheckCurrency() {
        assertTrue(appManager.checkCurrency("US Dollar"));
    }

    @Test
    public void EditExchangeRateTest() {
        appManager.addExchangeRate("EUR", "USD", 1.12f);
        appManager.editExchangeRate("EUR", "USD", 1.10f);
        assertTrue(appManager.getExchangeRate("EUR", "USD") == 1.10f && appManager.getRateChanges().get("EUR/USD") == 1);
    }
}
