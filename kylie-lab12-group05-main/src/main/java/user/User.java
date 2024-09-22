package user;

public class User {
    private String username;
    private String password;
    private boolean admin;

    // Constructor to initialize a user, admin flag determines if it's an admin user
    public User(String username, String password, boolean admin) {
        this.username = username;
        this.password = password;
        this.admin = admin;
    }

    // Get the username
    public String getUsername() {
        return username;
    }

    // Check if the user is an admin
    public boolean isAdmin() {
        return admin;
    }

    // Check if the user is a normal user (non-admin)
    public boolean isNormalUser() {
        return !admin;
    }

    public String getPassword() {
        return password;
    }
}

