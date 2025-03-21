package user;

import java.sql.*;
import java.util.ArrayList;

public class UserManager {

    private Connection connection;

    public UserManager() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:currency.db");
            createTables();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Creates User Table If doesnt exist in Db already (it does lol)
    private void createTables() throws SQLException {
        String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                "username TEXT PRIMARY KEY, " +
                "password TEXT NOT NULL, " +
                "isAdmin INTEGER NOT NULL);"; // isAdmin: 1 = admin, 0 = normal user

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createUsersTable);
        }
    }

    
    public void addUser(User user) {
        String sql = "INSERT OR IGNORE INTO users(username, password, isAdmin) VALUES(?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setInt(3, user.isAdmin() ? 1 : 0);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean userExists(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();  
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
    

    public User authenticateUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                boolean isAdmin = rs.getInt("isAdmin") == 1;
                return isAdmin ? new AdminUser(username, password) : new NormalUser(username, password);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null; // User not found or invalid credentials
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
}
