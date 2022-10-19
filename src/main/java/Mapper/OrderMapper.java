package Mapper;

import Domain.Order;
import Injector.ISQLInjector;
import Util.SQLUtil;

import java.sql.*;
import java.util.*;

public class OrderMapper extends GeneralMapper<Order> {
    public static int latestKeyVal = 0;
    public OrderMapper() {

    }

    @Override
    public boolean insert(Order order) {
        try {
            PreparedStatement statement;

            statement = conn.prepareStatement(
                    "INSERT INTO orders (userId, address) " +
                            "VALUES (?, ?);", Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, order.getUserId());
            statement.setString(2, order.getAddress());
            statement.execute();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            while(generatedKeys.next()) {
                order.setOrderId(generatedKeys.getInt("orderId"));
            }
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean modify(Order order) {
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

    public Order find(ISQLInjector injector, List<Object> queryParam) {
        try {
            ResultSet rs = getResultSet(injector, queryParam);
            if (rs.next()) {
                return Order.create(
                        rs.getInt("orderId"),
                        rs.getInt("userId"),
                        rs.getString("address")
                );
            }
        } catch (SQLException e) {
            return null;
        }
        return null;
    }

    @Override
    public List<Order> findMulti(ISQLInjector injector, List<Object> queryParam) {
        List<Order> orderList = new ArrayList<>();
        try {
            ResultSet rs = getResultSet(injector, queryParam);
            while(rs.next()) {
                Order order = Order.create(
                        rs.getInt("orderId"),
                        rs.getInt("userId"),
                        rs.getString("address")
                );
                orderList.add(order);
            }
        } catch (SQLException e) {
            return null;
        }
        return orderList;
    }

}