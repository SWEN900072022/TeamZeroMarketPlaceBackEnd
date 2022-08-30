import Entity.User;
import Mapper.Mapper;
import Model.UserModel;
import Util.JWTUtil;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static Util.JWTUtil.validateToken;
import static org.junit.jupiter.api.Assertions.*;

public class UserModelTest {
    private mockUserMapper mapper;
    private UserModel userModel;

    public UserModelTest() {
        mapper = new mockUserMapper();
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
        String jwt = userModel.login(new User());
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

    class mockUserMapper extends Mapper<User> {
        private boolean isEmptyResult = false;
        public List<User> result = new ArrayList<>();

        public mockUserMapper() {

        }

        public void setEmptyResult(boolean emptyResult) {
            this.isEmptyResult = emptyResult;
            if(emptyResult) {
                result = new ArrayList<>();
            } else {
                result.add(new User());
            }
        }

        public boolean insert(List<User> userList) {
            return this.isEmptyResult;
        }

        public boolean delete(List<User> userList) {
            return false;
        }

        public boolean modify(List<User> userList) {
            return false;
        }

        public List<User> find(Map<String, String> map) {
            return find(map, 0);
        }

        public List<User> find(Map<String, String> map, int mode) {
            return this.result;
        }
    }
}
