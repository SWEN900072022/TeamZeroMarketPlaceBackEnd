package Entity;

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
        if(orderId == 0) {
            return true;
        }
        return false;
    }
}
