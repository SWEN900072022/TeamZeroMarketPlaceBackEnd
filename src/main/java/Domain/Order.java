package Domain;

import UnitofWork.IUnitofWork;

import java.util.List;

public class Order {
    private int orderId;
    private int userId;
    private String address;
    private List<OrderItem> orderItemList;
    private IUnitofWork repo;

    protected Order(int orderId, int userId, String address, List<OrderItem> orderItemList) {
        this.orderId = orderId;
        this.userId = userId;
        this.address = address;
        this.orderItemList = orderItemList;
    }

    public static Order create(int orderId, int userId, String address, List<OrderItem> orderItemList) {
        return new Order(orderId, userId, address, orderItemList);
    }

    public void setRepository(IUnitofWork repo) {
        this.repo = repo;
    }

//    public void setOrderItemList() {
//        // We need a way to fetch the data
//        // Maybe a static read from the repo
//        orderItemList = repo.read();
//    }

    public OrderItem addOrderItem(Listing l, int quantity) {
        if(l.getQuantity() - quantity < 0) {
            return null;
        }
        OrderItem oi = OrderItem.create(orderId, l.getListingId(), quantity, l.getPrice());
        orderItemList.add(oi);
        return oi;
    }

    public void modifyOrderItem(Listing l, int quantity) {
        deleteOrderItem(l);
        addOrderItem(l, quantity);
    }

    public void deleteOrderItem(Listing l) {
        orderItemList.removeIf(t -> (
                t.getListingId() == l.getListingId()
        ));
    }
}
