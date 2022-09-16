package Entity;

import Enums.ListingTypes;
import Mapper.Mapper;
import Mapper.ListingMapper;
import org.javamoney.moneta.Money;

import javax.money.Monetary;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Listing extends EntityObject{
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
}
