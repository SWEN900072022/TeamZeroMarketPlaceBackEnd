package Domain;

import Injector.DeleteConditionInjector.DeleteIdInjector;
import Injector.FindConditionInjector.*;
import UnitofWork.IUnitofWork;
import Util.GeneralUtil;

import java.util.ArrayList;
import java.util.List;

public class Order extends EntityObject {
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

    public static Order create(int orderId, int userId, String address) {
        return new Order(orderId, userId, address, null);
    }

    public static Order create(int orderId, int userId, String address, List<OrderItem> orderItemList) {
        return new Order(orderId, userId, address, orderItemList);
    }

    public void setRepository(IUnitofWork repo) {
        this.repo = repo;
    }

    public static List<OrderItem> getOrdersByGroupId(int groupId, IUnitofWork repo){
        List<Object> param = new ArrayList<>();
        param.add(groupId);

        // Get order details by groupId
        List<OrderItem> ordList = GeneralUtil.castObjectInList(repo.readMulti(
                new FindOrderForSellerGroupInjector(),
                param,
                OrderItem.class), OrderItem.class);

        return ordList;
    }

    public static List<Order> getOrdersByUserId(int userId, IUnitofWork repo) {
        List<Object> param = new ArrayList<>();
        param.add(userId);

        // Get order details
        List<Order> ordList = GeneralUtil.castObjectInList(repo.readMulti(
                        new FindOrderWithUser(),
                        param,
                        Order.class), Order.class);

        for(Order ord : ordList) {
            param = new ArrayList<>();
            param.add(ord.orderId);
            // Get order items
            List<OrderItem> oi = GeneralUtil.castObjectInList(
                    repo.readMulti(
                            new FindOrderItemWithOrderId(),
                            param,
                            OrderItem.class),
                    OrderItem.class
            );
            ord.orderItemList = oi;
        }

        return ordList;
    }

    public static Order getLastOrderItem(IUnitofWork repo) {
        return (Order) repo.read(
                new FindLastOrderInjector(),
                new ArrayList<>(),
                Order.class
        );
    }

    public static List<Order> getAllOrders(IUnitofWork repo) {
        List<Object> param = new ArrayList<>();

        // Get order details
        List<Order> ordList = GeneralUtil.castObjectInList(repo.readMulti(
                new FindAllInjector("orders"),
                param,
                Order.class), Order.class);

        return ordList;
    }

    public static List<OrderItem> getOrderItemList(int orderId, IUnitofWork repo) {
        List<Object> param = new ArrayList<>();
        param.add(orderId);
        // Get order items
        List<OrderItem> oi = GeneralUtil.castObjectInList(
                repo.readMulti(
                        new FindOrderItemWithOrderId(),
                        param,
                        OrderItem.class),
                OrderItem.class
        );
        return oi;
    }

    public OrderItem modifyOrderItem(int listingId, int quantity, int stockLevel) {
        // Lazy load if orderItemList is empty
        if(orderItemList == null) {
            getOrderItemList(orderId, repo);
        }

        for(OrderItem ord : orderItemList) {
            if(ord.getListingId() == listingId && (quantity - ord.getQuantity() <= stockLevel)) {
                ord.setQuantity(quantity);
                return ord;
            }
        }
        throw new IllegalArgumentException();
    }

    public OrderItem getOrderItem(int listingId) {
        // Lazy load if orderItemList is empty
        if(orderItemList == null) {
            orderItemList = getOrderItemList(orderId, repo);
        }

        for(OrderItem ord : orderItemList) {
            if(ord.getListingId() == listingId) {
                return ord;
            }
        }
        throw new IllegalArgumentException();
    }

//    public void deleteOrderItem(Listing l) {
//        orderItemList.removeIf(t -> (
//                t.getListingId() == l.getListingId()
//        ));
//    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<OrderItem> getOrderItemList() {
        if(orderItemList == null) {
            orderItemList = getOrderItemList(orderId, repo);
        }
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public void markForDelete() {
        List<Object>param = new ArrayList<>();
        param.add(getOrderId());
        setInjector(new DeleteIdInjector("orders"));
        setParam(param);
    }
}
