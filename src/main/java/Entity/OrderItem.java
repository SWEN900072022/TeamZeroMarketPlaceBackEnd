package Entity;

import org.javamoney.moneta.Money;

import javax.money.Monetary;

public class OrderItem {
    private int orderId;
    private int listingId;
    private int quantity;
    private Money unitPrice;
    private int priceInCents;

    public OrderItem() {
    }

    public OrderItem(int orderId, int listingId, int quantity, Money unitPrice) {
        this.orderId = orderId;
        this.listingId = listingId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
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
        if(unitPrice == null && priceInCents != 0) {
            // Somewhat lazy loading
            Money unitPriceInCents = Money.of(getPriceInCents(), Monetary.getCurrency("AUD"));
            setUnitPrice(unitPriceInCents.divide(100));
        } else if(unitPrice == null) {
            // Retrieve it from the database with the load function
        }
        return unitPrice;
    }

    public void setUnitPrice(Money unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getPriceInCents() {
        return priceInCents;
    }

    public void setPriceInCents(int priceInCents) {
        this.priceInCents = priceInCents;
    }
}
