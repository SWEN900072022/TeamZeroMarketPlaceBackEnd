package MockClasses;

import Entity.Order;
import Injector.FindConditionInjector;
import UnitofWork.IUnitofWork;

import java.util.List;

public class MockOrderRepository implements IUnitofWork<Order> {
    @Override
    public Order read(FindConditionInjector injector, List<Object> param, String key) {
        return null;
    }

    @Override
    public List<Order> readMulti(FindConditionInjector injector, List<Object> param, String key) {
        return null;
    }

    @Override
    public Order read(FindConditionInjector injector, List<Object> param) {
        return null;
    }

    @Override
    public List<Order> readMulti(FindConditionInjector injector, List<Object> param) {
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
