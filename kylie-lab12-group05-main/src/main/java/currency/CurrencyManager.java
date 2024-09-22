package currency;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CurrencyManager {
    private HashMap<String, Currency> currencies = new HashMap<>();
    private HashMap<String, Float> rates = new HashMap<>();
    private HashMap<String, Integer> ratesChange = new HashMap<>();
    private Connection connection;
    private static List<String> topCurrencies = new ArrayList<>();

    // Mohsin's HashMap for rates
    // private HashMap<String, CurrencyRate> rates = new HashMap<>();

    // Mohsin's functions
    // public void addCurrency(Currency currency) {
    // currencies.put(currency.getCode(), currency);
    // }
    //
    // public void addRate(Currency baseCurrency, Currency targetCurrency, Float
    // rate) {
    // String key = generateKey(baseCurrency, targetCurrency);
    // CurrencyRate currencyRate = new CurrencyRate(baseCurrency, targetCurrency,
    // rate, LocalDate.now());
    // rates.put(key, currencyRate);
    // }
    //
    // public void editRate(Currency baseCurrency, Currency targetCurrency, Float
    // rate) {
    // String key = generateKey(baseCurrency, targetCurrency);
    // if (rates.containsKey(key)) {
    // CurrencyRate updatedRate = new CurrencyRate(baseCurrency, targetCurrency,
    // rate, LocalDate.now());
    // rates.put(key, updatedRate);
    // } else {
    // System.out.println("Rate for this currency pair does not exist. Use addRate
    // instead.");
    // }
    // }
    //
    // public CurrencyRate getRate(Currency baseCurrency, Currency targetCurrency) {
    // String key = generateKey(baseCurrency, targetCurrency);
    // return rates.get(key);
    // }

    public CurrencyManager() {
        // Initialize SQLite connection
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:currency.db");
            createTables();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Create necessary tables
    private void createTables() throws SQLException {
        String createCurrenciesTable = "CREATE TABLE IF NOT EXISTS currencies (" +
                "name TEXT PRIMARY KEY, " +
                "code TEXT NOT NULL, " +
                "symbol TEXT NOT NULL);";

        String createRatesTable = "CREATE TABLE IF NOT EXISTS rates (" +
                "currency1 TEXT NOT NULL, " +
                "currency2 TEXT NOT NULL, " +
                "rate REAL NOT NULL, " +
                "change INTEGER NOT NULL, " +
                "PRIMARY KEY (currency1, currency2));";

        // Table for storing historical exchange rates
        String createRatesHistoryTable = "CREATE TABLE IF NOT EXISTS rates_history (" +
                "base_currency TEXT NOT NULL, " +
                "target_currency TEXT NOT NULL, " +
                "date TEXT NOT NULL, " +
                "rate REAL NOT NULL, " +
                "PRIMARY KEY (base_currency, target_currency, date));";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createCurrenciesTable);
            stmt.execute(createRatesTable);
            stmt.execute(createRatesHistoryTable);
        }
    }

    // Add a currency to the database
    public void addCurrency(Currency currency) {
        String sql = "INSERT OR IGNORE INTO currencies(name, code, symbol) VALUES(?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, currency.getName());
            pstmt.setString(2, currency.getCode());
            pstmt.setString(3, currency.getSymbol());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        currencies.put(currency.getName(), currency);
    }

    // Add a rate between two currencies to the database
    public void addRate(String currency1, String currency2, Float rate, int change) {
        String sql = "INSERT OR REPLACE INTO rates(currency1, currency2, rate, change) VALUES(?, ?, ?, ?)";
        String insertHistorySql = "INSERT OR REPLACE INTO rates_history(base_currency, target_currency, date, rate) " +
                "VALUES(?, ?, date('now'), ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             PreparedStatement historyPstmt = connection.prepareStatement(insertHistorySql)) {

            // Insert into rates table
            pstmt.setString(1, currency1);
            pstmt.setString(2, currency2);
            pstmt.setFloat(3, rate);
            pstmt.setInt(4, change);
            pstmt.executeUpdate();

            // Insert into rates_history table with the current date
            historyPstmt.setString(1, currency1);
            historyPstmt.setString(2, currency2);
            historyPstmt.setFloat(3, rate);
            historyPstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error adding rate: " + e.getMessage());
        }

        rates.put(currency1 + "/" + currency2, rate);
    }

    // Retrieve currencies from the database
    public HashMap<String, Currency> getCurrencies() {
        String sql = "SELECT * FROM currencies";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String name = rs.getString("name");
                String code = rs.getString("code");
                String symbol = rs.getString("symbol");
                currencies.put(name, new Currency(name, code, symbol));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return currencies;
    }

    // Retrieve rates from the database
    public HashMap<String, Float> getRates() {
        String sql = "SELECT * FROM rates";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String key = rs.getString("currency1") + "/" + rs.getString("currency2");
                float rate = rs.getFloat("rate");
                rates.put(key, rate);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return rates;
    }

    // Retrieve rates from the database
    public HashMap<String, Integer> getRateChanges() {
        String sql = "SELECT * FROM rates";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String key = rs.getString("currency1") + "/" + rs.getString("currency2");
                int change = rs.getInt("change");
                ratesChange.put(key, change);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return ratesChange;
    }

    // Get the most popular currency pairs by count
//    public HashMap<String, Float> getMostPopularRates() {
//        HashMap<String, Float> popularRates = new HashMap<>();
//        String sql = "SELECT currency1, currency2, rate FROM rates WHERE count > 0 ORDER BY count DESC LIMIT 5";
//
//        try (Statement stmt = connection.createStatement();
//             ResultSet rs = stmt.executeQuery(sql)) {
//            while (rs.next()) {
//                String key = rs.getString("currency1") + "/" + rs.getString("currency2");
//                float rate = rs.getFloat("rate");
//                popularRates.put(key, rate);
//            }
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//
//        return popularRates;
//    }

    // set list of top 4 currencies
    public void setCommonCurrencies(List<String> commonCurrencies) {
        topCurrencies = commonCurrencies;
    }

    public List<String> getCommonCurrencies() {
        return topCurrencies;
    }

    // Close the database connection
    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    public HashMap<String, Float> getHistoricalRates(String baseCurrency, String targetCurrency, String startDate, String endDate) {
        String sql = "SELECT date, rate FROM rates_history WHERE base_currency = ? AND target_currency = ? AND date BETWEEN ? AND ?";
        HashMap<String, Float> historicalRates = new HashMap<>();

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, baseCurrency);
            pstmt.setString(2, targetCurrency);
            pstmt.setString(3, startDate);
            pstmt.setString(4, endDate);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String date = rs.getString("date");
                float rate = rs.getFloat("rate");
                historicalRates.put(date, rate);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving historical rates: " + e.getMessage());
        }

        return historicalRates;
    }
    // public HashMap<String, CurrencyRate> getRates() {
    // return rates;
    // }

    // private String generateKey(Currency baseCurrency, Currency targetCurrency) {
    // return baseCurrency.getCode() + "_" + targetCurrency.getCode();
    // }
}
