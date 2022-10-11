package Domain;

import java.util.List;

public class SellerGroup {
    private int groupId;
    private String groupName;
    private List<Seller> sellerList;
    private List<Listing> listingList;
    private List<OrderItem> ordersList;

    protected SellerGroup(int groupId, String groupName, List<Seller> sellerList, List<Listing> listingList, List<OrderItem> ordersList) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.sellerList = sellerList;
        this.listingList = listingList;
        this.ordersList = ordersList;
    }

    public static SellerGroup create(int groupId, String groupName, List<Seller>sellerList, List<Listing> listingList, List<OrderItem> ordersList) {
        return new SellerGroup(groupId, groupName, sellerList, listingList, ordersList);
    }

    public void register() {
        // Register the seller group into the db
    }

    public void addSeller(Seller seller) {
        sellerList.add(seller);
    }

    public void removeSeller(Seller seller) {
        sellerList.removeIf(t -> (
                seller.getUserId() == t.getUserId()
                ));
    }

    public boolean addListing(Listing listing) {
        if(listing.getGroupId() != groupId) {
            return false;
        }
        listingList.add(listing);
        return true;
    }

    public void modifyListing(Listing listing) {
        deleteListing(listing);
        addListing(listing);
    }

    public void deleteListing(Listing listing) {
        listingList.removeIf(t -> (
                listing.getListingId() == t.getListingId()
        ));
    }

    public void addOrderItem(OrderItem oi) {
        ordersList.add(oi);
    }

    public void modifyOrderItem(OrderItem oi) {
        deleteOrderItem(oi);
        addOrderItem(oi);
    }

    public void deleteOrderItem(OrderItem oi) {
        ordersList.removeIf(t -> (
                oi.getOrderId() == t.getOrderId() && oi.getListingId() == t.getListingId()
                ));
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<Seller> getSellerList() {
        return sellerList;
    }

    public void setSellerList(List<Seller> sellerList) {
        this.sellerList = sellerList;
    }

    public List<Listing> getListingList() {
        return listingList;
    }

    public void setListingList(List<Listing> listingList) {
        this.listingList = listingList;
    }
}
