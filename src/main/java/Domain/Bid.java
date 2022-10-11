package Domain;

import org.javamoney.moneta.Money;

import javax.money.Monetary;

public class Bid {
    private int listingId;
    private int userId;
    private Money bidAmount;

    protected Bid(int listingId, int userId) {
        this.bidAmount = Money.of(0, Monetary.getCurrency("AUD"));
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
    public static Bid create(int listingId, int userId) {
        return new Bid(listingId, userId);
    }
}
