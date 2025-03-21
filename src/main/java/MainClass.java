import java.util.*;

import currency.Currency;
import user.User;

/**
 * Entry point of the currency converter app.
 * Manages the CLI and the displayable menus.
 */
public class MainClass {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[31m\u001B[32m";
    public static final String ANSI_RED_BOLD = "\u001B[31m\u001B[1m";

    private static AppManager appManager = new AppManager();

    public static void main(String[] args) {
        loadData();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Welcome! Please choose an option:");
            System.out.println("1. Log in");
            System.out.println("2. Sign up");
            System.out.println("0. Exit");
            System.out.print("\nEnter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    login(scanner);
                    break;
                case 2:
                    signUp(scanner);
                    break;
                case 0:
                    closeProgram();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
    }

    // Method for user login
    public static void login(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User user = appManager.getUserManager().authenticateUser(username, password);
        if (user != null) {
            System.out.println("Login successful! Welcome, " + user.getUsername() + "!");
            appManager.setCurrentUser(user);
            
            displayAvailableCurrenciesLogin();

            mainMenu();
        } else {
            System.out.println("Invalid username or password. Please try again.");
        }
    }

    private static void displayAvailableCurrenciesLogin() {
        System.out.println("\nAvailable Currencies:");
        System.out.println("----------------------");

        HashMap<String, Currency> currencies = appManager.getCurrencies();

        for (Currency currency : currencies.values()) {
            System.out.println("Currency: " + currency.getName() + " | Symbol: " + currency.getSymbol() +
                    "| Code: " + currency.getCode());
        }
        System.out.println("----------------------\n");
    }

    private static void displayAvailableCurrencies() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nAvailable Currencies:");
        System.out.println("----------------------");

        HashMap<String, Currency> currencies = appManager.getCurrencies();

        for (Currency currency : currencies.values()) {
            System.out.println("Currency: " + currency.getName() + " | Symbol: " + currency.getSymbol() +
                    "| Code: " + currency.getCode());
        }
        System.out.println("----------------------");

        System.out.println("\nPress enter to return to menu");
        scanner.nextLine();
    }

    public static void setAppManager(AppManager manager) {
        appManager = manager;
    }

    public static void signUp(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        if (appManager.getUserManager().userExists(username)) {
            System.out.println(
                    ANSI_RED_BOLD + "Username already exists. Please choose a different username." + ANSI_RESET);
            return;
        }

        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Are you an admin? (Y/N): ");
        String isAdminInput = scanner.nextLine();
        boolean isAdmin = isAdminInput.equalsIgnoreCase("Y");

        User newUser = new User(username, password, isAdmin);
        appManager.getUserManager().addUser(newUser);

        System.out.println(ANSI_GREEN + "Account created successfully! You can now log in." + ANSI_RESET);
    }

    private static void mainMenu() {
        int menuChoice;
        while (true) {
            menuChoice = displayMenu();

            switch (menuChoice) {
                case 1:
                    displayAvailableCurrencies();
                    break;
                case 2:
                    convertMoneyMenu();
                    break;
                case 3:
                    popularCurrenciesMenu();
                    break;
                case 4:
                    rateSummaryMenu();
                    break;
                case 5:
                    editRateMenu();
                    break;
                case 6:
                    addCurrencyMenu();
                    break;
                case 7:
                    setCommonCurrenciesMenu();
                    break;
                case 0:
                    closeProgram();
                    System.exit(0);
            }
        }
    }

    public static void closeProgram() {// ends the program
        appManager.closeConnections();
        System.exit(0);
    }

    /**
     * Displays the main menu to the user with the possible options.
     * Different options are displayed depending on whether the user is an admin or
     * not.
     *
     * @return the user's selected menu option as an integer.
     */
    public static int displayMenu() {
        System.out.println("Welcome, " + appManager.getCurrentUser().getUsername() + "!");
        System.out.println("Select one of the following options by typing in a number:\n");

        System.out.println("1. Display available currencies\n" +
                "2. Convert money\n" +
                "3. Show most popular currencies\n" +
                "4. Historical summary of 2 currencies");

        if (appManager.getCurrentUser().isAdmin()) {
            System.out.println("5. Edit an exchange rate (ADMIN)\n" +
                    "6. Add a currency (ADMIN)\n" +
                    "7. Set common currencies (ADMIN)");
        }

        System.out.println("\n0. Exit application");
        Scanner scanner = new Scanner(System.in);
        int userChoice;

        while (true) {
            System.out.print("\nEnter your choice: ");
            userChoice = scanner.nextInt();

            if (userChoice <= 4) {
                System.out.println();
                return userChoice;
            } else if (userChoice <= 7 && appManager.getCurrentUser().isAdmin()) {
                System.out.println();
                return userChoice;
            }
        }
    }

    public static void convertMoneyMenu() {
        Scanner scanner = new Scanner(System.in);

        HashMap<String, Float> rates = appManager.getRates();
        System.out.println("\nAvailable exchange rates:");
        for (Map.Entry<String, Float> entry : rates.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        System.out.println("Convert money");
        System.out.print("Enter starting currency code: ");
        String startingCurrency = scanner.nextLine().toUpperCase();

        System.out.print("Enter target currency code: ");
        String targetCurrency = scanner.nextLine().toUpperCase();

        System.out.print("Enter amount to be converted: ");
        double amount = scanner.nextDouble();

        System.out.print(ANSI_RED_BOLD + "Confirm you want to convert " + amount + " " + startingCurrency + " to "
                + targetCurrency + " [Y/N]: " + ANSI_RESET);
        String confirm = scanner.next();

        if (confirm.equalsIgnoreCase("N")) {
            System.out.println("Conversion cancelled.");
            return;
        }

        if (rates.containsKey(startingCurrency + "/" + targetCurrency)) {
            float exchangeRate = appManager.getExchangeRate(startingCurrency, targetCurrency);
            double convertedAmount = amount * exchangeRate;
            System.out.printf(ANSI_GREEN + "\n Success: %.2f %s = %.2f %s%n" + ANSI_RESET, amount, startingCurrency,
                    convertedAmount, targetCurrency);
        } else {
            System.out.println("Exchange rate not available for the selected currencies.");
        }
        scanner.nextLine();
        System.out.println("\nPress enter to return to menu");
        scanner.nextLine();
    }

    public static void addCurrencyMenu() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Add a new currency");
        System.out.print("Enter currency code: ");
        String code = scanner.nextLine();
        System.out.print("Enter currency name: ");
        String name = scanner.nextLine();
        System.out.print("Enter currency symbol: ");
        String symbol = scanner.nextLine();

        // exchange rate to each existing currency

        if (appManager.checkCurrency(name)) {
            System.out.println("\n" + name + " already exists.");
            System.out.println("Press enter to return to menu");
            scanner.nextLine();
            return;
        }

        ArrayList<Currency> currentCurrencies = new ArrayList<>(appManager.getCurrencies().values());

        for (Currency currency : currentCurrencies) {
            System.out.print("\nEnter exchange rate for " + code + "/" + currency.getCode() + " rate: ");
            float rate = scanner.nextFloat();

            appManager.addExchangeRate(code, currency.getCode(), rate);
            appManager.addExchangeRate(currency.getCode(), code, 1/rate);

            scanner.nextLine();
        }

        appManager.addCurrency(name, code, symbol);
        System.out.println("\n" + name + " has been successfully added!");
        System.out.println("Press enter to return to menu");
        scanner.nextLine();
    }

    public static void deleteCurrencyMenu() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Delete a currency");
        System.out.print("Enter currency code: ");
        String code = scanner.nextLine();
        // todo: check if currency exists

        System.out.print("Are you sure you want to delete this currency? [Y/N]: ");
        String confirm = scanner.nextLine();
        if (confirm.equalsIgnoreCase("Y")) {
            // todo: delete currency and all associated rates
            System.out.println("\n" + code + " has successfully been deleted.");
            System.out.println("Press enter to return to menu");
            scanner.nextLine();
        } else {
            System.out.println("\n" + code + " was not deleted!");
            System.out.println("Press enter to return to menu");
            scanner.nextLine();
        }
    }

    public static void rateSummaryMenu() {
        Scanner scanner = new Scanner(System.in);
    
        System.out.println("History of exchange rate");
        System.out.print("Enter base currency code: ");
        String baseCurrency = scanner.nextLine().toUpperCase();
        System.out.print("Enter target currency code: ");
        String targetCurrency = scanner.nextLine().toUpperCase();
        System.out.print("Enter starting date (YYYY-MM-DD): ");
        String startDate = scanner.nextLine();
        System.out.print("Enter ending date (YYYY-MM-DD): ");
        String endDate = scanner.nextLine();
    
        // Fetch historical rates from the database
        HashMap<String, Float> historicalRates = appManager.getCurrencyManager()
            .getHistoricalRates(baseCurrency, targetCurrency, startDate, endDate);
        Integer count = 0; //this just holds the number of exchange rates to use in calculation of:
        double total = 0.00;
        double mean = 0.00; // variable for mean of the exchange rate over time
        double standard_dev = 0.00;
        double median = 0.00;
        double minimum = 0.00;
        double maximum = 0.00;


        if (historicalRates.isEmpty()) {
            System.out.println("No historical data available for the given date range.");
        } else {
            System.out.println("\nHistory of exchange rate from " + baseCurrency + " to " + targetCurrency + ":");
            System.out.println("Date\t\tRate");
            List<Double> median_list = new ArrayList<Double>();
            for (Map.Entry<String, Float> entry : historicalRates.entrySet()) {
                System.out.printf("%s\t%.4f\n", entry.getKey(), entry.getValue()); //print all rates
                count += 1;
                total += entry.getValue();
                if (entry.getValue() < minimum) {
                    minimum = entry.getValue(); // sets current key's value as min
                }
                if (entry.getValue() > maximum) {
                    maximum = entry.getValue(); // sets current key's value as max
                }
                // adding to median_list
                double currentVal = entry.getValue();
                median_list.add(currentVal);
            }
            mean = total/count;

            //below is the median calculation
            median_list.sort(null);
            int median_num = count/2;
            median = median_list.get(median_num);

            // below is the standard deviation calculation
            double stddev_rawtotal = 0.00;
            for (Map.Entry<String, Float> entry : historicalRates.entrySet()) {
                stddev_rawtotal += Math.pow((entry.getValue() - mean), 2);
            }
            standard_dev = Math.sqrt(stddev_rawtotal/count);

            System.out.println("Average: " + mean); // print mean aka average
            System.out.println("Median: " + median); // print median
            System.out.println("Minimum: " + minimum); //print minimum
            System.out.println("Maximum: " + maximum); //print maximum
            System.out.println("Standard Deviation: " + standard_dev); // print standard deviation of the exchange rate

        }
    
        System.out.println("\nPress enter to return to the menu");
        scanner.nextLine();  // Wait for user input to go back to the menu
    }



    public static void popularCurrenciesMenu() {
        Scanner scanner = new Scanner(System.in);

        List<String> use = appManager.getCommonCurrencies();

        if (use.isEmpty() || use.size() < 4) {
            System.out.println("At least 4 popular currencies must first be set by an admin to display the table.");
            System.out.println("Press enter to return to menu");
            scanner.nextLine();
            return;
        }

        System.out.println("\nMost Popular Currency Pairs:");
        System.out.println("------------------------------");

        String C1 = use.get(0); // 1st most common currency
        String C2 = use.get(1); // 2nd
        String C3 = use.get(2); // 3rd
        String C4 = use.get(3); // 4th

        HashMap<Integer, String> symbols = new HashMap<>();
        symbols.put(0, " (-)");
        symbols.put(1, " (D)");
        symbols.put(2, " (I)");

        HashMap<String, Float> rates = appManager.getRates();
        HashMap<String, Integer> rateChanges = appManager.getRateChanges();
        // Cx_y where x is base currency and y is target currency
        String C1_1 = "    -   "; // blank
        float C1_2 = rates.get(C1 + "/" + C2);
        String C1_2f = String.format("%.2f", C1_2); // formatted, so 2 digits after decimal point
        String C1_2f_final = C1_2f + symbols.get(rateChanges.get(C1 + "/" + C2));
        float C1_3 = rates.get(C1 + "/" + C3);
        String C1_3f = String.format("%.2f", C1_3);
        String C1_3f_final = C1_3f + symbols.get(rateChanges.get(C1 + "/" + C3));
        float C1_4 = rates.get(C1 + "/" + C4);
        String C1_4f = String.format("%.2f", C1_4);
        String C1_4f_final = C1_4f + symbols.get(rateChanges.get(C1 + "/" + C4));

        float C2_1 = rates.get(C2 + "/" + C1);
        String C2_1f = String.format("%.2f", C2_1);
        String C2_1f_final = C2_1f + symbols.get(rateChanges.get(C2 + "/" + C1));
        String C2_2 = "    -   ";
        float C2_3 = rates.get(C2 + "/" + C3);
        String C2_3f = String.format("%.2f", C2_3);
        String C2_3f_final = C2_3f + symbols.get(rateChanges.get(C2 + "/" + C3));
        float C2_4 = rates.get(C2 + "/" + C4);
        String C2_4f = String.format("%.2f", C2_4);
        String C2_4f_final = C2_4f + symbols.get(rateChanges.get(C2 + "/" + C4));

        float C3_1 = rates.get(C3 + "/" + C1);
        String C3_1f = String.format("%.2f", C3_1);
        String C3_1f_final = C3_1f + symbols.get(rateChanges.get(C3 + "/" + C1));
        float C3_2 = rates.get(C3 + "/" + C2);
        String C3_2f = String.format("%.2f", C3_2);
        String C3_2f_final = C3_2f + symbols.get(rateChanges.get(C3 + "/" + C2));
        String C3_3 = "    -   ";
        float C3_4 = rates.get(C3 + "/" + C4);
        String C3_4f = String.format("%.2f", C3_4);
        String C3_4f_final = C3_4f + symbols.get(rateChanges.get(C3 + "/" + C4));

        float C4_1 = rates.get(C4 + "/" + C1);
        String C4_1f = String.format("%.2f", C4_1);
        String C4_1f_final = C4_1f + symbols.get(rateChanges.get(C4 + "/" + C1));
        float C4_2 = rates.get(C4 + "/" + C2);
        String C4_2f = String.format("%.2f", C4_2);
        String C4_2f_final = C4_2f + symbols.get(rateChanges.get(C4 + "/" + C2));
        float C4_3 = rates.get(C4 + "/" + C3);
        String C4_3f = String.format("%.2f", C4_3);
        String C4_3f_final = C4_3f + symbols.get(rateChanges.get(C4 + "/" + C3));
        String C4_4 = "    -   ";

        // print table
        System.out.println("-------------------------------------------------------");
        System.out.println(" From/To " + "   |   " + C1 + "    |   " + C2 + "    |   " + C3 + "    |   " + C4);
        System.out.println("-------------------------------------------------------");
        System.out.println("   " + C1 + "      | " + C1_1 + " | " + C1_2f_final + " | " + C1_3f_final + " | " + C1_4f_final);
        System.out.println("-------------------------------------------------------");
        System.out.println("   " + C2 + "      | " + C2_1f_final + " | " + C2_2 + " | " + C2_3f_final + " | " + C2_4f_final);
        System.out.println("-------------------------------------------------------");
        System.out.println("   " + C3 + "      | " + C3_1f_final + " | " + C3_2f_final + " | " + C3_3 + " | " + C3_4f_final);
        System.out.println("-------------------------------------------------------");
        System.out.println("   " + C4 + "      | " + C4_1f_final + " | " + C4_2f_final + " | " + C4_3f_final + " | " + C4_4);

        System.out.println("\nPress enter to return to menu");
        scanner.nextLine();
    }

    public static void editRateMenu() {
        Scanner scanner = new Scanner(System.in);
        HashMap<String, Float> rates = appManager.getRates();

        System.out.println("Edit exchange rate");
        System.out.print("Enter base currency code: ");
        String baseCurrency = scanner.nextLine();
        System.out.print("Enter target currency code: ");
        String targetCurrency = scanner.nextLine();

        String rate = String.format("%.2f", rates.get(baseCurrency + "/" + targetCurrency));
        System.out.println("\nCurrent " + baseCurrency + "/" + targetCurrency + " exchange rate: " + rate);
        System.out.print("Enter new exchange rate: ");
        float newRate = scanner.nextFloat();
        scanner.nextLine();

        appManager.editExchangeRate(baseCurrency, targetCurrency, newRate);
        appManager.editExchangeRate(targetCurrency, baseCurrency, 1/newRate);

        System.out.println("\nNew exchange rate has been set.");
        System.out.println("Press enter to return to menu");
        scanner.nextLine();
    }

    public static void setCommonCurrenciesMenu() { // set list of top 4 currencies

        Scanner scanner = new Scanner(System.in);

        System.out.println("Set common currencies");

        //admin enters ISO 4217 codes of the 4 common currencies of their choice
        System.out.print("Enter 1st common currency (ISO 4217 code only): ");
        String C1 = scanner.nextLine();
        System.out.print("Enter 2nd common currency (ISO 4217 code only): ");
        String C2 = scanner.nextLine();
        System.out.print("Enter 3rd common currency (ISO 4217 code only): ");
        String C3 = scanner.nextLine();
        System.out.print("Enter 4th common currency (ISO 4217 code only): ");
        String C4 = scanner.nextLine();

        List<String> topCurrencies = new ArrayList<>();
        topCurrencies.add(C1);
        topCurrencies.add(C2);
        topCurrencies.add(C3);
        topCurrencies.add(C4);
        appManager.setCommonCurrencies(topCurrencies);
    }
}
