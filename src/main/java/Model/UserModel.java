package Model;

import Entity.User;
import Mapper.Mapper;
import Mapper.UserMapper;

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
        Map<String, String> queryMap = new HashMap<String, String>();
        queryMap.put("username", user.getUsername());
        queryMap.put("email", user.getEmail());

        List<User> list = uMapper.find(queryMap);

        if(list.isEmpty()) {
            return uMapper.insert(user);
        }
        return false;
    }
}
