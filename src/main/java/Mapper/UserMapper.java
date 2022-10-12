package Mapper;

import Domain.User;
import Enums.UserRoles;
import Injector.ISQLInjector;
import Util.SQLUtil;

import java.sql.*;
import java.util.*;

public class UserMapper extends GeneralMapper<User> {
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
            conn = SQLUtil.getConnection();
        }

        statement = conn.prepareStatement(
                "INSERT INTO users (username, password, email, role)" +
                        "VALUES (?, ?, ?, ?);"
        );
        statement.setString(1, user.getUsername());
        statement.setString(2, user.getPassword());
        statement.setString(3, user.getEmail());
        statement.setString(4, user.getRole());
        statement.execute();

        if(!shouldReturn) {
            return null;
        }

        ResultSet generatedKeys = statement.getGeneratedKeys();
        while(generatedKeys.next()) {
            user.setUserId(generatedKeys.getInt(1));
        }
        return user;
    }

    public boolean modify(User user) {
        try {
            PreparedStatement statement;

            if(conn == null) {
                conn = SQLUtil.getConnection();
            }

            statement = conn.prepareStatement(
                    "UPDATE users as u set " +
                            "username=u2.username, " +
                            "password=u2.password, " +
                            "email=u2.email, " +
                            "userId=u2.userId, " +
                            "role=u2.role " +
                            "from (values " +
                            "(?, ?, ?, ?, ?)" +
                            ") as u2(username, password, email, userId, role) " +
                            "where u.userId=u2.userId;");
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getEmail());
            statement.setInt(4, user.getUserId());
            statement.setString(5, user.getRole());
            statement.execute();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public User find(ISQLInjector injector, List<Object> queryParam) {
        try {
            ResultSet rs = getResultSet(injector, queryParam);
            if(rs.next()) {
                return User.create(
                        rs.getString("email"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getInt("userid"),
                        UserRoles.valueOf(rs.getString("role"))
                );
            }
        } catch (SQLException e) {
            return null;
        }
        return null;
    }

    @Override
    public List<User> findMulti(ISQLInjector injector, List<Object> queryParam) {
        List<User> userList = new ArrayList<User>();
        try {
            ResultSet rs = getResultSet(injector, queryParam);
            while(rs.next()) {
                User user = User.create(
                        rs.getString("email"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getInt("userid"),
                        UserRoles.valueOf(rs.getString("role"))
                );
                userList.add(user);
            }
        } catch (SQLException e) {
            System.out.println("error in query");
//            e.printStackTrace();
            return null;
        }
        return userList;
    }
}
