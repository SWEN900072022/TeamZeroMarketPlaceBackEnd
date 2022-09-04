package Model;

import Entity.User;
import Mapper.Mapper;
import Mapper.UserMapper;
import UnitofWork.UserRepository;

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

        List<User> list = uMapper.find(queryMap);

        if(list.isEmpty()) {
            repo.registerNew(user);
            return true;
        }
        return false;
    }

    public List<User> getAllUsers() {
        List<User> list = uMapper.getAll();
        return list;
    }
}
