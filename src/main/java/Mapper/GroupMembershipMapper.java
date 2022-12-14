package Mapper;

import Domain.GroupMembership;
import Injector.ISQLInjector;
import Util.SQLUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GroupMembershipMapper extends GeneralMapper<GroupMembership> {
    @Override
    public boolean insert(GroupMembership TEntity) {
        try {
            insertOperation(TEntity, false);
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    private void insertOperation(GroupMembership gm, boolean shouldReturn) throws SQLException {
        PreparedStatement statement;


        statement = conn.prepareStatement(
                "INSERT INTO groupmembership(groupId, userId) " +
                        "VALUES (?, ?);",
                Statement.RETURN_GENERATED_KEYS
        );
        statement.setInt(1, gm.getGroupId());
        statement.setInt(2, gm.getUserId());
        statement.execute();
    }

    @Override
    public boolean modify(GroupMembership TEntity) {
        try {

            PreparedStatement statement = conn.prepareStatement(
                    "UPDATE groupmembership as gm set " +
                            "groupId=gm2.groupId, " +
                            "userId=gm2.userId " +
                            "from (values " +
                            "(?, ?)" +
                            ") as gm2(groupId, userId) " +
                            "where gm.userId=gm2.userId;"
            );
            statement.setInt(1, TEntity.getGroupId());
            statement.setInt(2, TEntity.getUserId());
            statement.execute();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    @Override
    public GroupMembership find(ISQLInjector injector, List<Object> queryParam) {
        GroupMembership gm = GroupMembership.create(0, 0);
        try {
            ResultSet rs = getResultSet(injector, queryParam);
            if(rs.next()) {
                gm = GroupMembership.create(
                        rs.getInt("groupId"),
                        rs.getInt("userId"));
                return gm;
            }
        } catch (SQLException e) {
            return null;
        }
        return null;
    }

    @Override
    public List<GroupMembership> findMulti(ISQLInjector injector, List<Object> queryParam) {
        List<GroupMembership> gmList = new ArrayList<>();
        try {
            ResultSet rs = getResultSet(injector, queryParam);
            while(rs.next()) {
                GroupMembership gm = GroupMembership.create(
                        rs.getInt("groupId"),
                        rs.getInt("userId"));
                gmList.add(gm);
            }
        } catch (SQLException e) {
            return null;
        }
        return gmList;
    }
}
