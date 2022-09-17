package MockClasses;

import Entity.OrderItem;
import Injector.IInjector;
import UnitofWork.IUnitofWork;

import java.util.List;

public class MockOrderItemRepository implements IUnitofWork<OrderItem> {
    @Override
    public OrderItem read(IInjector injector, List<Object> param, String key) {
        return null;
    }

    @Override
    public List<OrderItem> readMulti(IInjector injector, List<Object> param, String key) {
        return null;
    }

    @Override
    public OrderItem read(IInjector injector, List<Object> param) {
        return null;
    }

    @Override
    public List<OrderItem> readMulti(IInjector injector, List<Object> param) {
        return null;
    }

    @Override
    public void registerNew(OrderItem entity) {

    }

    @Override
    public void registerModified(OrderItem entity) {

    }

    @Override
    public void registerDeleted(OrderItem entity) {

    }

    @Override
    public void commit() {

    }
}
