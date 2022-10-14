package MockClasses;

import Domain.OrderItem;
import Injector.ISQLInjector;
import Mapper.Mapper;
import org.javamoney.moneta.Money;

import javax.money.Monetary;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class MockOrderItemMapper implements Mapper<OrderItem> {
    // In-memory database
    public List<OrderItem> orderItems = new ArrayList<>();

    public MockOrderItemMapper() {
        // Populate the order items with some dummy data
        OrderItem oi = OrderItem.create(
                1,
                1,
                1,
                Money.of(1, Monetary.getCurrency("AUD"))
        );

        OrderItem oi2 = OrderItem.create(
                2,
                2,
                2,
                Money.of(2, Monetary.getCurrency("AUD"))
        );

        orderItems.add(oi);
        orderItems.add(oi2);
    }

    @Override
    public boolean insert(OrderItem TEntity) {
        orderItems.add(TEntity);
        return true;
    }

    @Override
    public boolean delete(OrderItem TEntity) {
        orderItems.removeIf(t -> (
                t.getListingId() == TEntity.getListingId() &&
                        t.getOrderId() == TEntity.getListingId()
                ));
        return true;
    }

    @Override
    public boolean modify(OrderItem TEntity) {
        delete(TEntity);
        insert(TEntity);
        return true;
    }

    @Override
    public OrderItem find(ISQLInjector injector, List<Object> queryParam) {
        return orderItems.get(0);
    }

    @Override
    public List<OrderItem> findMulti(ISQLInjector injector, List<Object> queryParam) {
        return orderItems;
    }

    @Override
    public void setConnection(Connection conn) {

    }
}
