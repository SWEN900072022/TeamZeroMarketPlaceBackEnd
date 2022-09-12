package Entity;

import Enums.UserRoles;

public class User{
    private String email;
    private String username;
    private String password;
    private int userId;
    private UserRoles role;

    public User() {
        this.email = null;
        this.username = null;
        this.password = null;
        this.userId = 0;
        this.role = UserRoles.CUSTOMER;
    }
    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.userId = 0;
        this.role = UserRoles.CUSTOMER;
    }

    public User(String email, String username, String password, String roles) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.userId = 0;
        setRoles(roles);
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getEmail() {
        return this.email;
    }

    public String getRole() { return this.role.toString(); }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(UserRoles role) {
        this.role = role;
    }

    public void setRoles(String roles) {
        switch(roles) {
            case "seller":
                this.role = UserRoles.SELLER;
                break;
            case "customer":
            default:
                this.role = UserRoles.CUSTOMER;
        }
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String toString() {
        return email + " " + username + " " + password;
    }

    public boolean isEmpty() {
        if(username == null && email == null && password == null) {
            return true;
        }
        return false;
    }
}
