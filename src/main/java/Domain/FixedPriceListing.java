package Domain;

import Enums.ListingTypes;
import org.javamoney.moneta.Money;

import javax.money.Monetary;
import java.time.LocalDateTime;
import java.util.Objects;

public class FixedPriceListing extends Listing{
    protected FixedPriceListing(int listingId, int groupId, ListingTypes type, String title, String description, int quantity, Money price, LocalDateTime startTime, LocalDateTime endTime) {
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
                        getPrice().isEqualTo(Money.of(0, Monetary.getCurrency("AUD")))
        ) {
            return true;
        }
        return false;
    }
}
