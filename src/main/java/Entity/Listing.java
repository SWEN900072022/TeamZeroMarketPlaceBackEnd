package Entity;

import Enums.ListingTypes;

public abstract class Listing {
    private String description;
    private String title;
    private ListingTypes type;
    private int id;
    private int createdById;

    public Listing() {
        this.description = null;
        this.title = null;
        this.type = ListingTypes.FIXED_PRICE; // 0 for fixed, 1 for auctions
        this.id = 0;
        this.createdById = 0;
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

    public Listing(String description, String title, int type, int createdById) {
        this.description = description;
        this.title = title;
        this.type = setType(type);
        this.id = 0;
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
}
