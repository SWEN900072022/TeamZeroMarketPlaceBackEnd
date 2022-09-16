import Entity.GroupMembership;
import Entity.Listing;
import Entity.User;
import Enums.UserRoles;
import Injector.FindConditionInjector;
import Model.UserModel;
import UnitofWork.IUnitofWork;
import Util.JWTUtil;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class UserModelTest {
    /**
     * Initialise the repositories needed for the test
     */
    private UserModel uModel;
    private MockUserRepository uRepo;
    private MockGroupMembershipRepository gmRepo;
    public UserModelTest() {
        this.uRepo = new MockUserRepository();
        this.gmRepo = new MockGroupMembershipRepository();
        this.uModel = new UserModel(uRepo, gmRepo);
    }

    @Test
    /**
     * This is for the ideal case where all the fields of the user object is
     * not null
     */
    public void idealUserRegistration() {
        User user = new User();
        user.setUsername("abc");
        user.setEmail("abc");
        user.setPassword("abc");

        boolean isSuccessful = uModel.register(user); // Register the user
        assertTrue(isSuccessful);
    }

    @Test
    public void userIsEmptyRegistration() {
        User user = new User(); // The user is not set by the frontend

        boolean isSuccessful = uModel.register(user);
        assertFalse(isSuccessful);
    }

    @Test
    /**
     * This is for the ideal login situation where the user object is correctly set
     */
    public void idealUserLogin() {
        User user = new User();
        user.setEmail("a");
        user.setPassword("a");
        user.setUsername("a");
        user.setRoles(UserRoles.CUSTOMER.toString());
        user.setUserId(1);

        String jwt = uModel.login(user);
        assertNotNull(jwt);

        // This should be a user token
        assertNotNull(JWTUtil.getSubject(jwt));
        assertEquals(Integer.toString(user.getUserId()), JWTUtil.getSubject(jwt));
        assertNotNull(JWTUtil.getClaim("role", jwt));
        assertEquals(user.getRole(), JWTUtil.getClaim("role", jwt));
        assertNull(JWTUtil.getClaim("groupId", jwt));
    }

    @Test
    public void failedNullLogin() {
        User user = new User();

        this.uRepo.isNull = true;

        String jwt = uModel.login(user);
        assertEquals("", jwt);
    }

    @Test
    public void idealSellerLogin() {
        User user = new User();
        user.setEmail("a");
        user.setPassword("ab");
        user.setUsername("a");
        user.setRoles(UserRoles.SELLER.toString());
        user.setUserId(1);

        this.uRepo.isSeller = true;

        String jwt = uModel.login(user);
        assertNotNull(jwt);

        // This should be a seller token now
        assertNotNull(JWTUtil.getSubject(jwt));
        assertEquals(Integer.toString(user.getUserId()), JWTUtil.getSubject(jwt));
        assertNotNull(JWTUtil.getClaim("role", jwt));
        assertEquals(user.getRole(), JWTUtil.getClaim("role", jwt));
        assertNotNull(JWTUtil.getClaim("groupId", jwt));
    }

    class MockUserRepository implements IUnitofWork<User> {
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

    class MockGroupMembershipRepository implements IUnitofWork<GroupMembership> {
        public GroupMembership gm;
        public MockGroupMembershipRepository() {
            gm = new GroupMembership();
            gm.setGroupId(1);
            gm.setUserId(1);
        }

        @Override
        public GroupMembership read(FindConditionInjector injector, List<Object> param, String key) {
            return null;
        }

        @Override
        public List<GroupMembership> readMulti(FindConditionInjector injector, List<Object> param, String key) {
            return null;
        }

        @Override
        public GroupMembership read(FindConditionInjector injector, List<Object> param) {
            return gm;
        }

        @Override
        public List<GroupMembership> readMulti(FindConditionInjector injector, List<Object> param) {
            return null;
        }

        @Override
        public void registerNew(GroupMembership entity) {

        }

        @Override
        public void registerModified(GroupMembership entity) {

        }

        @Override
        public void registerDeleted(GroupMembership entity) {

        }

        @Override
        public void commit() {

        }
    }
}
