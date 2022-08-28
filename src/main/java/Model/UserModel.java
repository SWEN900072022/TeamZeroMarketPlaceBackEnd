package Model;

import Entity.User;
import Mapper.Mapper;
import Mapper.UserMapper;
import Util.JWTUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserModel {
    private Mapper<User> uMapper;

    public UserModel() {
        // Create a mapper for the model to write data to
        uMapper = new UserMapper();
    }

    public UserModel(Mapper<User> mapper) {
        this.uMapper = mapper;
    }

    public boolean register(User user) {
        // We are registering we want to make sure that it is not
        // a duplicate account
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("username", user.getUsername());
        queryMap.put("email", user.getEmail());

        List<User> list = uMapper.find(queryMap, 1);

        if(list.isEmpty()) {
            return uMapper.insert(user);
        }
        return false;
    }

    public String login(User user) {
        // Validate to see whether the user is an actual user
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("email", user.getEmail());
        queryMap.put("password", user.getPassword());

        List<User> list = uMapper.find(queryMap);

        if(list.isEmpty()) {
            // User does not exist
            return null;
        }

        // User exists here
        // Generate jwt token for upcoming sessions
        return JWTUtil.generateToken(user.getEmail(), queryMap);
    }
}
