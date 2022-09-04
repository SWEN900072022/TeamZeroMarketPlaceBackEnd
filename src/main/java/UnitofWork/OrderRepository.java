package UnitofWork;

import Entity.Order;

import java.util.Map;

public class OrderRepository implements IUnitofWork<Order> {
    @Override
    public Map<Integer, Order> read(Integer[] id) {
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
    public void commit() throws Exception {

    }
}
