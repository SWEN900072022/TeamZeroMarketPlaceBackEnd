package Entity;

import Injector.FindIdInjector;
import Mapper.FixedPriceListingMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FixedPriceListingImpl extends Listing implements FixedPriceListing {
    private int price;
    private int quantity;
    private int fplId;

    public FixedPriceListingImpl() {
        super();
        this.price = 0;
        this.quantity = 0;
    }

    public FixedPriceListingImpl(String description, String title, int createdById, int price, int quantity) {
        super(description, title, 0, createdById);
        this.price = price;
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getFplId() {
        return fplId;
    }

    public void setFplId(int fplId) {
        this.fplId = fplId;
    }

    public void load() {
        // Lazy initialisation
        // Create a mapper and load the values into the object
        FixedPriceListingMapper mapper = new FixedPriceListingMapper();

        List<Object>param = new ArrayList<>();
        param.add(getId());

        FixedPriceListing result = mapper.find(new FindIdInjector("fixed_price_listing"), param);

        // There should only be one item in the result set
        this.price = result.getPrice();
        this.quantity = result.getQuantity();
        this.fplId = result.getFplId();
    }

    public static List<String> getFPListAttributes() {
        List<String> attr = new ArrayList<>();
        attr.add("id");
        attr.add("general_listing_id");
        attr.add("price");
        attr.add("quantity");
        return attr;
    }
}
