package Entity;

import org.javamoney.moneta.Money;

import javax.money.Monetary;
import java.math.BigDecimal;

public class Bid extends EntityObject{
    private int listingId;
    private int userId;
    private Money bidAmount;
    private int bidAmountInCents;

    public Bid() {
        this.bidAmount = Money.of(0, Monetary.getCurrency("AUD"));
        this.listingId = 0;
        this.userId = 0;
    }

    public Bid(int listingId, int userId, BigDecimal bidAmount) {
        this.bidAmount = Money.of(bidAmount, Monetary.getCurrency("AUD"));
        this.listingId = listingId;
        this.userId = userId;
    }

    public int getListingId() {
        return listingId;
    }

    public void setListingId(int listingId) {
        this.listingId = listingId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Money getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(Money bidAmount) {
        this.bidAmount = bidAmount;
    }

    public int getBidAmountInCents() {
        return bidAmountInCents;
    }

    public void setBidAmountInCents(int bidAmountInCents) {
        this.bidAmountInCents = bidAmountInCents;
    }

    public boolean isEmpty() {
        // For bids, we need all three fields to be populated
        if(this.bidAmount == Money.of(0, Monetary.getCurrency("AUD")) ||
            this.listingId == 0 ||
            this.userId == 0
        ) {
            return true;
        }
        return false;
    }
}
