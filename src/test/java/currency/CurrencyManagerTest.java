package currency;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CurrencyManagerTest {

    private CurrencyManager currencyManager;

    @BeforeEach
    public void setUp() {
        currencyManager = new CurrencyManager();
    }

    @Test
    public void testAddCurrency() {
        Currency usd = new Currency("US Dollar", "USD", "$");
        currencyManager.addCurrency(usd);
        assertEquals("USD", currencyManager.getCurrencies().get("US Dollar").getCode());
    }

    @Test
    public void testAddRate() {
        currencyManager.addRate("XXX", "YYY", 1.12f, 0);
        assertEquals(1.12f, currencyManager.getRates().get("XXX/YYY"));
    }

    @Test
    public void testGetHistoricalRate() {
        currencyManager.addRate("EUR", "USD", 1.12f, 0);
        assertTrue(currencyManager.getHistoricalRates("EUR", "USD", "2000-01-01", "2001-01-01").isEmpty());
    }
}
