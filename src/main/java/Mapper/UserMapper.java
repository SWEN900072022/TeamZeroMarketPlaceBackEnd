package Mapper;

import Entity.User;
import Util.Util;

import java.sql.*;
import java.util.*;

public class UserMapper extends Mapper<User> {
    private Connection conn = null;

    public UserMapper() {
    }

    public boolean insert(List<User> userList) {
        StringBuilder sb = new StringBuilder();
        PreparedStatement statement;
        ListIterator<User> userListIterator = userList.listIterator();
        sb.append("INSERT INTO users (username, password, email, roles) VALUES");

        while(userListIterator.hasNext()) {
            User user = userListIterator.next();
            sb.append(String.format("('%s','%s','%s', '%s')",
                            user.getUsername(),
                            user.getPassword(),
                            user.getEmail(),
                            user.getRoles()));
            if(!userListIterator.hasNext()) {
                sb.append(";");
            } else {
                sb.append(",");
            }
        }

        try {
            if(conn == null) {
                conn = Util.getConnection();
            }
            statement = conn.prepareStatement(sb.toString());
            statement.execute();
        } catch (SQLException e) {
            // Print out the exceptions for now
            System.out.println(e);
            return false;
        }
        return true;
    }

    public boolean delete(List<User> userList) {
        return false;
    }

    public boolean modify(List<User> userList) {
        List<String> fieldsToBeUpdated = User.getUserAttributes();
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE users as u set ");
        Iterator<String> fItr = fieldsToBeUpdated.listIterator();

        while(fItr.hasNext()) {
            String field = fItr.next();
            sb.append(String.format("%s=u2.%s", field, field));
            if(fItr.hasNext()) {
                sb.append(",");
            }
        }

        sb.append(" from ( values ");
        Iterator<User> userIterator = userList.listIterator();
        while(userIterator.hasNext()) {
            User user = userIterator.next();
            sb.append(String.format("(%s, %s, %s, %s, %s)",
                    user.getUsername(),
                    user.getPassword(),
                    user.getEmail(),
                    user.getId(),
                    user.getRoles()));

            if(fItr.hasNext()) {
                sb.append(",");
            }
        }

        sb.append(" ) as u2(");
        fItr = fieldsToBeUpdated.listIterator();

        while(fItr.hasNext()) {
            String field = fItr.next();
            sb.append(String.format("%s", field));
            if(fItr.hasNext()) {
                sb.append(",");
            } else {
                sb.append(")");
            }
        }

        sb.append("where u.id=u2.id;");

        try {
            if(conn == null) {
                conn = Util.getConnection();
            }
            PreparedStatement statement = conn.prepareStatement(sb.toString());
            statement.execute();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public Map<Integer, User> find(Map<String, String> map) {
        return find(map, 0);
    }

    public Map<Integer, User> find(Map<String, String> map, int mode) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM users");
        PreparedStatement statement;
        Iterator<Map.Entry<String, String>> itr = map.entrySet().iterator();
        Map<Integer, User> list = new HashMap<>();
        ResultSet rs;

        if(itr.hasNext()) {
            sb.append(" WHERE ");
        } else {
            sb.append(";");
        }

        while(itr.hasNext()) {
            Map.Entry<String, String> entry = itr.next();
            sb.append(String.format("%s='%s'", entry.getKey(), entry.getValue()));
            if(itr.hasNext()) {
                if(mode == 0) { // 0 for and, 1 for or
                    sb.append(" AND ");
                } else {
                    sb.append(" OR ");
                }
            } else {
                sb.append(";");
            }
        }

        // Additional conditions, such as limit goes here

        try {
            if(conn == null) {
                conn = Util.getConnection();
            }
            statement = conn.prepareStatement(sb.toString());
            statement.execute();

            rs = statement.getResultSet();
            while(rs.next()) {
                User user = new User();
                user.setEmail(rs.getString("email"));
                user.setUsername(rs.getString("username"));
                user.setRoles(rs.getString("roles"));
                user.setPassword(rs.getString("password"));
                user.setId(rs.getInt("id"));
                list.put(user.getId(), user);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }
}
