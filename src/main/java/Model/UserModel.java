package Model;

import Entity.User;
import Injector.FindAllInjector;
import Injector.FindEmailAndPasswordInjector;
import Injector.FindIdInjector;
import Injector.FindUserOrEmailInjector;
import Mapper.Mapper;
import Mapper.UserMapper;
import UnitofWork.UserRepository;
import Util.JWTUtil;

import java.util.ArrayList;
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
        repo = new UserRepository(mapper);
    }

    public boolean register(User user) {
        // We are registering we want to make sure that it is not
        // a duplicate account
        List<Object> param = new ArrayList<>();
        param.add(user.getUsername());
        param.add(user.getEmail());

        User user1 = uMapper.find(new FindUserOrEmailInjector(), param);

        if(user1.isEmpty()) {
            repo.registerNew(user);
            repo.commit();
            return true;
        }
        return false;
    }


    public List<User> getAllUsers() {
        List<User> list = uMapper.findAllItems(new FindAllInjector("users"));
        return list;
    }

    public String login(User user) {
        // Validate to see whether the user is an actual user

        List<Object> param = new ArrayList<>();
        param.add(user.getEmail());
        param.add(user.getPassword());

        User user1 = uMapper.find(new FindEmailAndPasswordInjector(), param);

        if(user1.isEmpty()) {
            // User does not exist
            return null;
        }

        // User exists here
        // Generate jwt token for upcoming sessions
        return JWTUtil.generateToken(String.valueOf(user1.getId()), new HashMap<>());

    }
}
