package MockClasses;

import Entity.Order;
import Injector.IInjector;
import UnitofWork.IUnitofWork;

import java.util.List;

public class MockOrderRepository implements IUnitofWork<Order> {
    public boolean isUser = false;
    private Order order;

    public MockOrderRepository() {
    }

    @Override
    public Order read(IInjector injector, List<Object> param, String key) {
        if(isUser) {
            order = new Order();
            order.setOrderId(1);
            order.setUserId(1);
            return order;
        } else {
            return new Order();
        }
    }

    @Override
    public List<Order> readMulti(IInjector injector, List<Object> param, String key) {
        return null;
    }

    @Override
    public Order read(IInjector injector, List<Object> param) {
        if(isUser) {
            order = new Order();
            order.setOrderId(1);
            return order;
        } else {
            return new Order();
        }
    }

    @Override
    public List<Order> readMulti(IInjector injector, List<Object> param) {
        return null;
    }

    @Override
    public void registerNew(Order entity) {

    }

    @Override
    public void registerModified(Order entity) {

    }

    @Override
    public void registerDeleted(Order entity) {

    }

    @Override
    public void commit() {

    }
}