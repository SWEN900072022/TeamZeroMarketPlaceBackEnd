package Entity;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private int id;
    private int listingId;
    private int quantity;
    private int orderedBy;


    public Order() {
        this.id = 0;
        this.listingId = 0;
        this.quantity = 0;
        this.orderedBy = 0;
    }

    public int getOrderedBy() {
        return orderedBy;
    }

    public void setOrderedBy(int orderedBy) {
        this.orderedBy = orderedBy;
    }

    public Order(int listingId, int quantity, int orderedBy) {
        this.id = 0;
        this.listingId = listingId;
        this.quantity = quantity;
        this.orderedBy = orderedBy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public static List<String> getOrderAttribute() {
        List<String> attr = new ArrayList<>();
        attr.add("id");
        attr.add("listing_id");
        attr.add("quantity");
        attr.add("ordered_by");
        return attr;
    }
}
