package Entity;

public class ShoppingCartItem {
    private int userId;
    private int listingId;

    public ShoppingCartItem() {
    }

    public ShoppingCartItem(int userId, int listingId) {
        this.userId = userId;
        this.listingId = listingId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getListingId() {
        return listingId;
    }

    public void setListingId(int listingId) {
        this.listingId = listingId;
    }
}
