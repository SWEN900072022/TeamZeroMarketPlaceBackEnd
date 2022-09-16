package MockClasses;

import Entity.GroupMembership;
import Injector.FindConditionInjector;
import UnitofWork.IUnitofWork;

import java.util.List;

public class MockGroupMembershipRepository implements IUnitofWork<GroupMembership> {
    public GroupMembership gm;
    public MockGroupMembershipRepository() {
        gm = new GroupMembership();
        gm.setGroupId(1);
        gm.setUserId(1);
    }

    @Override
    public GroupMembership read(FindConditionInjector injector, List<Object> param, String key) {
        return null;
    }

    @Override
    public List<GroupMembership> readMulti(FindConditionInjector injector, List<Object> param, String key) {
        return null;
    }

    @Override
    public GroupMembership read(FindConditionInjector injector, List<Object> param) {
        return gm;
    }

    @Override
    public List<GroupMembership> readMulti(FindConditionInjector injector, List<Object> param) {
        return null;
    }

    @Override
    public void registerNew(GroupMembership entity) {

    }

    @Override
    public void registerModified(GroupMembership entity) {

    }

    @Override
    public void registerDeleted(GroupMembership entity) {

    }

    @Override
    public void commit() {

    }
}
