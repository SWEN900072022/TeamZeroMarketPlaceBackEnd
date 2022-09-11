package Mapper;

import Entity.Order;
import Injector.FindConditionInjector;
import Util.Util;

import java.sql.*;
import java.util.*;

public class OrderMapper implements Mapper<Order> {
    private Connection conn = null;

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
                    "INSERT INTO orders (listing_id, quantity, ordered_by) " +
                            "VALUES (?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, order.getListingId());
            statement.setInt(2, order.getQuantity());
            statement.setInt(3, order.getOrderedBy());
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
                            "id=o2.id, " +
                            "listing_id=o2.listing_id, " +
                            "quantity=o2.quantity, " +
                            "ordered_by=o2.ordered_by " +
                            "from (values " +
                            "(?, ?, ?, ?)" +
                            ") as o2(id, listing_id, quantity, ordered_by) " +
                            "where o.id=o2.id;"
            );
            statement.setInt(1, order.getId());
            statement.setInt(2, order.getListingId());
            statement.setInt(3, order.getQuantity());
            statement.setInt(4, order.getOrderedBy());
            statement.execute();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public Order find(FindConditionInjector injector, List<Object> queryParam) {
        Order order = new Order();
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
            while (rs.next()) {
                // Process the result set into an object
                order.setListingId(rs.getInt("listing_id"));
                order.setQuantity(rs.getInt("quantity"));
                order.setId(rs.getInt("id"));
                order.setOrderedBy(rs.getInt("ordered_by"));
            }
        } catch (SQLException e) {
            return null;
        }
        return order;
    }
}