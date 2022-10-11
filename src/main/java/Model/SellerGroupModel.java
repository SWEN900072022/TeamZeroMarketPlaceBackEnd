package Model;

import Entity.GroupMembership;
import Entity.SellerGroup;
import Entity.User;
import Enums.UserRoles;
import Injector.DeleteConditionInjector.DeleteGroupMemberByUserIdInjector;
import Injector.FindConditionInjector.FindAllInjector;
import Injector.FindConditionInjector.FindGroupIdByNameInjector;
import Injector.FindConditionInjector.FindIdInjector;
import UnitofWork.IUnitofWork;
import UnitofWork.UnitofWork;
import Util.GeneralUtil;
import Util.JWTUtil;

import java.util.ArrayList;
import java.util.List;

public class SellerGroupModel {
    private IUnitofWork repo;

    public SellerGroupModel() {
        repo = new UnitofWork();
    }

    public SellerGroupModel(IUnitofWork repo) {
        this.repo = repo;
    }

    public boolean createSellerGroup(SellerGroup sg, String jwt) {
        // Get the role from the jwt
        String role;

        try{
            if(!JWTUtil.validateToken(jwt)) {
                // if not valid, return false
                return false;
            }
            role = JWTUtil.getClaim("role", jwt);
        } catch (Exception e) {
            // Something went wrong
            return false;
        }

        // Check to see if the role is empty or null or not admin
        if(role == null || role.equals("") || !role.equals(UserRoles.ADMIN.toString())) {
            return false;
        }

        // Might be worthwhile to do a check to see if the name is taken
        List<Object> param = new ArrayList<>();
        param.add(sg.getGroupName());

        SellerGroup sgTemp = (SellerGroup) repo.read(
                new FindGroupIdByNameInjector(),
                param,
                SellerGroup.class);

        // At this point, we should be an admin, write to db
        if(sgTemp.isEmpty()) {
            repo.registerNew(sg);
            return true;
        }
        return false;
    }

    public boolean addSellerToSellerGroup(User user, String groupName) {
        // Check to see if the object is valid
        if(user == null || user.isEmpty()) {
            return false;
        }

        // Check to see if the user exists
        List<Object> param = new ArrayList<>();
        param.add(user.getUserId());
        User userDB = (User) repo.read(
                new FindIdInjector("users"),
                param,
                User.class);

        if(userDB == null || userDB.isEmpty() || userDB.getRoleEnum() != UserRoles.SELLER) {
            // Null user, non-existing user and non-seller user
            return false;
        }
        // At this point, we know the user exists and is a seller

        // Check to see if the seller group exists
        param = new ArrayList<>();
        param.add(groupName);
        SellerGroup sg = (SellerGroup) repo.read(
                new FindGroupIdByNameInjector(),
                param,
                SellerGroup.class);

        if(sg == null || sg.isEmpty()) {
            // Non-existent seller group and null sg
            return false;
        }
        // Otherwise, seller group is valid

        // Check to see if the user is in a preexisting group
        // We have group id and user id, we can find it there is an extry in group membership or not
        param = new ArrayList<>();
        param.add(userDB.getUserId());
        GroupMembership gm = (GroupMembership) repo.read(
                new FindIdInjector("groupmembership"),
                param,
                GroupMembership.class);

        if(!gm.isEmpty()) {
            // User is in a pre existing group, cannot be added in
            return false;
        }
        // Now, we have a user that exists and is not in any groups, we can register the changes now
        gm = new GroupMembership();
        gm.setGroupId(sg.getGroupId());
        gm.setUserId(userDB.getUserId());

        repo.registerNew(gm);

        return true;
    }

    public boolean removeSellerFromSellerGroup(User user, String groupName) {
        // To remove we need to make sure that the userid is in the group membership table
        // If yes, we can just delete from the db, postgres will handle the foreign keys
        if(user == null || user.isEmpty()) {
            return false;
        }

        // Check to see if the user exists
        List<Object> param = new ArrayList<>();
        param.add(user.getUserId());
        User userDB = (User) repo.read(
                new FindIdInjector("users"),
                param,
                User.class);

        if(userDB == null || userDB.isEmpty() || userDB.getRoleEnum() != UserRoles.SELLER) {
            // Null user, non-existing user and non-seller user
            return false;
        }

        // Check to see if the user is currently in the seller group
        param = new ArrayList<>();
        param.add(userDB.getUserId());
        GroupMembership gm = (GroupMembership) repo.read(
                new FindIdInjector("groupmembership"),
                param,
                GroupMembership.class);

        if(gm == null || gm.isEmpty()) {
            // User is in a pre existing group, cannot be added in
            return false;
        }

        // Existing user in a seller group, register the deletion
        param = new ArrayList<>();
        param.add(gm.getUserId());

        gm.setInjector(new DeleteGroupMemberByUserIdInjector());
        gm.setParam(param);
        repo.registerDeleted(gm);

        return true;
    }

    public List<SellerGroup> getAllSellerGroup() {
        List<Object> param = new ArrayList<>();
        List<SellerGroup> sellerGroupList = GeneralUtil.castObjectInList(
                repo.readMulti(
                        new FindAllInjector("sellergroups"),
                        param,
                        SellerGroup.class),
                SellerGroup.class
        );
        return sellerGroupList;
    }
}
