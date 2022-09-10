package Mapper;

import Entity.Order;
import Util.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class OrderMapper extends Mapper<Order> {
    private Connection conn = null;
    public OrderMapper() {

    }
    @Override
    public boolean insert(List<Order> orderList) {
        StringBuilder sb = new StringBuilder();
        PreparedStatement statement;
        ListIterator<Order> orderListIterator = orderList.listIterator();
        sb.append("INSERT INTO orders (listing_id, quantity) VALUES");

        while(orderListIterator.hasNext()) {
            Order order = orderListIterator.next();
            sb.append(String.format("('%s','%s')",
                    order.getListingId(),
                    order.getQuantity()));
            if(!orderListIterator.hasNext()) {
                sb.append(";");
            } else {
                sb.append(",");
            }
        }

        try{
            if(conn == null) {
                conn = Util.getConnection();
            }
            statement = conn.prepareStatement(sb.toString());
            statement.execute();
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(List<Order> TEntity) {
        return false;
    }

    @Override
    public boolean modify(List<Order> orderList) {
        List<String> fieldsToBeUpdated = Order.getOrderAttribute();
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE orders as o set ");
        Iterator<String> fItr = fieldsToBeUpdated.listIterator();

        while(fItr.hasNext()) {
            String field = fItr.next();
            sb.append(String.format("%s=o2.%s", field, field));
            if(fItr.hasNext()) {
                sb.append(",");
            }
        }

        sb.append(" from ( values ");
        Iterator<Order> orderIterator = orderList.listIterator();
        while(orderIterator.hasNext()) {
            Order order = orderIterator.next();
            sb.append(String.format("(%s, %s, %s)",
                    order.getId(),
                    order.getListingId(),
                    order.getQuantity()));

            if(fItr.hasNext()) {
                sb.append(",");
            }
        }

        sb.append(" ) as o2(");
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

        sb.append("where o.id=o2.id");

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

    @Override
    public Map<Integer, Order> find(Map<String, String> map) {
        return find(map, 0);
    }

    @Override
    public Map<Integer, Order> find(Map<String, String> map, int mode) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM orders");
        PreparedStatement statement;
        Iterator<Map.Entry<String, String>> itr = map.entrySet().iterator();
        Map<Integer, Order> list = new HashMap<>();
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

        // Additional conditions, such as limit should go here

        try{
             if(conn == null) {
                 conn = Util.getConnection();
             }

             statement = conn.prepareStatement(sb.toString());
             statement.execute();

             rs = statement.getResultSet();
             while(rs.next()) {
                 // Process the result set into an object
                 Order order = new Order();
                 order.setListingId(rs.getInt("listing_id"));
                 order.setQuantity(rs.getInt("quantity"));
                 order.setId(rs.getInt("id"));
                 list.put(order.getId(), order);
             }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }
}
