package Domain;

import Enums.ListingTypes;
import org.javamoney.moneta.Money;

import java.time.LocalDateTime;
import java.util.Objects;

public class AuctionListing extends Listing{
    public AuctionListing(int listingId, int groupId, ListingTypes type, String title, String description, int quantity, Money price, LocalDateTime startTime, LocalDateTime endTime) {
        super(listingId, groupId, type, title, description, quantity, price, startTime, endTime);
    }

    @Override
    public boolean isEmpty() {
        if(
                getGroupId() == 0 ||
                getType() == null ||
                Objects.equals(getTitle(), "") ||
                Objects.equals(getDescription(), "") ||
                getQuantity() < 0 ||
                getStartTime() == null ||
                getEndTime() == null
        ) {
            return true;
        }
        return false;
    }

    public Bid bid(Money bid, LocalDateTime bidTime, int userId) {
        if(bidTime.isAfter(getEndTime()) || bid.isLessThanOrEqualTo(getPrice())) {
            return null;
        }

        setPrice(bid);
        return Bid.create(getListingId(), userId, bid);
    }
}
