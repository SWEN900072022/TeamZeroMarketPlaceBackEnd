package MockClasses;

import Domain.Order;
import Injector.ISQLInjector;
import Mapper.Mapper;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class MockOrderMapper implements Mapper<Order> {
    // In-memory database
    public List<Order> orders = new ArrayList<>();

    public MockOrderMapper() {
        // Populate the bids with some dummy data
        Order order = Order.create(
                1,
                1,
                "a"
        );

        Order order1 = Order.create(
                2,
                2,
                "b"
        );

        orders.add(order);
        orders.add(order1);
    }

    @Override
    public boolean insert(Order TEntity) {
        orders.add(TEntity);
        return true;
    }

    @Override
    public boolean delete(Order TEntity) {
        orders.removeIf(t -> (
                t.getOrderId() == TEntity.getOrderId()
                ));
        return true;
    }

    @Override
    public boolean modify(Order TEntity) {
        delete(TEntity);
        insert(TEntity);
        return true;
    }

    @Override
    public Order find(ISQLInjector injector, List<Object> queryParam) {
        return orders.get(0);
    }

    @Override
    public List<Order> findMulti(ISQLInjector injector, List<Object> queryParam) {
        return orders;
    }

    @Override
    public void setConnection(Connection conn) {

    }
}
