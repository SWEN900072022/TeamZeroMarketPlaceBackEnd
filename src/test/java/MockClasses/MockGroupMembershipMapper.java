package MockClasses;

import Domain.GroupMembership;
import Injector.FindConditionInjector.FindIdInjector;
import Injector.ISQLInjector;
import Mapper.Mapper;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class MockGroupMembershipMapper implements Mapper<GroupMembership> {
    // In-memory database
    public List<GroupMembership> gmList = new ArrayList<>();

    public MockGroupMembershipMapper() {
        // Populate the gmList with some dummy data
        GroupMembership gm1 = GroupMembership.create(1, 1);

        GroupMembership gm2 = GroupMembership.create(2, 2);

        gmList.add(gm1);
        gmList.add(gm2);
    }

    @Override
    public boolean insert(GroupMembership TEntity) {
        gmList.add(TEntity);
        return true;
    }

    @Override
    public boolean delete(GroupMembership TEntity) {
        gmList.removeIf(t -> (
                t.getGroupId() == TEntity.getGroupId() &&
                        t.getUserId() == TEntity.getUserId()
                ));
        return true;
    }

    @Override
    public boolean modify(GroupMembership TEntity) {
        delete(TEntity);
        insert(TEntity);
        return true;
    }

    @Override
    public GroupMembership find(ISQLInjector injector, List<Object> queryParam) {
        for(GroupMembership gm : gmList) {
            if(injector instanceof FindIdInjector) {
                if((Integer)queryParam.get(0) == gm.getUserId()) {
                    return gm;
                }
            }
        }
        return null;
    }

    @Override
    public List<GroupMembership> findMulti(ISQLInjector injector, List<Object> queryParam) {
        return gmList;
    }

    @Override
    public void setConnection(Connection conn) {

    }
}
