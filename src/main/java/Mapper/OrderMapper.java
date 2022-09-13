package Mapper;

import Entity.Order;
import Entity.User;
import Injector.FindConditionInjector;
import Util.Util;

import java.sql.*;
import java.util.*;

public class OrderMapper extends GeneralMapper<Order> {
    public OrderMapper() {

    }

    @Override
    public boolean insert(Order order) {
        try {
            PreparedStatement statement;

            if (conn == null) {
                conn = Util.getConnection();
            }

            statement = conn.prepareStatement(
                    "INSERT INTO orders (userId, address) " +
                            "VALUES (?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, order.getUserId());
            statement.setString(2, order.getAddress());
            statement.execute();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(Order TEntity) {
        return false;
    }

    @Override
    public boolean modify(Order order) {
        if (conn == null) {
            conn = Util.getConnection();
        }
        try {
            PreparedStatement statement = conn.prepareStatement(
                    "UPDATE orders as o set " +
                            "orderId=o2.orderId, " +
                            "userId=o2.userId, " +
                            "address=o2.address " +
                            "from (values " +
                            "(?, ?, ?)" +
                            ") as o2(orderId, userId, address) " +
                            "where o.orderId=o2.orderId;"
            );
            statement.setInt(1, order.getOrderId());
            statement.setInt(2, order.getUserId());
            statement.setString(3, order.getAddress());
            statement.execute();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public Order find(FindConditionInjector injector, List<Object> queryParam) {
        Order order = new Order();
        try {
            ResultSet rs = getResultSet(injector, queryParam);
            if (rs.next()) {
                order.setOrderId(rs.getInt("orderId"));
                order.setUserId(rs.getInt("userId"));
                order.setAddress(rs.getString("address"));
            }
        } catch (SQLException e) {
            return null;
        }
        return order;
    }

    @Override
    public List<Order> findMulti(FindConditionInjector injector, List<Object> queryParam) {
        List<Order> orderList = new ArrayList<>();
        try {
            ResultSet rs = getResultSet(injector, queryParam);
            while(rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("orderId"));
                order.setUserId(rs.getInt("userId"));
                order.setAddress(rs.getString("address"));
                orderList.add(order);
            }
        } catch (SQLException e) {
            return null;
        }
        return orderList;
    }

}