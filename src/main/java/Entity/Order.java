package Entity;

public class Order {
    private int id;
    private int listingId;
    private int quantity;

    public Order() {
        this.id = 0;
        this.listingId = 0;
        this.quantity = 0;
    }

    public Order(int listingId, int quantity) {
        this.id = 0;
        this.listingId = listingId;
        this.quantity = quantity;
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
}
