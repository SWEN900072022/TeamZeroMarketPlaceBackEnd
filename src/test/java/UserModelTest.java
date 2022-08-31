import Entity.User;
import Mapper.Mapper;
import Model.UserModel;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        mapper.setCanInsert(true);
        boolean isSuccessful = userModel.register(new User());
        assertTrue(isSuccessful);
    }

    @Test
    public void failedRegistration() {
        // Exhibits the behaviour when there are duplicate names
        mapper.setCanInsert(false);
        boolean isSuccessful = userModel.register(new User());
        assertFalse(isSuccessful);
    }

    class mockUserMapper extends Mapper<User> {
        private boolean canInsert = false;
        public List<User> result = new ArrayList<>();

        public mockUserMapper() {

        }

        public void setCanInsert(boolean canInsert) {
            this.canInsert = canInsert;
            if(canInsert) {
                result = new ArrayList<>();
            } else {
                result.add(new User());
            }
        }

        public boolean insert(List<User> userList) {
            return this.canInsert;
        }

        public boolean delete(List<User> userList) {
            return false;
        }

        public boolean modify(List<User> userList) {
            return false;
        }

        public List<User> find(Map<String, String> map) {
            return this.result;
        }
    }
}
