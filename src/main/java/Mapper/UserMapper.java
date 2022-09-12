package Mapper;

import Entity.User;
import Injector.FindConditionInjector;
import Util.Util;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.*;

public class UserMapper extends Mapper<User> {
    private Connection conn = null;

    @Override
    public boolean insert(User user) {
        try {
            insertOperation(user, false);
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    private User insertOperation(User user, boolean shouldReturn) throws SQLException {
        PreparedStatement statement;

        if(conn == null) {
            conn = Util.getConnection();
        }

        statement = conn.prepareStatement(
                "INSERT INTO users (username, password, email, roles)" +
                        "VALUES (?, ?, ?, ?);"
        );
        statement.setString(1, user.getUsername());
        statement.setString(2, user.getPassword());
        statement.setString(3, user.getEmail());
        statement.setString(4, user.getRoles());
        statement.execute();

        if(!shouldReturn) {
            return null;
        }

        ResultSet generatedKeys = statement.getGeneratedKeys();
        while(generatedKeys.next()) {
            user.setId(generatedKeys.getInt(1));
        }
        return user;
    }

    public boolean delete(User user) {
        return false;
    }

    public boolean modify(User user) {
        try {
            PreparedStatement statement;

            if(conn == null) {
                conn = Util.getConnection();
            }

            statement = conn.prepareStatement(
                    "UPDATE users as u set " +
                            "username=u2.username, " +
                            "password=u2.password, " +
                            "email=u2.email, " +
                            "id=u2.id, " +
                            "role=u2.role " +
                            "from (values " +
                            "(?, ?, ?, ?, ?)" +
                            ") as u2(username, password, email, id, role) " +
                            "where u.id=u2.id;");
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getEmail());
            statement.setInt(4, user.getId());
            statement.setString(5, user.getRoles());
            statement.execute();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public User find(FindConditionInjector injector, List<Object> queryParam) {
        User user = new User();
        try {
            PreparedStatement statement;

            if(conn == null) {
                conn = Util.getConnection();
            }

            statement = conn.prepareStatement(injector.getSQLQuery());
            for(int i = 1; i <= queryParam.size(); i++) {
                Object param = queryParam.get(i-1);
                if(param instanceof Integer) {
                    statement.setInt(i, (Integer)param);
                } else if(param instanceof String) {
                    statement.setString(i, (String)param);
                }
            }
            statement.execute();

            ResultSet rs = statement.getResultSet();
            while(rs.next()) {
                user.setEmail(rs.getString("email"));
                user.setUsername(rs.getString("username"));
                user.setRoles(rs.getString("roles"));
                user.setPassword(rs.getString("password"));
                user.setId(rs.getInt("id"));
            }
        } catch (SQLException e) {
            return null;
        }
        return user;
    }

    public List<User> findAllItems(FindConditionInjector injector){
        List <User> allUsers= new ArrayList<User>();
        PreparedStatement statement;
        ResultSet rs;
        try{
            if(conn==null){
                conn = Util.getConnection();
            }
            statement = conn.prepareStatement(injector.getSQLQuery());
            rs = statement.executeQuery();
            while(rs.next()){
                User user = new User();
                user.setEmail(rs.getString("email"));
                user.setUsername(rs.getString("username"));
                user.setRoles(rs.getString("roles"));
                user.setPassword(rs.getString("password"));
                allUsers.add(user);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return allUsers;
    }
}
