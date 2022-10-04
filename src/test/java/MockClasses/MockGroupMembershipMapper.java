package MockClasses;

import Entity.GroupMembership;
import Entity.SellerGroup;
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
        GroupMembership gm1 = new GroupMembership();
        gm1.setGroupId(1);
        gm1.setUserId(1);

        GroupMembership gm2 = new GroupMembership();
        gm2.setGroupId(2);
        gm2.setUserId(2);

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
        return new GroupMembership();
    }

    @Override
    public List<GroupMembership> findMulti(ISQLInjector injector, List<Object> queryParam) {
        return gmList;
    }

    @Override
    public void setConnection(Connection conn) {

    }
}
