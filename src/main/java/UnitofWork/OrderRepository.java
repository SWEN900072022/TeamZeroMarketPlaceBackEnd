package UnitofWork;

import Entity.Order;
import Enums.UnitActions;
import Mapper.OrderMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderRepository implements IUnitofWork<Order> {
    private final Map<String, List<Order>> context;
    private final OrderMapper oMapper;

    public OrderRepository() {
        context = new HashMap<>();
        oMapper = new OrderMapper();
    }

    @Override
    public Map<Integer, Order> read(Integer[] id) {
        return null;
    }

    @Override
    public void registerNew(Order entity) {
        register(entity, UnitActions.INSERT.toString());
    }

    @Override
    public void registerModified(Order entity) {
        register(entity, UnitActions.MODIFY.toString());
    }

    @Override
    public void registerDeleted(Order entity) {
        register(entity, UnitActions.DELETE.toString());
    }

    @Override
    public void commit() throws Exception {
        if(context.size() == 0) {
            return;
        }

        if (context.containsKey(UnitActions.INSERT.toString())) {
            commitNew();
        }

        if(context.containsKey(UnitActions.MODIFY.toString())) {
            commitModify();
        }

        if(context.containsKey(UnitActions.DELETE.toString())) {
            commitDel();
        }
    }

    private void register(Order order, String operation) {
        List<Order> orderToBeRegistered = context.get(operation);
        if(orderToBeRegistered == null) {
            orderToBeRegistered = new ArrayList<>();
        }
        orderToBeRegistered.add(order);
        context.put(operation, orderToBeRegistered);
    }

    private void commitNew() {
        List<Order> orderList = context.get(UnitActions.INSERT.toString());
        for(Order order : orderList) {
            oMapper.insert(order);
        }
    }

    private void commitModify() {
        List<Order> orderList = context.get(UnitActions.MODIFY.toString());
        for(Order order : orderList) {
            oMapper.modify(order);
        }
    }

    private void commitDel() {
        List<Order> orderList = context.get(UnitActions.DELETE.toString());
        for(Order order : orderList) {
            oMapper.delete(order);
        }
    }
}
