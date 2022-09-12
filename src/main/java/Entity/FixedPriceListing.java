package Entity;

import java.util.List;

public interface FixedPriceListing {
    public int getPrice();
    public void setPrice(int price);
    public int getId();
    public void setId(int id);
    public int getQuantity();
    public void setQuantity(int quantity);
    public int getFplId();
    public void setFplId(int fplId);
    public void load();
}
