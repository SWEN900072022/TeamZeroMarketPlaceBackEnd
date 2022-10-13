package Domain;

import Enums.UserRoles;
import Injector.FindConditionInjector.FindAllInjector;
import Injector.FindConditionInjector.FindEmailAndPasswordInjector;
import UnitofWork.IUnitofWork;
import Util.GeneralUtil;
import Util.JWTUtil;

import java.util.*;

public abstract class User extends EntityObject{
    private String email;
    private String username;
    private String password;
    private int userId;
    private IUnitofWork repo;

    public User(String email, String username, String password, int userId) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.userId = userId;
    }

    public static User create(String email, String username, String password, int userId, String roles) {
        if(roles.equals(UserRoles.CUSTOMER.toString())) {
            return new Customer(email, username, password, userId);
        }

        if(roles.equals(UserRoles.SELLER.toString())) {
            return new Seller(email, username, password, userId);
        }

        if(roles.equals(UserRoles.ADMIN.toString())) {
            return new Admin(email, username, password, userId);
        }
        throw new IllegalArgumentException();
    }

    public static List<User> getAllUser(IUnitofWork repo) {
        List<Object> param = new ArrayList<>();

        // Get order details
        List<User> ordList = GeneralUtil.castObjectInList(repo.readMulti(
                new FindAllInjector("users"),
                param,
                User.class), User.class);

        return ordList;
    }

    public void setRepository(IUnitofWork repo) {
        this.repo = repo;
    }

    public static User getUserByUsernamePassword(String email, String password, IUnitofWork repo) {
        List<Object> param = new ArrayList<>();
        param.add(email);
        param.add(password);

        return (User) repo.read(
                new FindEmailAndPasswordInjector(),
                param,
                User.class
        );
    }

    public String login() {
        // Check to see if the user is present
        User user = getUserByUsernamePassword(email, password, repo);

        if(user != null) {
            // user exists with the correct username and password
            // populate the claim map
            Map<String, String> claims = new HashMap<>();
            claims.put("role", user.getRole());

            if(Objects.equals(user.getRole(), UserRoles.SELLER.toString())) {
                GroupMembership gm = GroupMembership.getGroupMembershipByUserId(user.userId, repo);
                if(gm != null) {
                    claims.put("groupId", Integer.toString(gm.getGroupId()));
                }
            }

            return JWTUtil.generateToken(String.valueOf(user.userId), claims);
        }
        return "";
    }

    public static User register(String email, String username, String password, String roles, IUnitofWork repo) {
        // Check to see if the user is already present
        User user = getUserByUsernamePassword(email, password, repo);

        if(user == null) {
            // user does not exist
            return create(email, username, password,0, roles);
        }
        return null;
    }

    public abstract String getRole();

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
