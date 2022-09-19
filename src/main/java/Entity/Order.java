package Entity;

import Injector.FindConditionInjector.FindIdInjector;
import Mapper.OrderMapper;
import UnitofWork.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Order extends EntityObject{
    private int orderId;
    private int userId;
    private String address;

    public Order() {
        this.orderId = 0;
        this.userId = 0;
        this.address = "";
    }

    public Order(int orderId, int userId, String address) {
        this.orderId = orderId;
        this.userId = userId;
        this.address = address;
    }

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

    public boolean isEmpty() {
        if(userId == 0 || address == null || address.equals("")) {
            boolean canLoad = load();
            return !canLoad;
        }
        return false;
    }

    public boolean load() {
        // Check to see if the id is present, if not return false
        if(orderId == 0) {
            return false;
        }

        Repository<Order> orderRepository = new Repository<Order>(new OrderMapper());
        List<Object> param = new ArrayList<>();
        param.add(orderId);
        Order order = orderRepository.read(new FindIdInjector("orders"), param);

        //populate the object with the results
        userId = order.getUserId();
        address = order.getAddress();

        return true;
    }
}
