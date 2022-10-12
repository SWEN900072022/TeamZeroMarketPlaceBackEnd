package Domain;

import Injector.FindConditionInjector.FindIdInjector;
import UnitofWork.IUnitofWork;

import java.util.ArrayList;
import java.util.List;

public class GroupMembership extends EntityObject{
    private int groupId;
    private int userId;

    protected GroupMembership(int groupId, int userId) {
        this.groupId = groupId;
        this.userId = userId;
    }

    public static GroupMembership create(int groupId, int userId) {
        return new GroupMembership(groupId, userId);
    }

    public static GroupMembership getGroupMembershipByUserId(int userId, IUnitofWork repo) {
        List<Object> param = new ArrayList<>();
        param.add(userId);

        return (GroupMembership) repo.read(
                new FindIdInjector("groupmembership"),
                param,
                GroupMembership.class
        );
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
