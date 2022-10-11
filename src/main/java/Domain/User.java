package Domain;

import Enums.UserRoles;

public class User {
    private String email;
    private String username;
    private String password;
    private int userId;

    public User(String email, String username, String password, int userId) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.userId = userId;
    }

    public static User create(String email, String username, String password, int userId, UserRoles roles) {
        if(roles.equals(UserRoles.CUSTOMER.toString())) {
            // Return customer user
        }

        if(roles.equals(UserRoles.SELLER.toString())) {
            // Return seller user
        }

        if(roles.equals(UserRoles.ADMIN.toString())) {
            // Return admin user
        }
        throw new IllegalArgumentException();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
