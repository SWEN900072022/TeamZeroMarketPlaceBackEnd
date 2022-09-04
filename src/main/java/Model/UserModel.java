package Model;

import Entity.User;
import Mapper.Mapper;
import Mapper.UserMapper;
import UnitofWork.UserRepository;
import Util.JWTUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserModel {
    private final Mapper<User> uMapper;
    private UserRepository repo;

    public UserModel() {
        // Create a mapper for the model to write data to
        uMapper = new UserMapper();
        repo = new UserRepository();
    }

    public UserModel(Mapper<User> mapper) {
        this.uMapper = mapper;
        repo = new UserRepository();
    }

    public boolean register(User user) {
        // We are registering we want to make sure that it is not
        // a duplicate account
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("username", user.getUsername());
        queryMap.put("email", user.getEmail());

        Map<Integer, User> map = uMapper.find(queryMap, 1);

        if(map.isEmpty()) {
            repo.registerNew(user);
            return true;
        }
        return false;
    }

    public String login(User user) {
        // Validate to see whether the user is an actual user
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("email", user.getEmail());
        queryMap.put("password", user.getPassword());

        Map<Integer, User> map = uMapper.find(queryMap);

        if(map.isEmpty()) {
            // User does not exist
            return null;
        }

        user = (User)map.values().toArray()[0]; // Update the user object
        // User exists here
        // Generate jwt token for upcoming sessions
        return JWTUtil.generateToken(String.valueOf(user.getId()), queryMap);
    }
}
