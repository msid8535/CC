package user;

public class AdminUser extends User {

    public AdminUser(String username, String password) {
        super(username, password, true);
    }

}
