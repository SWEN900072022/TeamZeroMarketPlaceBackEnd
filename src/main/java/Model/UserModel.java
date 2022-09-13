package Model;

import Entity.User;
import Injector.FindAllInjector;
import Injector.FindEmailAndPasswordInjector;
import Injector.FindIdInjector;
import Injector.FindUserOrEmailInjector;
import Mapper.Mapper;
import Mapper.UserMapper;
import UnitofWork.IUnitofWork;
import UnitofWork.Repository;
import Util.JWTUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserModel {
    private IUnitofWork<User> repo;

    public UserModel() {
        // Create a mapper for the model to write data to
        repo = new Repository<User>(new UserMapper());
    }

    public UserModel(Mapper<User> mapper) {
        repo = new Repository<User>(mapper);
    }

    public boolean register(User user) {
        // We are registering we want to make sure that it is not
        // a duplicate account
        List<Object> param = new ArrayList<>();
        param.add(user.getUsername());
        param.add(user.getEmail());

        User user1 = repo.read(new FindUserOrEmailInjector(), param);

        if(user1.isEmpty()) {
            repo.registerNew(user);
            repo.commit();
            return true;
        }
        return false;
    }


    public List<User> getAllUsers() {
        List<Object> param = new ArrayList<>();
        List<User> userList = repo.readMulti(new FindAllInjector("users"), param);
        return userList;
    }

    public String login(User user) {
        // Validate to see whether the user is an actual user

        List<Object> param = new ArrayList<>();
        param.add(user.getEmail());
        param.add(user.getPassword());

        User user1 = repo.read(new FindEmailAndPasswordInjector(), param);

        if(user1.isEmpty()) {
            // User does not exist
            return null;
        }

        // User exists here
        // Generate jwt token for upcoming sessions
        return JWTUtil.generateToken(String.valueOf(user1.getUserId()), new HashMap<>());

    }
}
