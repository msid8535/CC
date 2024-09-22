import currency.Currency;
import currency.CurrencyManager;
import user.User;
import user.UserManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Manages the application and handles requests for currency functions.
 */
public class AppManager {

    private CurrencyManager currencyManager = new CurrencyManager();
    private UserManager userManager = new UserManager();
    private User currentUser;

    /**
     * Gets the current user logged in to the system.
     * @return the current user as a User object.
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Sets the current logged-in user.
     * @param currentUser the user to be logged in to the system.
     */
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public CurrencyManager getCurrencyManager() {
        return currencyManager;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public HashMap<String, Float> getRates() {
        return currencyManager.getRates();
    }

    public HashMap<String, Currency> getCurrencies() {
        return currencyManager.getCurrencies();
    }

    public HashMap<String, Integer> getRateChanges() {
        return currencyManager.getRateChanges();
    }

    public void setCommonCurrencies(List<String> commonCurrencies) {
        currencyManager.setCommonCurrencies(commonCurrencies);
    }

    public List<String> getCommonCurrencies() {
        return currencyManager.getCommonCurrencies();
    }

    public void closeConnections() {
        userManager.closeConnection();
        currencyManager.closeConnection();
    }

    /**
     * TODO
     * Checks if a currency exists in the currency database.
     * @param currencyName the three-letter code for the currency (ISO code 4217)
     * @return true if the specified currency is in the database, false otherwise
     */
    public boolean checkCurrency(String currencyName) {
        return currencyManager.getCurrencies().containsKey(currencyName);
    }

    /**
     * TODO
     * Adds an exchange rate.
     * When an exchange rate is added, the reciprocal change rate is also added.
     * If the exchange rate already exists, then store the old exchange rate in history and override with the new rate.
     * @param baseCurrency the three-letter code for the base currency
     * @param targetCurrency the three-letter code for the target currency
     * @param rate the exchange rate
     */
    public void addExchangeRate(String baseCurrency, String targetCurrency, float rate) {
        currencyManager.addRate(baseCurrency, targetCurrency, rate, 0);
    }

    public void editExchangeRate(String baseCurrency, String targetCurrency, float rate) {
        float oldRate = currencyManager.getRates().get(baseCurrency + "/" + targetCurrency);

        if (oldRate > rate) {
            currencyManager.addRate(baseCurrency, targetCurrency, rate, 1);
        } else if (oldRate < rate) {
            currencyManager.addRate(baseCurrency, targetCurrency, rate, 2);
        } else {
            currencyManager.addRate(baseCurrency, targetCurrency, rate, 0);
        }
    }

    /**
     * Adds a currency to the currencies database.
     * @param name the name of the currency
     * @param code the three-letter code of the currency (ISO code 4217)
     * @param symbol the symbol of the currency
     */
    public void addCurrency(String name, String code, String symbol) {
        currencyManager.addCurrency(new Currency(name, code, symbol));
    }

    /**
     * Gets the current exchange rate between a base and target currency.
     * @param baseCurrency the three-letter code for the base currency
     * @param targetCurrency the three-letter code for the target currency
     * @return
     */
    public float getExchangeRate(String baseCurrency, String targetCurrency) {
        String key = baseCurrency + "/" + targetCurrency;
        if (currencyManager.getRates().containsKey(key)) {
            return currencyManager.getRates().get(key);
        } else {
            return 0;
        }
    }

    public boolean hasExchangeRate(String baseCurrency, String targetCurrency) {
        return currencyManager.getRates().containsKey(baseCurrency + "/" + targetCurrency);
    }

}
