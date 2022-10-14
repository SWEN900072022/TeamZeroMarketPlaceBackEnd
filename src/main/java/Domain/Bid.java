package Domain;

import org.javamoney.moneta.Money;

import javax.money.Monetary;

public class Bid extends EntityObject {
    private int listingId;
    private int userId;
    private Money bidAmount;

    protected Bid(int listingId, int userId, Money bidAmount) {
        this.bidAmount = bidAmount;
        this.setListingId(listingId);
        this.setUserId(userId);
    }

    public void setListingId(int listingId) {
        this.listingId = listingId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    // Factory to create bids
    public static Bid create(int listingId, int userId, Money bidAmount) {
        return new Bid(listingId, userId, bidAmount);
    }

    public int getListingId() {
        return listingId;
    }

    public int getUserId() {
        return userId;
    }

    public Money getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(Money bidAmount) {
        this.bidAmount = bidAmount;
    }
}
