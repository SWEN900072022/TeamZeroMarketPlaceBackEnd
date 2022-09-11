package Entity;

import Enums.UserRoles;

import java.io.Serializable;

public class User{
    private String email;
    private String username;
    private String password;
    private int id;
    private UserRoles roles;

    public User() {
        this.email = null;
        this.username = null;
        this.password = null;
        this.id = 0;
        this.roles = UserRoles.CUSTOMER;
    }
    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.id = 0;
        this.roles = UserRoles.CUSTOMER;
    }

    public User(String email, String username, String password, String roles) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.id = 0;
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

    public String getRoles() { return this.roles.toString(); }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRoles(UserRoles roles) {
        this.roles = roles;
    }

    public void setRoles(String roles) {
        switch(roles) {
            case "seller":
                this.roles = UserRoles.SELLER;
                break;
            case "customer":
            default:
                this.roles = UserRoles.CUSTOMER;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String toString() {
        return email + " " + username + " " + password;
    }
}
