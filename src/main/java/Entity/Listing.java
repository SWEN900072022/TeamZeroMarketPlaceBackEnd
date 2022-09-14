package Entity;

import Enums.ListingTypes;
import Injector.FindIdInjector;
import Mapper.Mapper;
import Mapper.ListingMapper;
import org.javamoney.moneta.Money;

import javax.money.Monetary;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Listing {
    private int listingId;
    private int groupId;
    private ListingTypes type;
    private String title;
    private String description;
    private int quantity;
    private Money price;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Listing() {
        this.listingId = 0;
        this.groupId = 0;
        this.type = null;
        this.title = "";
        this.description = "";
        this.quantity = 0;
        this.price = Money.of(0, Monetary.getCurrency("AUD"));
        this.startTime = null;
        this.endTime = null;
    }

    public Listing(int listingId, int groupId, ListingTypes type, String title, String description, int quantity, Money price, LocalDateTime startTime, LocalDateTime endTime) {
        this.listingId = listingId;
        this.groupId = groupId;
        this.type = type;
        this.title = title;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getListingId() {
        return listingId;
    }

    public void setListingId(int listingId) {
        this.listingId = listingId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public ListingTypes getType() {
        return type;
    }

    public void setType(ListingTypes type) {
        this.type = type;
    }

    public ListingTypes setType(int type) {
        if(type == 0) {
            return ListingTypes.FIXED_PRICE;
        } else {
            return ListingTypes.AUCTION;
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Money getPrice() {
        return price;
    }

    public void setPrice(Money price) {
        this.price = price;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void load() {
        // Lazy Initialisation
        // Create a mapper and load the values into the object
        Mapper<Listing> mapper = new ListingMapper();
        List<Object> param = new ArrayList<>();
        param.add(listingId);

        Listing listing = mapper.find(new FindIdInjector("listings"), param);

        this.price = listing.getPrice();
        this.quantity = listing.getQuantity();
        this.startTime = listing.getStartTime();
        this.endTime = listing.getEndTime();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Listing listing = (Listing) o;
        return getListingId() == listing.getListingId() && getGroupId() == listing.getGroupId() && getQuantity() == listing.getQuantity() && getType() == listing.getType() && Objects.equals(getTitle(), listing.getTitle()) && Objects.equals(getDescription(), listing.getDescription()) && Objects.equals(getPrice(), listing.getPrice()) && Objects.equals(getStartTime(), listing.getStartTime()) && Objects.equals(getEndTime(), listing.getEndTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getListingId(), getGroupId(), getType(), getTitle(), getDescription(), getQuantity(), getPrice(), getStartTime(), getEndTime());
    }
}
