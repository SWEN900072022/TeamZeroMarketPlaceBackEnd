package MockClasses;

import Entity.Order;
import Injector.ISQLInjector;
import Mapper.Mapper;
import java.util.ArrayList;
import java.util.List;

public class MockOrderMapper implements Mapper<Order> {
    // In-memory database
    public List<Order> orders = new ArrayList<>();

    public MockOrderMapper() {
        // Populate the bids with some dummy data
        Order order = new Order();
        order.setOrderId(1);
        order.setUserId(1);
        order.setAddress("a");

        Order order1 = new Order();
        order1.setOrderId(2);
        order1.setUserId(2);
        order1.setAddress("b");

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
}
