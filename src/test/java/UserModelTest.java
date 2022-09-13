import Entity.User;
import Injector.FindConditionInjector;
import Mapper.Mapper;
import Model.UserModel;
import Util.JWTUtil;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserModelTest {
    private MockUserMapper mapper;
    private UserModel userModel;

    public UserModelTest() {
        mapper = new MockUserMapper();
        userModel = new UserModel(mapper);
    }

    @Test
    public void successfulRegistration() {
        // Set uMapper to return false (i.e. no duplicate names)
        mapper.setEmptyResult(true);
        boolean isSuccessful = userModel.register(new User());
        assertTrue(isSuccessful);
    }

    @Test
    public void failedRegistration() {
        // Exhibits the behaviour when there are duplicate names
        mapper.setEmptyResult(false);
        boolean isSuccessful = userModel.register(new User());
        assertFalse(isSuccessful);
    }

    @Test
    public void successfulLogin() {
        mapper.setEmptyResult(false);
        User user = new User();
        user.setEmail("a");
        String jwt = userModel.login(user);
        assertNotNull(jwt);

        // Make sure that the jwt token is valid
        try {
            boolean isValid = JWTUtil.validateToken(jwt);
            assertTrue(isValid);
        } catch(Exception ex) {

        }
    }

    @Test
    public void failedLogin() {
        mapper.setEmptyResult(true);
        String jwt = userModel.login(new User());
        assertNull(jwt);
    }

    class MockUserMapper implements Mapper<User> {
        private boolean isEmptyResult = false;
        public User result = new User();
        List <User> allUsers= new ArrayList<User>();

        public void setEmptyResult(boolean emptyResult) {
            this.isEmptyResult = emptyResult;
            if(emptyResult) {
                result = new User();
            } else {
                result.setEmail("a");
                result.setPassword("a");
                result.setUsername("a");
            }
        }

        @Override
        public boolean insert(User TEntity) {
            return this.isEmptyResult;
        }

        @Override
        public boolean delete(User TEntity) {
            return false;
        }

        @Override
        public boolean modify(User TEntity) {
            return false;
        }

        @Override
        public User find(FindConditionInjector injector, List<Object> queryParam) {
            return this.result;
        }

        @Override
        public List<User> findMulti(FindConditionInjector injector, List<Object> queryParam) {
            return null;
        }
    }
}
