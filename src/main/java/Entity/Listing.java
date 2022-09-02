package Entity;

public abstract class Listing {
    private String description;
    private String title;
    private int type;
    private int id;
    private int createdById;

    public Listing() {
        this.description = null;
        this.title = null;
        this.type = 0; // 0 for fixed, 1 for auctions
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
        this.type = type;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
