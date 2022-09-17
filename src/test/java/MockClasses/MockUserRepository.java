package MockClasses;

import Entity.User;
import Enums.UserRoles;
import Injector.FindConditionInjector;
import UnitofWork.IUnitofWork;

import java.util.List;
import java.util.Objects;

public class MockUserRepository implements IUnitofWork<User> {
    private User user;
    public boolean isSeller = false;
    public boolean isNull = false;

    public MockUserRepository() {
        this.user = new User();
        user.setEmail("a");
        user.setPassword("a");
        user.setUsername("a");
        user.setRoles(UserRoles.CUSTOMER.toString());
        user.setUserId(1);
    }

    @Override
    public User read(FindConditionInjector injector, List<Object> param, String key) {
        return null;
    }

    @Override
    public List<User> readMulti(FindConditionInjector injector, List<Object> param, String key) {
        return null;
    }

    @Override
    public User read(FindConditionInjector injector, List<Object> param) {
//            if(!checkInjectorString(injector)) {
//                // Incorrect SQL query
//                return null;
//            }

        if(isSeller) {
            this.user.setRole(UserRoles.SELLER);
        }

        if(isNull) {
            return null;
        }

        return this.user;
    }

    @Override
    public List<User> readMulti(FindConditionInjector injector, List<Object> param) {
        return null;
    }

    @Override
    public void registerNew(User entity) {

    }

    @Override
    public void registerModified(User entity) {

    }

    @Override
    public void registerDeleted(User entity) {

    }

    @Override
    public void commit() {

    }

    private boolean checkInjectorString(FindConditionInjector injector) {
        if(Objects.equals(injector.toString(), "SELECT * FROM users where email=? and password=?;")) {
            // This is should be the string for finding duplicate emails
            return true;
        }

        if(Objects.equals(injector.toString(), "SELECT * FROM groupmembership where userid=?;")) {
            return true;
        }
        return false;
    }
}
