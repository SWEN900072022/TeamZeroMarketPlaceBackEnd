package Model;

import Entity.Order;
import UnitofWork.IUnitofWork;
import UnitofWork.OrderRepository;

public class OrderModel {
    private IUnitofWork<Order> repo;

    public OrderModel() {
        repo = new OrderRepository();
    }

    public OrderModel(IUnitofWork<Order> repo) {
        this.repo = repo;
    }

    public boolean createOrders(Integer[] listingId, Integer[] quantity) {
        // First we validate that the quantity is sufficient

    }
}
