package UnitofWork;

import Entity.Order;

public class OrderRepository implements IUnitofWork<Order> {

    @Override
    public void read(Order[] entity) {

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
