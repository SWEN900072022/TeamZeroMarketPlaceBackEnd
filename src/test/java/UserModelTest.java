import Entity.User;
import Enums.UserRoles;
import MockClasses.MockGroupMembershipRepository;
import MockClasses.MockUserRepository;
import Model.UserModel;
import Util.JWTUtil;
import org.junit.jupiter.api.Test;

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
}
