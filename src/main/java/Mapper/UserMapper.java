package Mapper;

import Entity.User;
import Util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class UserMapper extends Mapper<User> {
    private Connection conn = null;

    public UserMapper() {
    }

    public boolean insert(User user) {
        String sql = String.format(
                "INSERT INTO users (username, password, email, roles) VALUES ('%s','%s','%s', '%s');",
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getRoles());
        PreparedStatement statement;

        try {
            if(conn == null) {
                conn = Util.getConnection();
            }
            statement = conn.prepareStatement(sql);
            statement.execute();
        } catch (SQLException e) {
            // Print out the exceptions for now
            System.out.println(e);
            return false;
        }
        return true;
    }

    public boolean delete(User u) {
        return false;
    }

    public boolean modify(User u) {
        return false;
    }

    public List<User> find(Map<String, String> map) {
        return find(map, 0);
    }

    public List<User> find(Map<String, String> map, int mode) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM users");
        PreparedStatement statement;
        Iterator<Map.Entry<String, String>> itr = map.entrySet().iterator();
        List<User> list = new ArrayList<>();
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
                if(mode == 0) {
                    sb.append(" AND ");
                } else {
                    sb.append(" OR ");
                }
            } else {
                sb.append(";");
            }
        }

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
                list.add(user);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }
}
