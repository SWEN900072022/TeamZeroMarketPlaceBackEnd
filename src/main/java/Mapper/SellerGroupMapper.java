package Mapper;

import Entity.SellerGroup;
import Injector.IInjector;
import Util.Util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SellerGroupMapper extends GeneralMapper<SellerGroup> {
    @Override
    public boolean insert(SellerGroup TEntity) {
        try {
            insertOperation(TEntity, false);
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public SellerGroup insertWithResultSet(SellerGroup TEntity) {
        try {
            TEntity = insertOperation(TEntity, true);
        } catch (SQLException e) {
            return null;
        }
        return TEntity;
    }

    private SellerGroup insertOperation(SellerGroup TEntity, boolean shouldReturn) throws SQLException {
        PreparedStatement statement;

        if(conn == null) {
            conn = Util.getConnection();
        }

        statement = conn.prepareStatement(
                "INSERT INTO sellergroups (groupname) " +
                        "VALUES (?);",
                Statement.RETURN_GENERATED_KEYS
        );

        statement.setString(1, TEntity.getGroupName());
        statement.execute();

        if(!shouldReturn) {
            return null;
        }

        ResultSet generatedKeys = statement.getGeneratedKeys();
        if(generatedKeys.next()) {
            TEntity.setGroupId(generatedKeys.getInt("groupId"));
        }
        return TEntity;
    }

    @Override
    public boolean modify(SellerGroup TEntity) {
        try {
            if(conn == null) {
                conn = Util.getConnection();
            }

            PreparedStatement statement = conn.prepareStatement(
                    "UPDATE sellergroups as sg set " +
                            "groupid=sg2.groupid, " +
                            "groupname=sg2.groupname " +
                            "from (values " +
                            "(?, ?)" +
                            ") as sg2(groupId, groupName) " +
                            "where sg.groupId=sg2.groupId;"
            );
            statement.setInt(1, TEntity.getGroupId());
            statement.setString(2, TEntity.getGroupName());
            statement.execute();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    @Override
    public SellerGroup find(IInjector injector, List<Object> queryParam) {
        SellerGroup sg = new SellerGroup();
        try {
            ResultSet rs = getResultSet(injector, queryParam);
            if(rs.next()) {
                sg.setGroupId(rs.getInt("groupId"));
                sg.setGroupName(rs.getString("groupName"));
            }
        } catch (SQLException e) {
            return null;
        }
        return sg;
    }

    @Override
    public List<SellerGroup> findMulti(IInjector injector, List<Object> queryParam) {
        List<SellerGroup> sgList = new ArrayList<>();
        try {
            ResultSet rs = getResultSet(injector, queryParam);
            while(rs.next()) {
                SellerGroup sg = new SellerGroup();
                sg.setGroupId(rs.getInt("groupId"));
                sg.setGroupName(rs.getString("groupName"));
                sgList.add(sg);
            }
        } catch (SQLException e) {
            return null;
        }
        return sgList;
    }
}
