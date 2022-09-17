package Model;

import Entity.GroupMembership;
import Entity.User;
import Enums.UserRoles;
import Injector.FindAllInjector;
import Injector.FindEmailAndPasswordInjector;
import Injector.FindIdInjector;
import Injector.FindUserOrEmailInjector;
import Mapper.GroupMembershipMapper;
import Mapper.UserMapper;
import UnitofWork.IUnitofWork;
import UnitofWork.Repository;
import Util.JWTUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserModel {
    private IUnitofWork<User> userRepo;
    private IUnitofWork<GroupMembership> gmRepo;

    public UserModel() {
        // Create a mapper for the model to write data to
        userRepo = new Repository<User>(new UserMapper());
        gmRepo = new Repository<GroupMembership>(new GroupMembershipMapper());
    }

    public UserModel(IUnitofWork<User> userRepo) {
        this.userRepo = userRepo;
        gmRepo = new Repository<GroupMembership>(new GroupMembershipMapper());
    }

    public UserModel(IUnitofWork<User> userRepo, IUnitofWork<GroupMembership> gmRepo) {
        this.userRepo = userRepo;
        this.gmRepo = gmRepo;
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

        User user1 = userRepo.read(new FindUserOrEmailInjector(), param);

        if(!user1.isEmpty()) {
            userRepo.registerNew(user);
            userRepo.commit();
            return true;
        }
        return false;
    }


    public List<User> getAllUsers() {
        List<Object> param = new ArrayList<>();
        List<User> userList = userRepo.readMulti(new FindAllInjector("users"), param);
        return userList;
    }

    public String login(User user) {
        // Validate to see whether the user is an actual user
        List<Object> param = new ArrayList<>();
        param.add(user.getEmail());
        param.add(user.getPassword());

        User user1 = userRepo.read(new FindEmailAndPasswordInjector(), param);

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
        if(user1.getRole() == UserRoles.SELLER.toString()) {
            param = new ArrayList<>();
            param.add(user.getUserId());
            GroupMembership gm =gmRepo.read(new FindIdInjector("groupmembership"), param);
            claims.put("groupId", Integer.toString(gm.getGroupId()));
        }

        // User exists here
        // Generate jwt token for upcoming sessions
        return JWTUtil.generateToken(String.valueOf(user1.getUserId()), claims);
    }
}
