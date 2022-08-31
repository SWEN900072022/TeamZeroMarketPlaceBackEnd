package Entity;

public class FixedPriceListing extends Listing {
    private int price;
    private int quantity;

    public FixedPriceListing() {
        super();
        this.price = 0;
        this.quantity = 0;
    }

    public FixedPriceListing(String description, String title, int price, int quantity) {
        super(description, title, 0);
        this.price = price;
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
