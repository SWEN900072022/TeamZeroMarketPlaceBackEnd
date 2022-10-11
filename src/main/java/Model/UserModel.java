package Model;

import Entity.GroupMembership;
import Entity.User;
import Enums.UserRoles;
import Injector.FindConditionInjector.FindIdInjector;
import Injector.FindConditionInjector.FindAllInjector;
import Injector.FindConditionInjector.FindEmailAndPasswordInjector;
import Injector.FindConditionInjector.FindUserOrEmailInjector;
import UnitofWork.IUnitofWork;
import UnitofWork.UnitofWork;
import Util.GeneralUtil;
import Util.JWTUtil;

import java.util.*;

public class UserModel {
    private IUnitofWork repo;

    public UserModel() {
        // Create a mapper for the model to write data to
        repo = new UnitofWork();
    }

    public UserModel(IUnitofWork repo) {
        this.repo = repo;
    }

    public boolean register(User user) {
        // We need to ensure that the user object is not null, if it is return null
        if(user.isEmpty()) {
            return false;
        }

        // We are registering we want to make sure that it is not
        // a duplicate account
        List<Object> param = new ArrayList<>();
        param.add(user.getUsername());
        param.add(user.getEmail());

//        User user1 = userRepo.read(new FindUserOrEmailInjector(), param);
        User user1 = (User) repo.read(
                new FindUserOrEmailInjector(),
                param,
                User.class
        );

        if(user1.isEmpty()) {
            repo.registerNew(user);
            return true;
        }
        return false;
    }


    public List<User> getAllUsers() {
        List<Object> param = new ArrayList<>();
//        List<User> userList = userRepo.readMulti(new FindAllInjector("users"), param);
        List<User> userList = GeneralUtil.castObjectInList(
                repo.readMulti(
                        new FindAllInjector("users"),
                        param,
                        User.class
                ),
                User.class
        );
        return userList;
    }

    public String login(User user) {
        // Validate to see whether the user is an actual user
        List<Object> param = new ArrayList<>();
        param.add(user.getEmail());
        param.add(user.getPassword());

//        User user1 = userRepo.read(new FindEmailAndPasswordInjector(), param);
        User user1 = (User) repo.read(
                new FindEmailAndPasswordInjector(),
                param,
                User.class
        );

        if(user1 == null) {
            return "";
        }

        if(user1.isEmpty()) {
            // User does not exist
            return "";
        }

        Map<String, String> claims = new HashMap<>();
        claims.put("role", user1.getRole());

        // If the user is a seller, we include the seller group in the tokem
        if(Objects.equals(user1.getRole(), UserRoles.SELLER.toString())) {
            param = new ArrayList<>();
            param.add(user1.getUserId());
//            GroupMembership gm = gmRepo.read(new FindIdInjector("groupmembership"), param);
            GroupMembership gm = (GroupMembership) repo.read(
                    new FindIdInjector("groupmembership"),
                    param,
                    GroupMembership.class
            );
            claims.put("groupId", Integer.toString(gm.getGroupId()));
        }

        // User exists here
        // Generate jwt token for upcoming sessions
        return JWTUtil.generateToken(String.valueOf(user1.getUserId()), claims);
    }
}
