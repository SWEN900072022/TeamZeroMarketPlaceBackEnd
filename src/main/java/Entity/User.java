package Entity;

import Enums.UserRoles;
import Injector.FindConditionInjector.FindIdInjector;
import Mapper.UserMapper;
import UnitofWork.Repository;

import java.util.ArrayList;
import java.util.List;

public class User extends EntityObject{
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
    public UserRoles getRoleEnum() {
        return this.role;
    }

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
            case "admin":
                this.role = UserRoles.ADMIN;
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
        if(username == null ||
                email == null ||
                password == null ||
                role == null
        ) {
            // Lazy load here.
            boolean canLoad = load();
            return !canLoad;
        }
        return false;
    }

    public boolean load() {
        // Check if the id is present, if not return false
        if(userId == 0) {
            return false;
        }

        Repository<User> userRepo = new Repository<User>(new UserMapper());
        List<Object> param = new ArrayList<>();
        param.add(userId);
        User user = userRepo.read(new FindIdInjector("users"), param);

        // Populate the object with the results
        username = user.getUsername();
        password = user.getPassword();
        email = user.getEmail();
        role = user.getRoleEnum();

        return true;
    }
}
