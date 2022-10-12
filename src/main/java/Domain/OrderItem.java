package Domain;

import UnitofWork.IUnitofWork;
import org.javamoney.moneta.Money;

public class OrderItem extends EntityObject{
    private int orderId;
    private int listingId;
    private int quantity;
    private Money unitPrice;

    protected OrderItem(int orderId, int listingId, int quantity, Money unitPrice) {
        this.orderId = orderId;
        this.listingId = listingId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public static OrderItem create(int orderId, int listingId, int quantity, Money unitPrice) {
        return new OrderItem(orderId, listingId, quantity, unitPrice);
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getListingId() {
        return listingId;
    }

    public void setListingId(int listingId) {
        this.listingId = listingId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Money getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Money unitPrice) {
        this.unitPrice = unitPrice;
    }
}
