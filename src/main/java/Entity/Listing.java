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

public class Listing {
    private String description;
    private String title;
    private ListingTypes type;
    private int id;
    private int createdById;
    private int quantity;
    private Money price;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public Listing() {
        this.description = null;
        this.title = null;
        this.type = ListingTypes.FIXED_PRICE; // 0 for fixed, 1 for auctions
        this.id = 0;
        this.createdById = 0;
        this.quantity = 0;
        this.price = Money.of(0, Monetary.getCurrency("AUD"));
        this.startDate = LocalDateTime.now();
        this.endDate = LocalDateTime.now();
    }

    public Listing(String description, String title, int type, int createdById) {
        this.description = description;
        this.title = title;
        this.type = setType(type);
        this.id = 0;
        this.createdById = createdById;
        this.quantity = 0;
        this.price = Money.of(0, Monetary.getCurrency("AUD"));
        this.startDate = LocalDateTime.now();
        this.endDate = LocalDateTime.now();
    }

    public Listing(String description, String title, int type, int createdById, int price, int quantity) {
        this.description = description;
        this.title = title;
        this.type = setType(type);
        this.id = 0;
        this.createdById = createdById;
        this.quantity = quantity;
        this.price = Money.of(price, Monetary.getCurrency("AUD"));
        this.startDate = LocalDateTime.now();
        this.endDate = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCreatedById() {
        return createdById;
    }

    public void setCreatedById(int createdById) {
        this.createdById = createdById;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ListingTypes getType() {
        return type;
    }

    public String getTypeString() {
        return type.toString();
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

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public void load() {
        // Lazy Initialisation
        // Create a mapper and load the values into the object
        Mapper<Listing> mapper = new ListingMapper();
        List<Object>param = new ArrayList<>();
        param.add(id);

        if(type == ListingTypes.FIXED_PRICE) {
            Listing listing = mapper.find(new FindIdInjector("listing"), param);

            this.price = listing.getPrice();
            this.quantity = listing.getQuantity();
        } else {
            // Auction loading unimplemented
        }
    }
}
