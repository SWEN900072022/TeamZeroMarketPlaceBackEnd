package Domain;

import Enums.ListingTypes;
import Injector.DeleteConditionInjector.DeleteIdInjector;
import Injector.FindConditionInjector.FindAllInjector;
import Injector.FindConditionInjector.FindIdInjector;
import Injector.FindConditionInjector.FindListingWithGroupIdInjector;
import Injector.FindConditionInjector.FindTitleInjector;
import Injector.ISQLInjector;
import UnitofWork.IUnitofWork;
import Util.GeneralUtil;
import org.javamoney.moneta.Money;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Listing extends EntityObject {
    private int listingId;
    private int groupId;
    private ListingTypes type;
    private String title;
    private String description;
    private int quantity;
    private Money price;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    protected Listing(int listingId, int groupId, ListingTypes type, String title, String description, int quantity, Money price, LocalDateTime startTime, LocalDateTime endTime) {
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

    // TODO: Replace with abstarct class later
    public boolean isEmpty() {
        return false;
    }

    public static Listing create(int listingId, int groupId, ListingTypes type, String title, String description, int quantity, Money price, LocalDateTime startTime, LocalDateTime endTime) {
        if(type == ListingTypes.FIXED_PRICE) {
            return new FixedPriceListing(listingId, groupId, type, title, description, quantity, price, startTime, endTime);
        }
        if(type == ListingTypes.AUCTION) {
            return new AuctionListing(listingId, groupId, type, title, description, quantity, price, startTime, endTime);
        }
        throw new IllegalArgumentException();
    }

    public static Listing getListingById(int listingId, IUnitofWork repo) {
        List<Object> param = new ArrayList<>();
        param.add(listingId);
        return (Listing) repo.read(
                new FindIdInjector("listings"),
                param,
                Listing.class);
    }

    public static List<Listing> getListingByFilterCondition(List<Filter> filterCondition, IUnitofWork repo) {
        if(filterCondition == null || filterCondition.isEmpty()) {
            return GeneralUtil.castObjectInList(
                    repo.readMulti(
                            new FindAllInjector("listings"),
                            new ArrayList<>(), Listing.class),
                    Listing.class);
        }

        List<Listing> result = new ArrayList<>();
        // Populate the custom find injector
        for(Filter filter : filterCondition) {
            ISQLInjector inj = getInjector(filter.getFilterKey());

            List<Object> param = new ArrayList<>();
            param.add(filter.getFilterVal());

            List<Listing> temp = GeneralUtil.castObjectInList(repo.readMulti(inj, param, Listing.class), Listing.class);
            if(result.size() == 0) {
                result = temp;
            } else {
                Set<Listing> resultSet = result.stream()
                        .distinct()
                        .filter(temp::contains)
                        .collect(Collectors.toSet());
                result = new ArrayList<>(resultSet);
            }
        }
        return result;
    }

    private static ISQLInjector getInjector(String key) {
        switch(key) {
            case "title":
                return new FindTitleInjector();
            case "groupId":
                return new FindListingWithGroupIdInjector();
        }
        return null;
    }

    public ListingTypes getType() {
        return type;
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

    public void setType(ListingTypes type) {
        this.type = type;
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

    public void markForDelete() {
        List<Object>param = new ArrayList<>();
        param.add(getListingId());
        setInjector(new DeleteIdInjector("listings"));
        setParam(param);
    }
}
